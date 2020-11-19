package com.devtau.retrofit2.voice

import android.Manifest
import android.os.Bundle
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.FileUtils
import android.util.Base64
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.devtau.retrofit2.R
import com.devtau.retrofit2.voice.request.RecognitionRequestBody
import io.reactivex.functions.Action
import kotlinx.android.synthetic.main.activity_voice.*
import java.io.*

class VoiceRecognitionActivity: AppCompatActivity(), VoiceRecognitionActivityView {

    private var restClient = RESTClientVoice(this)
    private var sessionId = "7c11782d-552b-4953-a750-c48bef5d6b2e"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice)
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)

        open_session_button.setOnClickListener {
            restClient.openSession {
                sessionId = it
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
    }

    override fun showProgress(show: Boolean) {
        progress.visibility = if (show) View.VISIBLE else View.GONE
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



    private fun prepareFile(): RecognitionRequestBody.AudioFile? {
        val audioFile = File("/storage/emulated/0/Download", "test_16000.wav")

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
        return if (encoded.isEmpty()) null else RecognitionRequestBody.AudioFile(encoded, "audio/wav")
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 4582
    }
}