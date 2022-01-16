package com.sudzusama.vkimageclassifier.data.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveWallResponse(
    @SerialName("response")
    val response: List<Response>
) {
    @Serializable
    data class Response(
        @SerialName("access_key")
        val accessKey: String,
        @SerialName("album_id")
        val albumId: Int,
        @SerialName("date")
        val date: Int,
        @SerialName("has_tags")
        val hasTags: Boolean,
        @SerialName("id")
        val id: Int,
        @SerialName("owner_id")
        val ownerId: Int,
        @SerialName("sizes")
        val sizes: List<Size>,
        @SerialName("text")
        val text: String
    ) {
        @Serializable
        data class Size(
            @SerialName("height")
            val height: Int,
            @SerialName("type")
            val type: String,
            @SerialName("url")
            val url: String,
            @SerialName("width")
            val width: Int
        )
    }
}