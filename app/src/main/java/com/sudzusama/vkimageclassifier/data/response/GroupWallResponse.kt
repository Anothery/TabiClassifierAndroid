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
        @SerialName("groups")
        val groups: List<Group> = listOf(),
        @SerialName("items")
        val items: List<Item> = listOf(),
        @SerialName("profiles")
        val profiles: List<Profile> = listOf()
    ) {
        @Serializable
        data class Group(
            @SerialName("id")
            val id: Int = 0,
            @SerialName("is_admin")
            val isAdmin: Int = 0,
            @SerialName("is_advertiser")
            val isAdvertiser: Int = 0,
            @SerialName("is_closed")
            val isClosed: Int = 0,
            @SerialName("is_member")
            val isMember: Int = 0,
            @SerialName("name")
            val name: String = "",
            @SerialName("photo_50")
            val photo50: String = "",
            @SerialName("screen_name")
            val screenName: String = "",
            @SerialName("type")
            val type: String = ""
        )

        @Serializable
        data class Item(
            @SerialName("attachments")
            val attachments: List<Attachment> = listOf(),
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
            @SerialName("text")
            val text: String = "",
            @SerialName("views")
            val views: Views = Views()
        ) {
            @Serializable
            data class Attachment(
                @SerialName("photo")
                val photo: Photo = Photo(),
                @SerialName("type")
                val type: String = "",
                @SerialName("video")
                val video: Video = Video()
            ) {
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

                @Serializable
                data class Video(
                    @SerialName("access_key")
                    val accessKey: String = "",
                    @SerialName("can_add")
                    val canAdd: Int = 0,
                    @SerialName("can_add_to_faves")
                    val canAddToFaves: Int = 0,
                    @SerialName("can_comment")
                    val canComment: Int = 0,
                    @SerialName("can_like")
                    val canLike: Int = 0,
                    @SerialName("can_repost")
                    val canRepost: Int = 0,
                    @SerialName("can_subscribe")
                    val canSubscribe: Int = 0,
                    @SerialName("comments")
                    val comments: Int = 0,
                    @SerialName("date")
                    val date: Int = 0,
                    @SerialName("description")
                    val description: String = "",
                    @SerialName("duration")
                    val duration: Int = 0,
                    @SerialName("id")
                    val id: Int = 0,
                    @SerialName("image")
                    val image: List<Image> = listOf(),
                    @SerialName("is_favorite")
                    val isFavorite: Boolean = false,
                    @SerialName("local_views")
                    val localViews: Int = 0,
                    @SerialName("owner_id")
                    val ownerId: Int = 0,
                    @SerialName("platform")
                    val platform: String = "",
                    @SerialName("title")
                    val title: String = "",
                    @SerialName("track_code")
                    val trackCode: String = "",
                    @SerialName("type")
                    val type: String = "",
                    @SerialName("views")
                    val views: Int = 0
                ) {
                    @Serializable
                    data class Image(
                        @SerialName("height")
                        val height: Int = 0,
                        @SerialName("url")
                        val url: String = "",
                        @SerialName("width")
                        val width: Int = 0,
                        @SerialName("with_padding")
                        val withPadding: Int = 0
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
                @SerialName("is_deleted")
                val isDeleted: Boolean = false,
                @SerialName("owner_id")
                val ownerId: Int = 0,
                @SerialName("post_source")
                val postSource: PostSource = PostSource(),
                @SerialName("post_type")
                val postType: String = "",
                @SerialName("signer_id")
                val signerId: Int = 0,
                @SerialName("text")
                val text: String = ""
            ) {
                @Serializable
                data class Attachment(
                    @SerialName("doc")
                    val doc: Doc = Doc(),
                    @SerialName("link")
                    val link: Link = Link(),
                    @SerialName("photo")
                    val photo: Photo = Photo(),
                    @SerialName("type")
                    val type: String = ""
                ) {
                    @Serializable
                    data class Doc(
                        @SerialName("access_key")
                        val accessKey: String = "",
                        @SerialName("date")
                        val date: Int = 0,
                        @SerialName("ext")
                        val ext: String = "",
                        @SerialName("id")
                        val id: Int = 0,
                        @SerialName("owner_id")
                        val ownerId: Int = 0,
                        @SerialName("preview")
                        val preview: Preview = Preview(),
                        @SerialName("size")
                        val size: Int = 0,
                        @SerialName("title")
                        val title: String = "",
                        @SerialName("type")
                        val type: Int = 0,
                        @SerialName("url")
                        val url: String = ""
                    ) {
                        @Serializable
                        data class Preview(
                            @SerialName("photo")
                            val photo: Photo = Photo(),
                            @SerialName("video")
                            val video: Video = Video()
                        ) {
                            @Serializable
                            data class Photo(
                                @SerialName("sizes")
                                val sizes: List<Size> = listOf()
                            ) {
                                @Serializable
                                data class Size(
                                    @SerialName("height")
                                    val height: Int = 0,
                                    @SerialName("src")
                                    val src: String = "",
                                    @SerialName("type")
                                    val type: String = "",
                                    @SerialName("width")
                                    val width: Int = 0
                                )
                            }

                            @Serializable
                            data class Video(
                                @SerialName("file_size")
                                val fileSize: Int = 0,
                                @SerialName("height")
                                val height: Int = 0,
                                @SerialName("src")
                                val src: String = "",
                                @SerialName("width")
                                val width: Int = 0
                            )
                        }
                    }

                    @Serializable
                    data class Link(
                        @SerialName("caption")
                        val caption: String = "",
                        @SerialName("description")
                        val description: String = "",
                        @SerialName("is_favorite")
                        val isFavorite: Boolean = false,
                        @SerialName("photo")
                        val photo: Photo = Photo(),
                        @SerialName("target")
                        val target: String = "",
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
                data class PostSource(
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

        @Serializable
        data class Profile(
            @SerialName("can_access_closed")
            val canAccessClosed: Boolean = false,
            @SerialName("deactivated")
            val deactivated: String = "",
            @SerialName("first_name")
            val firstName: String = "",
            @SerialName("id")
            val id: Int = 0,
            @SerialName("is_closed")
            val isClosed: Boolean = false,
            @SerialName("last_name")
            val lastName: String = "",
            @SerialName("photo_50")
            val photo50: String = ""
        )
    }
}