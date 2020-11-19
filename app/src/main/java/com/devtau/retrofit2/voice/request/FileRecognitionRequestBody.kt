package com.devtau.retrofit2.voice.request

/**
 * Audio file recognition request body
 * @param audio - Binary audio file as Base64 string
 * @param package_id - идентификатор сессии
 */
data class FileRecognitionRequestBody(
    private val audio: AudioFile,
    private val package_id: String? = null
) {

    /**
     * Audio file to be recognized
     * @param data - Binary audio file as Base64 string
     * @param mime - Audio file mime type. optional
     */
    data class AudioFile(
        private val data: String,
        private val mime: String? = "audio/wav"
    )
}