package com.sudzusama.vkimageclassifier.data.response.vk

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VkResponse<R>(
    @SerialName("response") val response: R? = null,
    @SerialName("error") val error: VkApiError? = null
)