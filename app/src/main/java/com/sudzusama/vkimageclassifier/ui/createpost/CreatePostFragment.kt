package com.sudzusama.vkimageclassifier.ui.createpost

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.TooltipCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.FragmentCreatePostBinding
import com.sudzusama.vkimageclassifier.databinding.PostTimePickerBinding
import com.sudzusama.vkimageclassifier.domain.model.GroupDetail
import com.sudzusama.vkimageclassifier.ui.createpost.gallery.GalleryAdapter
import com.sudzusama.vkimageclassifier.ui.createpost.gallery.GalleryItem
import com.sudzusama.vkimageclassifier.ui.createpost.pictures.Picture
import com.sudzusama.vkimageclassifier.ui.createpost.pictures.PicturesAdapter
import com.sudzusama.vkimageclassifier.ui.createpost.tags.TagsAdapter
import com.sudzusama.vkimageclassifier.utils.view.gone
import com.sudzusama.vkimageclassifier.utils.view.shortToast
import com.sudzusama.vkimageclassifier.utils.view.visible
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class CreatePostFragment : BottomSheetDialogFragment() {
    private var binding: FragmentCreatePostBinding? = null
    private val viewModel: CreatePostViewModel by viewModels()
    private var galleryAdapter: GalleryAdapter? = null
    private var picturesAdapter: PicturesAdapter? = null
    private var genresAdapter: TagsAdapter? = null
    private var colorsAdapter: TagsAdapter? = null
    private var datePickerDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCreatePostBinding.inflate(inflater).apply { binding = this }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val details = arguments?.getParcelable<GroupDetail>(GROUP_DETAIL)
        val intentImageUri = arguments?.getString(INTENT_IMAGE)

        initPictures()
        initGenres()
        initColors()

        TooltipCompat.setTooltipText(
            binding?.btnSetDefaultTags as View,
            getString(R.string.button_set_default_tags)
        );
        TooltipCompat.setTooltipText(
            binding?.btnSave as View,
            getString(R.string.button_send_post)
        );
        TooltipCompat.setTooltipText(
            binding?.btnTagsRecognition as View,
            getString(R.string.button_recognition)
        );
        TooltipCompat.setTooltipText(binding?.btnClose as View, getString(R.string.button_close));
        TooltipCompat.setTooltipText(
            binding?.btnSelectTime as View,
            getString(R.string.button_select_time)
        )

        binding?.tvTitle?.text =
            if (details?.canPost == true) getString(R.string.create_post) else getString(R.string.suggest_post)
        binding?.btnSave?.setOnClickListener { viewModel.onSaveButtonClicked() }
        binding?.btnClose?.setOnClickListener { dismiss() }
        binding?.btnSetDefaultTags?.setOnClickListener { viewModel.onSetDefaultTagsClicked() }
        binding?.btnTagsRecognition?.setOnClickListener { viewModel.onChangeTagsRecognition() }
        binding?.llMain?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)


        intentImageUri?.let { viewModel.onIntentUriCatched(it) }
        details?.let {
            if (it.canPost) binding?.btnSelectTime?.visible() else binding?.btnSelectTime?.gone()

        }
        binding?.btnSelectTime?.setOnClickListener { viewModel.onSelectTimeButtonClicked() }

        viewModel.setGroupDetails(details)
        viewModel.galleryItems.observe(viewLifecycleOwner, ::initGallery)
        viewModel.pictures.observe(viewLifecycleOwner, ::onPicturesUpdated)
        viewModel.selectedItem.observe(viewLifecycleOwner) { galleryAdapter?.selectItem(it) }
        viewModel.deselectedItem.observe(viewLifecycleOwner) { galleryAdapter?.deselectItem(it) }
        viewModel.showDateScreen.observe(viewLifecycleOwner) { showDatePickerDialog(it) }
        viewModel.pickerDate.observe(viewLifecycleOwner) {
            val defaultCalendar =
                ContextCompat.getDrawable(requireContext(), R.drawable.outline_edit_calendar_24)
            val calendarWithDate =
                ContextCompat.getDrawable(requireContext(), R.drawable.outline_event_available_24)

            if (it == null) {
                binding?.btnSelectTime?.setImageDrawable(defaultCalendar)
                binding?.btnSelectTime?.setColorFilter(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.colorOnPrimary,
                        null
                    )
                )
            } else {
                binding?.btnSelectTime?.setImageDrawable(calendarWithDate)
                binding?.btnSelectTime?.setColorFilter(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.light_blue_400,
                        null
                    )
                )
            }


        }


        viewModel.genreTags.observe(viewLifecycleOwner) { tags ->
            genresAdapter?.let { binding?.rvGenreTags?.post { it.setTags(tags) } }
        }
        viewModel.colorTags.observe(viewLifecycleOwner) { tags ->
            colorsAdapter?.let { adapter ->
                val shouldScroll = adapter.itemCount != 0 && tags.isNotEmpty()
                binding?.rvColorTags?.post {
                    adapter.setTags(tags)
                    if (shouldScroll) binding?.rvColorTags?.scrollToPosition(0)
                }
            }
        }
        viewModel.showMessage.observe(viewLifecycleOwner) { context?.shortToast(it) }
        viewModel.onPostSent.observe(viewLifecycleOwner) {
            activity?.supportFragmentManager?.setFragmentResult(ON_POST_CREATED, bundleOf())
            dismiss()
        }
        viewModel.tagsRecognition.observe(viewLifecycleOwner) { enabled ->
            binding?.btnTagsRecognition?.setImageDrawable(
                if (enabled) ContextCompat.getDrawable(
                    requireContext(), R.drawable.outline_blur_on_24
                ) else ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.outline_blur_off_24
                )
            )
        }

        viewModel.postingState.observe(viewLifecycleOwner) { posting ->
            if (posting) disableControls() else enableControls()
        }


    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initGallery(items: List<GalleryItem>) {
        binding?.rvGallery?.layoutManager = GridLayoutManager(context, 3)
        binding?.rvGallery?.setHasFixedSize(true)
        galleryAdapter =
            GalleryAdapter(ArrayList(items), Glide.with(this)) { item, position ->
                if (binding?.rvGallery?.isEnabled == true) viewModel.onGalleryItemClicked(
                    item,
                    position
                )
            }
        (binding?.rvGallery?.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        binding?.rvGallery?.adapter = galleryAdapter
    }

    private fun initColors() {
        colorsAdapter = TagsAdapter(viewModel::onColorCheckUpdate)
        binding?.rvColorTags?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvColorTags?.adapter = colorsAdapter
    }

    private fun initGenres() {
        genresAdapter = TagsAdapter(viewModel::onGenreCheckUpdate)
        binding?.rvGenreTags?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvGenreTags?.adapter = genresAdapter
    }

    private fun showDatePickerDialog(date: Date?) {
        val selectedDate = Calendar.getInstance()
        date?.let(selectedDate::setTime)
        context?.let { context ->
            var isFirstPage = true
            val iconNext = ContextCompat.getDrawable(context, R.drawable.ic_forward)
            val iconSave = ContextCompat.getDrawable(context, R.drawable.outline_save_24)
            val binding = PostTimePickerBinding.inflate(LayoutInflater.from(context))
            binding.datePicker.minDate = Calendar.getInstance().timeInMillis
            binding.datePicker.updateDate(
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
            )
            binding.icErase.setOnClickListener {
                viewModel.onDatePicked(null)
                datePickerDialog?.hide()
            }
            binding.timePicker.setIs24HourView(true)
            binding.timePicker.currentHour = selectedDate.get(Calendar.HOUR_OF_DAY)
            binding.timePicker.currentMinute = selectedDate.get(Calendar.MINUTE)
            binding.icBack.gone()
            binding.icNext.setImageDrawable(iconNext)
            binding.tvTitle.text = getString(R.string.choose_date)
            binding.icNext.setOnClickListener {
                if (isFirstPage) {
                    binding.tvTitle.text = getString(R.string.choose_time)
                    binding.icNext.setImageDrawable(null)
                    binding.icNext.setImageDrawable(iconSave)
                    binding.icBack.visible()
                    binding.timePicker.gone()
                    binding.datePicker.gone()
                    binding.timePicker.visible()
                    isFirstPage = false
                } else {
                    val calendar = GregorianCalendar()
                    calendar.set(
                        binding.datePicker.year,
                        binding.datePicker.month,
                        binding.datePicker.dayOfMonth
                    )

                    calendar.set(Calendar.HOUR_OF_DAY, binding.timePicker.currentHour)
                    calendar.set(Calendar.MINUTE, binding.timePicker.currentMinute)
                    viewModel.onDatePicked(calendar.time)
                    datePickerDialog?.hide()
                }
            }
            binding.icBack.setOnClickListener {
                binding.tvTitle.text = getString(R.string.choose_date)
                binding.icBack.gone()
                binding.icNext.setImageDrawable(null)
                binding.icNext.setImageDrawable(iconNext)
                binding.datePicker.visible()
                binding.timePicker.gone()
                isFirstPage = true
            }
            val dialog = AlertDialog.Builder(context).setView(binding.root).create()
            dialog.window?.decorView?.background?.alpha = 0
            dialog.show()
            datePickerDialog = dialog
        }
    }

    private fun onPicturesUpdated(pictures: List<Picture>) {
        picturesAdapter?.let { adapter ->
            val prevItemCount = adapter.itemCount
            if (pictures.isNotEmpty() && adapter.itemCount == 0) {
                binding?.flEmptyPicture?.animate()?.alpha(0f)?.scaleX(0f)?.scaleY(0f)
                    ?.withEndAction { binding?.flEmptyPicture?.gone() }
                binding?.rvPictures?.visible()
                binding?.rvPictures?.alpha = 0f
                binding?.rvPictures?.animate()?.alpha(1f)
            } else if (pictures.isEmpty() && adapter.itemCount != 0) {
                binding?.rvGallery?.isEnabled = false
                binding?.flEmptyPicture?.visible()
                binding?.flEmptyPicture?.alpha = 0f
                binding?.flEmptyPicture?.scaleX = 0f
                binding?.flEmptyPicture?.scaleY = 0f
                binding?.flEmptyPicture?.animate()?.alpha(1f)?.scaleX(1f)?.scaleY(1f)
                binding?.rvPictures?.animate()?.alpha(0f)?.withEndAction {
                    binding?.rvPictures?.gone()
                    binding?.rvPictures?.layoutParams?.width =
                        RecyclerView.LayoutParams.MATCH_PARENT
                    binding?.rvGallery?.isEnabled = true
                }
                adapter.setPictures(pictures)
                if (prevItemCount < pictures.size) {
                    binding?.rvPictures?.scrollToPosition(pictures.lastIndex)
                }
                return@let
            }

            binding?.rvPictures?.layoutParams?.width = RecyclerView.LayoutParams.MATCH_PARENT
            adapter.setPictures(pictures)
            if (prevItemCount < pictures.size) {
                binding?.rvPictures?.scrollToPosition(pictures.lastIndex)
            }
            if (adapter.itemCount == 1 && pictures.size > 1) {
                binding?.rvPictures?.layoutParams?.width = RecyclerView.LayoutParams.WRAP_CONTENT
            }
        }
    }

    private fun initPictures() {
        picturesAdapter =
            PicturesAdapter(
                Glide.with(this),
                viewModel::onRemovePictureClicked,
                requireContext()
            )
        binding?.rvPictures?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvPictures?.adapter = picturesAdapter
        PagerSnapHelper().attachToRecyclerView(binding?.rvPictures)
        binding?.rvPictures?.let { binding?.rvIndicator?.attachToRecyclerView(it) }
    }

    private fun disableControls() {
        binding?.btnSave?.isEnabled = false
        binding?.btnTagsRecognition?.isEnabled = false
        binding?.btnSetDefaultTags?.isEnabled = false
        binding?.flPosting?.visible()
        binding?.flPosting?.alpha = 0f
        binding?.flPosting?.animate()?.alpha(1f)
    }

    private fun enableControls() {
        binding?.btnSave?.isEnabled = true
        binding?.btnTagsRecognition?.isEnabled = true
        binding?.btnSetDefaultTags?.isEnabled = true
        binding?.flPosting?.animate()?.alpha(0f)?.withEndAction {
            binding?.flPosting?.gone()
        }
    }


    override fun onStart() {
        super.onStart()
        val behaviour = BottomSheetBehavior.from(requireView().parent as View)
        behaviour.peekHeight = Resources.getSystem().displayMetrics.heightPixels
    }

    override fun onDestroyView() {
        super.onDestroyView()
        galleryAdapter = null
        picturesAdapter = null
        genresAdapter = null
        colorsAdapter = null
        binding = null
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }


    companion object {
        const val TAG = "CreatePostFragment"
        const val GROUP_DETAIL = "GROUP_DETAIL"
        const val INTENT_IMAGE = "INTENT_IMAGE"
        const val ON_POST_CREATED = "ON_POST_CREATED"

        @JvmStatic
        fun newInstance(detail: GroupDetail, intentImageUri: Uri?) = CreatePostFragment().apply {
            arguments = Bundle().apply {
                putParcelable(GROUP_DETAIL, detail)
                putString(INTENT_IMAGE, intentImageUri?.toString())
            }
        }
    }
}