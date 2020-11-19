package com.devtau.retrofit2.voice.request

/**
 * Audio stream recognition request body
 * @param mime - тип входных аудиоданных. обязательный
 *               audio/pcm16: необработанные (RAW) аудиоданные в формате PCM
 *               с частотой дискретизации 8кГц и глубиной квантования 16 бит
 *               audio/l16: необработанные (RAW) аудиоданные в формате PCM
 *               с частотой дискретизации 16кГц и глубиной квантования 16 бит
 * @param package_id - идентификатор пакета. необязательный
 */
data class StreamRecognitionRequestBody(
    private val mime: String? = "audio/l16",
    private val package_id: String? = "CommonRus"
)