package com.sudzusama.vkimageclassifier.data.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupWallResponse(
    @SerialName("response")
    val response: Response = Response()
) {
    @Serializable
    data class Response(
        @SerialName("count")
        val count: Int = 0,
        @SerialName("items")
        val items: List<Item> = listOf()
    ) {
        @Serializable
        data class Item(
            @SerialName("attachments")
            val attachments: List<Attachment> = listOf(),
            @SerialName("carousel_offset")
            val carouselOffset: Int = 0,
            @SerialName("comments")
            val comments: Comments = Comments(),
            @SerialName("copy_history")
            val copyHistory: List<CopyHistory> = listOf(),
            @SerialName("date")
            val date: Int = 0,
            @SerialName("donut")
            val donut: Donut = Donut(),
            @SerialName("edited")
            val edited: Int = 0,
            @SerialName("from_id")
            val fromId: Int = 0,
            @SerialName("hash")
            val hash: String = "",
            @SerialName("id")
            val id: Int = 0,
            @SerialName("is_favorite")
            val isFavorite: Boolean = false,
            @SerialName("is_pinned")
            val isPinned: Int = 0,
            @SerialName("likes")
            val likes: Likes = Likes(),
            @SerialName("marked_as_ads")
            val markedAsAds: Int = 0,
            @SerialName("owner_id")
            val ownerId: Int = 0,
            @SerialName("post_source")
            val postSource: PostSource = PostSource(),
            @SerialName("post_type")
            val postType: String = "",
            @SerialName("reposts")
            val reposts: Reposts = Reposts(),
            @SerialName("short_text_rate")
            val shortTextRate: Double = 0.0,
            @SerialName("signer_id")
            val signerId: Int = 0,
            @SerialName("text")
            val text: String = "",
            @SerialName("views")
            val views: Views = Views()
        ) {
            @Serializable
            data class Attachment(
                @SerialName("audio")
                val audio: Audio = Audio(),
                @SerialName("link")
                val link: Link = Link(),
                @SerialName("photo")
                val photo: Photo = Photo(),
                @SerialName("type")
                val type: String = ""
            ) {
                @Serializable
                data class Audio(
                    @SerialName("artist")
                    val artist: String = "",
                    @SerialName("date")
                    val date: Int = 0,
                    @SerialName("duration")
                    val duration: Int = 0,
                    @SerialName("genre_id")
                    val genreId: Int = 0,
                    @SerialName("id")
                    val id: Int = 0,
                    @SerialName("is_explicit")
                    val isExplicit: Boolean = false,
                    @SerialName("is_focus_track")
                    val isFocusTrack: Boolean = false,
                    @SerialName("main_artists")
                    val mainArtists: List<MainArtist> = listOf(),
                    @SerialName("owner_id")
                    val ownerId: Int = 0,
                    @SerialName("short_videos_allowed")
                    val shortVideosAllowed: Boolean = false,
                    @SerialName("stories_allowed")
                    val storiesAllowed: Boolean = false,
                    @SerialName("stories_cover_allowed")
                    val storiesCoverAllowed: Boolean = false,
                    @SerialName("title")
                    val title: String = "",
                    @SerialName("track_code")
                    val trackCode: String = "",
                    @SerialName("url")
                    val url: String = ""
                ) {
                    @Serializable
                    data class MainArtist(
                        @SerialName("domain")
                        val domain: String = "",
                        @SerialName("id")
                        val id: String = "",
                        @SerialName("name")
                        val name: String = ""
                    )
                }

                @Serializable
                data class Link(
                    @SerialName("description")
                    val description: String = "",
                    @SerialName("photo")
                    val photo: Photo = Photo(),
                    @SerialName("title")
                    val title: String = "",
                    @SerialName("url")
                    val url: String = ""
                ) {
                    @Serializable
                    data class Photo(
                        @SerialName("album_id")
                        val albumId: Int = 0,
                        @SerialName("date")
                        val date: Int = 0,
                        @SerialName("has_tags")
                        val hasTags: Boolean = false,
                        @SerialName("id")
                        val id: Int = 0,
                        @SerialName("owner_id")
                        val ownerId: Int = 0,
                        @SerialName("sizes")
                        val sizes: List<Size> = listOf(),
                        @SerialName("text")
                        val text: String = "",
                        @SerialName("user_id")
                        val userId: Int = 0
                    ) {
                        @Serializable
                        data class Size(
                            @SerialName("height")
                            val height: Int = 0,
                            @SerialName("type")
                            val type: String = "",
                            @SerialName("url")
                            val url: String = "",
                            @SerialName("width")
                            val width: Int = 0
                        )
                    }
                }

                @Serializable
                data class Photo(
                    @SerialName("access_key")
                    val accessKey: String = "",
                    @SerialName("album_id")
                    val albumId: Int = 0,
                    @SerialName("date")
                    val date: Int = 0,
                    @SerialName("has_tags")
                    val hasTags: Boolean = false,
                    @SerialName("id")
                    val id: Int = 0,
                    @SerialName("owner_id")
                    val ownerId: Int = 0,
                    @SerialName("post_id")
                    val postId: Int = 0,
                    @SerialName("sizes")
                    val sizes: List<Size> = listOf(),
                    @SerialName("text")
                    val text: String = "",
                    @SerialName("user_id")
                    val userId: Int = 0
                ) {
                    @Serializable
                    data class Size(
                        @SerialName("height")
                        val height: Int = 0,
                        @SerialName("type")
                        val type: String = "",
                        @SerialName("url")
                        val url: String = "",
                        @SerialName("width")
                        val width: Int = 0
                    )
                }
            }

            @Serializable
            data class Comments(
                @SerialName("can_post")
                val canPost: Int = 0,
                @SerialName("count")
                val count: Int = 0,
                @SerialName("groups_can_post")
                val groupsCanPost: Boolean = false
            )

            @Serializable
            data class CopyHistory(
                @SerialName("attachments")
                val attachments: List<Attachment> = listOf(),
                @SerialName("date")
                val date: Int = 0,
                @SerialName("from_id")
                val fromId: Int = 0,
                @SerialName("id")
                val id: Int = 0,
                @SerialName("owner_id")
                val ownerId: Int = 0,
                @SerialName("post_source")
                val postSource: PostSource = PostSource(),
                @SerialName("post_type")
                val postType: String = "",
                @SerialName("text")
                val text: String = ""
            ) {
                @Serializable
                data class Attachment(
                    @SerialName("audio")
                    val audio: Audio = Audio(),
                    @SerialName("photo")
                    val photo: Photo = Photo(),
                    @SerialName("type")
                    val type: String = ""
                ) {
                    @Serializable
                    data class Audio(
                        @SerialName("artist")
                        val artist: String = "",
                        @SerialName("date")
                        val date: Int = 0,
                        @SerialName("duration")
                        val duration: Int = 0,
                        @SerialName("genre_id")
                        val genreId: Int = 0,
                        @SerialName("id")
                        val id: Int = 0,
                        @SerialName("is_explicit")
                        val isExplicit: Boolean = false,
                        @SerialName("is_focus_track")
                        val isFocusTrack: Boolean = false,
                        @SerialName("owner_id")
                        val ownerId: Int = 0,
                        @SerialName("short_videos_allowed")
                        val shortVideosAllowed: Boolean = false,
                        @SerialName("stories_allowed")
                        val storiesAllowed: Boolean = false,
                        @SerialName("stories_cover_allowed")
                        val storiesCoverAllowed: Boolean = false,
                        @SerialName("title")
                        val title: String = "",
                        @SerialName("track_code")
                        val trackCode: String = "",
                        @SerialName("url")
                        val url: String = ""
                    )

                    @Serializable
                    data class Photo(
                        @SerialName("access_key")
                        val accessKey: String = "",
                        @SerialName("album_id")
                        val albumId: Int = 0,
                        @SerialName("date")
                        val date: Int = 0,
                        @SerialName("has_tags")
                        val hasTags: Boolean = false,
                        @SerialName("id")
                        val id: Int = 0,
                        @SerialName("owner_id")
                        val ownerId: Int = 0,
                        @SerialName("post_id")
                        val postId: Int = 0,
                        @SerialName("sizes")
                        val sizes: List<Size> = listOf(),
                        @SerialName("text")
                        val text: String = "",
                        @SerialName("user_id")
                        val userId: Int = 0
                    ) {
                        @Serializable
                        data class Size(
                            @SerialName("height")
                            val height: Int = 0,
                            @SerialName("type")
                            val type: String = "",
                            @SerialName("url")
                            val url: String = "",
                            @SerialName("width")
                            val width: Int = 0
                        )
                    }
                }

                @Serializable
                data class PostSource(
                    @SerialName("platform")
                    val platform: String = "",
                    @SerialName("type")
                    val type: String = ""
                )
            }

            @Serializable
            data class Donut(
                @SerialName("is_donut")
                val isDonut: Boolean = false
            )

            @Serializable
            data class Likes(
                @SerialName("can_like")
                val canLike: Int = 0,
                @SerialName("can_publish")
                val canPublish: Int = 0,
                @SerialName("count")
                val count: Int = 0,
                @SerialName("user_likes")
                val userLikes: Int = 0
            )

            @Serializable
            data class PostSource(
                @SerialName("platform")
                val platform: String = "",
                @SerialName("type")
                val type: String = ""
            )

            @Serializable
            data class Reposts(
                @SerialName("count")
                val count: Int = 0,
                @SerialName("user_reposted")
                val userReposted: Int = 0
            )

            @Serializable
            data class Views(
                @SerialName("count")
                val count: Int = 0
            )
        }
    }
}