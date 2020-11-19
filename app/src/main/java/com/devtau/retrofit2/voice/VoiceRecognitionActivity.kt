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
import java.io.*

class VoiceRecognitionActivity: AppCompatActivity(), VoiceRecognitionActivityView {

    private var restClient = RESTClientVoice(this)
    private var sessionId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice)
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
        updateSessionId(DEFAULT_SESSION_ID)

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
            restClient.recognizeStream(sessionId)
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
        session_id_header.isVisible = sessionId.isNotEmpty()
        session_id_value.text = sessionId
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 4582
        private const val DEFAULT_SESSION_ID = "7c11782d-552b-4953-a750-c48bef5d6b2e"
        private const val DEFAULT_FILE_DIR = "/storage/emulated/0/Download"
        private const val DEFAULT_FILE_NAME = "test_16000.wav"
    }
}