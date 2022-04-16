package com.sudzusama.vkimageclassifier.ui.groupdetail.wall

import android.app.AlertDialog
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.RequestManager
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.ContentProgressBarBinding
import com.sudzusama.vkimageclassifier.databinding.GroupWallItemBinding
import com.sudzusama.vkimageclassifier.databinding.WallOptionDialogBinding
import com.sudzusama.vkimageclassifier.domain.model.WallItem
import com.sudzusama.vkimageclassifier.ui.imagedetail.ImageDetail
import com.sudzusama.vkimageclassifier.utils.view.VkSpannableHelper
import org.ocpsoft.prettytime.PrettyTime
import java.util.*


class WallAdapter(
    private val posts: ArrayList<WallItem?>,
    private val context: Context,
    private val glide: RequestManager,
    private val vkSpannableHelper: VkSpannableHelper,
    private val onPostLiked: (Int, Boolean) -> Unit,
    private val onImageClicked: (List<ImageDetail>, Int) -> Unit,
    private val onDownloadMore: () -> Unit,
    private val onPostRemoved: (Int) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var downloadMore = true
    private var isLoading = false
    private var showDeletePrompt = false


    fun setWall(newList: List<WallItem>) {
        val list =
            if (!downloadMore || newList.isEmpty()) newList
            else mutableListOf<WallItem?>().apply { addAll(newList); add(null) }

        val diffResult = DiffUtil.calculateDiff(WallDiffCallback(posts, list))
        posts.clear()
        posts.addAll(list)
        diffResult.dispatchUpdatesTo(this@WallAdapter)

    }

    fun setDownloadMore(downloadMore: Boolean) {
        this.downloadMore = downloadMore
        setWall(posts.filterNotNull())
    }

    fun setIsDataLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_PROGRESS -> {
                val binding = ContentProgressBarBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ProgressViewHolder(binding)
            }
            else -> {
                val binding =
                    GroupWallItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                binding.rvWallItemImages.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                binding.rvWallItemImages.addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        val density = parent.context.resources.displayMetrics.density
                        val position = parent.getChildAdapterPosition(view)

                        if (state.itemCount == 2) {
                            when (position) {
                                0 -> outRect.right = (2 * density).toInt()
                                1 -> outRect.left = (2 * density).toInt()
                            }
                            return
                        }

                        if (position % 3 in 0..1 && position == state.itemCount - 1) return

                        outRect.bottom = (4 * density).toInt()

                        when (position % 3) {
                            1 -> outRect.right = (2 * density).toInt()
                            2 -> outRect.left = (2 * density).toInt()
                        }
                    }
                })
                val adapter = PostImageAdapter(glide, onImageClicked)
                binding.rvWallItemImages.adapter = adapter
                return WallItemViewHolder(binding, adapter)
            }
        }
    }

    fun showDeletePrompt(enabled: Boolean) {
        showDeletePrompt = enabled
    }

    override fun getItemViewType(position: Int): Int {
        return if (posts[position] != null) VIEW_DEFAULT else VIEW_PROGRESS
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is WallItemViewHolder) holder.bind(posts[position]!!)
        else {
            if (!isLoading) {
                onDownloadMore()
                isLoading = true
            }
        }
    }

    inner class ProgressViewHolder(binding: ContentProgressBarBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class WallItemViewHolder(
        val binding: GroupWallItemBinding,
        private val adapter: PostImageAdapter
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var menuDialog: AlertDialog

        fun bind(wallItem: WallItem) {
            initDialog(wallItem, showDeletePrompt)
            if (wallItem.images.isEmpty() && wallItem.text.isBlank()) {
                binding.tvText.setTextColor(
                    ContextCompat.getColor(context, R.color.colorOnPrimaryDisabled)
                )
                binding.tvText.text = context.resources.getString(R.string.wall_item_no_content)
            } else {
                binding.tvText.setTextColor(ContextCompat.getColor(context, R.color.colorOnPrimary))
                binding.tvText.text = vkSpannableHelper.formatToSpannable(wallItem.text)
                binding.tvText.movementMethod = LinkMovementMethod.getInstance()
            }
            val locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.resources.configuration.locales[0]
            } else context.resources.configuration.locale
            binding.tvDate.text = PrettyTime().setLocale(locale).format(Date(wallItem.date * 1000L))
            binding.tvLikeCount.text = "${if (wallItem.likesCount > 0) wallItem.likesCount else ""}"
            binding.tvPosterName.text = wallItem.posterName
            glide.load(wallItem.posterThumbnail)
                .error(R.drawable.group_stub_avatar)
                .circleCrop()
                .into(binding.ivPosterAvatar)

            if (wallItem.userLikes > 0) {
                binding.ivLike.setImageResource(R.drawable.outline_favorite_24)
            }

            binding.ivLike.setOnClickListener {
                val isLiked = wallItem.userLikes > 0
                onPostLiked(wallItem.id, isLiked)
                if (wallItem.userLikes > 0) {
                    wallItem.likesCount--
                    wallItem.userLikes = 0
                    binding.ivLike.setImageResource(R.drawable.outline_favorite_border_24)
                } else {
                    wallItem.likesCount++
                    wallItem.userLikes++
                    binding.ivLike.setImageResource(R.drawable.outline_favorite_24)
                }
                binding.tvLikeCount.text = "${wallItem.likesCount}"
            }

            adapter.setImages(wallItem.images)
        }

        private fun initDialog(wallItem: WallItem, showDeletePrompt: Boolean) {
            val menuBinding = WallOptionDialogBinding.inflate(LayoutInflater.from(context))
            menuDialog = AlertDialog.Builder(context).setView(menuBinding.root).create()
            menuDialog.window?.decorView?.background?.alpha = 0
            if (showDeletePrompt) {
                binding.root.setOnLongClickListener {
                    menuDialog.show()
                    menuBinding.tvDeletePost.setOnClickListener {
                        onPostRemoved(wallItem.id)
                        menuDialog.dismiss()
                    }
                    true
                }
            }

        }
    }

    companion object {
        const val VIEW_DEFAULT = 0
        const val VIEW_PROGRESS = 1
    }
}