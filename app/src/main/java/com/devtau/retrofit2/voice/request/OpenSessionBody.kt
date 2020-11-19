package com.devtau.retrofit2.voice.request

import com.google.gson.annotations.SerializedName
/**
 * Объект используется, как боди запроса на открытие сессии распознавания речи
 */
data class OpenSessionBody(
    @SerializedName("client_info")
    private val clientInfo: ClientInfo = ClientInfo(),
    @SerializedName("username")
    private val username: String = "vk_user",
    @SerializedName("domain_id")
    private val domainId: String = "201",
    @SerializedName("password")
    private val password: String = "123"
) {

    data class ClientInfo(
        private val type: String = "MOBILE",
        private val description: String = "iPhone X, 128GB, iOS 12"
    )
}