package com.gfs.helper.elements.adapter

import android.net.Uri
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gfs.helper.elements.ElementsLogUtil
import com.gfs.helper.elements.R
import com.gfs.helper.elements.model.MultiImageConfigModel
import com.github.chrisbanes.photoview.PhotoView

/**
 * 图片列表适配器
 */
internal class MultiImageAdapter(list: MutableList<Any>, private val configModel: MultiImageConfigModel? = null) :
    BaseQuickAdapter<Any, BaseViewHolder>(R.layout.element_item_image, list) {

    private var mLayoutParams: LinearLayout.LayoutParams? = null

    override fun convert(viewHolder: BaseViewHolder, url: Any?) {
        if (url == null) {
            ElementsLogUtil.logE("url is null")
            return
        }
        viewHolder.addOnClickListener(R.id.element_photo_view)
        val photoView = viewHolder.getView<PhotoView>(R.id.element_photo_view)
        if (mLayoutParams != null && photoView.layoutParams != mLayoutParams) {
            photoView.layoutParams = mLayoutParams
        }
        var load = Glide.with(mContext).load(url)
        if (configModel != null) {
            if (configModel.loadErrorImageResId != null) {
                load = load.error(configModel.loadErrorImageResId)
            } else if (configModel.loadErrorImageDrawable != null) {
                load = load.error(configModel.loadErrorImageDrawable)
            }
            if (configModel.placeholderImageResId != null) {
                load = load.placeholder(configModel.placeholderImageResId)
            } else if (configModel.placeholderImageDrawable != null) {
                load = load.placeholder(configModel.placeholderImageDrawable)
            }
        }

        load.into(photoView)
    }

    fun setSize(width: Int?, height: Int?) {
        if (width != null || height != null) {
            mLayoutParams = LinearLayout.LayoutParams(width ?: LinearLayout.LayoutParams.MATCH_PARENT,
                height ?: LinearLayout.LayoutParams.WRAP_CONTENT)

        }
    }
}