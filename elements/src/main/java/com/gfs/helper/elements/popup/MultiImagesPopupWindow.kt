package com.gfs.helper.elements.popup

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.gfs.helper.elements.ElementsLogUtil
import com.gfs.helper.elements.model.MultiImageConfigModel
import com.gfs.helper.elements.R
import com.gfs.helper.elements.adapter.MultiImageAdapter
import java.lang.Exception

/**
 * 多张图片列表的放大弹窗，可左右滑动
 */
class MultiImagesPopupWindow(
    mContext: Context
) : PopupWindow(mContext) {

    companion object {
        private const val TAG = "MultiImagesPopupWindow"
    }

    private var mTvCount: TextView? = null
    private var mLlClose: LinearLayout? = null
    private var mRvImages: RecyclerView? = null
    private var mImageUrlList = mutableListOf<Any>()
    private var mAdapter: MultiImageAdapter? = null
    // 上次所处的列表位置
    private var mLastImagePosition = 0
    private var mConfigModel: MultiImageConfigModel? = null

    // 当前图片列表的长度
    val currentImageListSize: Int get() = mImageUrlList.size
    // 当前的配置类
    val currentConfigModel: MultiImageConfigModel? get() = mConfigModel

    init {
        val view = View.inflate(mContext, R.layout.element_popup_multi_images, null)
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.MATCH_PARENT

        isFocusable = false
        isTouchable = true

        initView(view, mContext)

        contentView = view
    }

    private fun initView(view: View?, mContext: Context) {
        try {
            view!!.apply {
                mTvCount = findViewById(R.id.elements_tv_image_count)
                mLlClose = findViewById(R.id.elements_ll_close)
                mRvImages = findViewById(R.id.elements_rv_images)

                mRvImages!!.let { rvImages ->
                    val pagerSnapHelper = PagerSnapHelper()
                    pagerSnapHelper.attachToRecyclerView(rvImages)

                    mAdapter = MultiImageAdapter(mImageUrlList)
                    rvImages.adapter = mAdapter
                    rvImages.layoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)

                    rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            super.onScrollStateChanged(recyclerView, newState)
                            val linearLayoutManager = rvImages.layoutManager as LinearLayoutManager
                            val firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition()

                            if (firstVisiblePosition < mImageUrlList.size) {
                                updateImagesCountText(firstVisiblePosition)
                            }
                        }
                    })
                }

                mLlClose!!.setOnClickListener {
                    dismiss()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置一些配置数据
     */
    fun setConfig(configModel: MultiImageConfigModel?) {
        configModel?.apply {
            mLlClose?.visibility = if (!isShowCloseIcon) {
                View.GONE
            } else {
                View.VISIBLE
            }

            mTvCount?.visibility = if (!isShowCountText) {
                View.GONE
            } else {
                View.VISIBLE
            }

            mAdapter?.setSize(imageWidth, imageHeight)

            // 设置背景颜色
            when {
                backgroundDrawable != null -> {
                    setBackgroundDrawable(backgroundDrawable)
                }
                backgroundColor != null -> {
                    setBackgroundDrawable(ColorDrawable(backgroundColor))
                }
                else -> {
                    setBackgroundDrawable(ColorDrawable(Color.parseColor("#CC000000")))
                }
            }
            mConfigModel = configModel
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateImagesCountText(currentPosition: Int) {
        mTvCount?.text = "${currentPosition + 1}/${mImageUrlList.size}"
        mLastImagePosition = currentPosition
    }

    /**
     * 设置弹窗消失时的监听器
     */
    fun setOnDialogDismissListener(onDismissListener: OnDismissListener) {
        setOnDismissListener(onDismissListener)
    }

    /**
     * 弹出弹窗，不设置列表数据，使用上次设置的数据
     *
     * ⚠️：调用此方法，需保证已经设置过数据，调用才会成功
     */
    fun showPopupWindowNoSetData(view: View?, scrollPosition: Int? = null) {
        showPopupWindowAndSetData(view, mImageUrlList, scrollPosition, true)
    }

    /**
     * 弹出弹窗并设置列表数据，首次弹出需调用此方法！
     *
     * [imageUrlList] 图片地址列表，可为：本地路径、网络url
     * [scrollPosition] 滑动到列表的哪个位置
     * [isUseOriginalData] ⚠️ 此字段仅内部使用；是否使用原始数据
     */
    @JvmOverloads
    fun showPopupWindowAndSetData(
            view: View?,
            imageUrlList: List<Any?>?,
            scrollPosition: Int? = null,
            isUseOriginalData: Boolean = false
    ) {
        if (view == null) {
            ElementsLogUtil.logE("showPopupWindow.error: view is null!")
            return
        }
        if (imageUrlList.isNullOrEmpty()) {
            ElementsLogUtil.logE("showPopupWindow.error: list is isNullOrEmpty!")
            return
        }
        // 如果不使用老数据或数据完全相同，则不更新列表内容
        if (!isUseOriginalData || !mImageUrlList.containsAll(imageUrlList)) {
            updateAllData(imageUrlList)
        }
        if (scrollPosition != null) {
            if (scrollPosition >= 0 && scrollPosition < mImageUrlList.size) {
                mRvImages?.scrollToPosition(scrollPosition)
                updateImagesCountText(scrollPosition)
            } else {
                updateImagesCountText(mLastImagePosition)
                Log.e(TAG, "showPopupWindow scrollToPosition.error: scrollPosition is index of out, scrollPosition: $scrollPosition, List.size: ${mImageUrlList.size}")
            }
        } else {
            updateImagesCountText(mLastImagePosition)
        }


        showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    /**
     * 更新数据内容
     *
     * 如果你想实现静默更新，可以调用此方法
     */
    fun updateAllData(imageUrlList: List<Any?>) {
        if (mImageUrlList.isNotEmpty()) {
            mImageUrlList.clear()
        }
        for (any in imageUrlList) {
            if (any != null) {
                mImageUrlList.add(any)
            }
        }
        mAdapter?.notifyDataSetChanged()
    }

    /**
     * 插入数据
     *
     * [position] 默认为空，不传的话默认插入到最后一位
     */
    fun insertData(data: Any?, position: Int? = null) {
        if (data == null) {
            ElementsLogUtil.logE("insertData.error: data is null!")
            return
        }

        val usePosition = if (position.isIndexSafe(mImageUrlList)) {
            mImageUrlList.add(position!!, data)
            position
        } else {
            mImageUrlList.add(data)
            mImageUrlList.size - 1
        }
        mAdapter?.notifyItemInserted(usePosition)
        // 如果插入的位置小于当前的位置，那么当前的位置就会向后一位，即 +1
        if (usePosition <= mLastImagePosition) {
            mLastImagePosition += 1
        }
    }

    /**
     * 根据位置删除指定数据
     */
    fun deleteData(position: Int?) {
        if (!position.isIndexSafe(mImageUrlList)) {
            ElementsLogUtil.logE("deleteDataByPosition.error: position is not safety, position: $position, list: ${mImageUrlList.size}")
            return
        }

        updateLastImagePositionBeforeRemove(position!!)

        mImageUrlList.removeAt(position)
        mAdapter?.notifyItemRemoved(position)
    }

    /**
     * 根据数据删除其自身
     */
    fun deleteData(data: Any?) : Boolean {
        if (data == null) {
            ElementsLogUtil.logE("deleteData.error: data is null!")
            return false
        }
        val preDeleteIndex = mImageUrlList.indexOf(data)
        if (preDeleteIndex != -1) {
            updateLastImagePositionBeforeRemove(preDeleteIndex)
            val result = mImageUrlList.remove(data)
            if (result) {
                mAdapter?.notifyItemRemoved(preDeleteIndex)
            }
            return result
        }

        return false
    }

    fun clearData() {
        mImageUrlList.clear()
        mAdapter?.notifyDataSetChanged()
    }

    private fun Int?.isIndexSafe(list: List<Any?>?) : Boolean {
        if (list.isNullOrEmpty()) {
            return false
        }
        if (this == null || this < 0 || this >= list.size) {
            return false
        }
        return true
    }

    /**
     * 在执行删除操作前，更新当前下标
     */
    private fun updateLastImagePositionBeforeRemove(preRemovePosition: Int) {
        if (mConfigModel?.isShowCountText == false) {
            return
        }
        if (preRemovePosition < mLastImagePosition) {
            mLastImagePosition -= 1
        } else if (preRemovePosition == mLastImagePosition) {
            // 如果当前下标在列表的末尾，我们需要向前移动
            if (mLastImagePosition == mImageUrlList.size - 1) {
                // 如果 -1 < 0，代表删除时列表仅有一个元素，无需再更新下标位置
                if (mLastImagePosition - 1 >= 0) {
                    mLastImagePosition -= 1
                }
            }
        }
    }

}