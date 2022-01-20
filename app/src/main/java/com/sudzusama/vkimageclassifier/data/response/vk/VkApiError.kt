package com.sudzusama.vkimageclassifier.data.response.vk


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VkApiError(
    @SerialName("error_code")
    val errorCode: Int,
    @SerialName("error_msg")
    val errorMsg: String,
    @SerialName("request_params")
    val requestParams: List<RequestParam>
) {
    @Serializable
    data class RequestParam(
        @SerialName("key")
        val key: String,
        @SerialName("value")
        val value: String
    )
}
