package com.devtau.retrofit2.voice

import android.Manifest
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.devtau.retrofit2.R
import com.devtau.retrofit2.util.isVisible
import com.devtau.retrofit2.voice.request.FileRecognitionRequestBody
import io.reactivex.functions.Action
import kotlinx.android.synthetic.main.activity_voice.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.ByteString
import okio.ByteString.Companion.decodeHex
import timber.log.Timber
import java.io.*
import java.util.concurrent.TimeUnit

class VoiceRecognitionActivity: AppCompatActivity(), VoiceRecognitionActivityView {

    private var restClient = RESTClientVoice(this)
    private var sessionId = ""
    private var webSocketUrl = ""
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .readTimeout(2000, TimeUnit.MILLISECONDS)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice)
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
        updateSessionId(DEFAULT_SESSION_ID)
        updateWebSocketUrl(DEFAULT_WEB_SOCKET_URL)

        open_session_button.setOnClickListener {
            restClient.openSession {
                updateSessionId(it)
            }
        }

        recognize_file_button.setOnClickListener {
            val file = prepareFile()
            if (file == null) {
                showMsg("file not parsed")
            } else {
                restClient.recognizeFile(file, sessionId)
            }
        }

        recognize_stream_button.setOnClickListener {
            Timber.d("recognize_stream_button clicked")
            restClient.recognizeStream(sessionId) {
                Timber.d("recognizeStream $it")
                updateWebSocketUrl(it)
            }
        }

        connect_to_web_socket_button.setOnClickListener {
            Timber.d("connect_to_web_socket_button clicked")
            val request = Request.Builder().url(webSocketUrl).build()
            val ws = httpClient.newWebSocket(request, EchoWebSocketListener())
            httpClient.dispatcher.executorService.shutdown()
        }
    }

    override fun showProgress(show: Boolean) {
        progress.isVisible = show
    }

    override fun showMsg(msgId: Int, confirmedListener: Action?, cancelledListener: Action?) {
        showMsg(getString(msgId), confirmedListener, cancelledListener)
    }

    override fun showMsg(msg: String, confirmedListener: Action?, cancelledListener: Action?) {
        showProgress(false)
        val dialog = AlertDialog.Builder(this).setMessage(msg)
        dialog.setPositiveButton(android.R.string.ok) { _, _ -> confirmedListener?.run() }
        if (cancelledListener != null) {
            dialog.setNegativeButton(android.R.string.ok) { _, _ -> cancelledListener.run() }
        }
        dialog.show()
    }



    private fun prepareFile(): FileRecognitionRequestBody.AudioFile? {
        val audioFile = File(DEFAULT_FILE_DIR, DEFAULT_FILE_NAME)

        val bytes = ByteArray(audioFile.length().toInt())
        try {
            val buf = BufferedInputStream(FileInputStream(audioFile))
            buf.read(bytes, 0, bytes.size)
            buf.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        val encoded: String = Base64.encodeToString(bytes, 0)
        return if (encoded.isEmpty()) null else FileRecognitionRequestBody.AudioFile(encoded, "audio/wav")
    }

    private fun updateSessionId(newSessionId: String) {
        sessionId = newSessionId
        session_id_header.isVisible = newSessionId.isNotEmpty()
        session_id_value.text = newSessionId
    }

    private fun updateWebSocketUrl(newWebSocketUrl: String) {
        webSocketUrl = newWebSocketUrl
        web_socket_url_header.isVisible = newWebSocketUrl.isNotEmpty()
        web_socket_url_value.text = newWebSocketUrl
    }

    private class EchoWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Timber.d("onOpen")
            webSocket.send("Hello, it's devtau!")
            webSocket.send("What's up?")
            webSocket.send("deadbeef".decodeHex())
            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye!")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Timber.d("onMessage. text=$text")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Timber.d("onMessage. bytes=${bytes.hex()}")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null)
            Timber.d("onClosing. code=$code, reason=$reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Timber.d("onFailure. error message=${t.message}")
        }

        companion object {
            private const val NORMAL_CLOSURE_STATUS = 1000
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 4582
        private const val DEFAULT_SESSION_ID = "7c11782d-552b-4953-a750-c48bef5d6b2e"
        private const val DEFAULT_FILE_DIR = "/storage/emulated/0/Download"
        private const val DEFAULT_FILE_NAME = "test_16000.wav"
//        private const val DEFAULT_WEB_SOCKET_URL = "ws://demos.kaazing.com/echo"
//        private const val DEFAULT_WEB_SOCKET_URL = "wss://echo.websocket.org/"
        private const val DEFAULT_WEB_SOCKET_URL = "ws://sda-tccdr42.ad.speechpro.com/vkasr1/v1/recognize/stream/text/1a3238c1-eca1-484e-ae1a-f8cb7d475207"
    }
}