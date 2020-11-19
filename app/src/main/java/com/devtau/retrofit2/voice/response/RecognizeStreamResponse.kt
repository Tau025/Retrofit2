package com.devtau.retrofit2.voice.response

/**
 * Audio stream recognition response body
 * @param url - ссылка для подключения по протоколу Websocket
 *              идентификатор транзакции X- Transaction-Id передаётся в url
 *              Например:  ws://172.17.0.15:9090/v1/recognize/stream/{X-Transaction-Id}
 */
data class RecognizeStreamResponse(
    val url: String
)