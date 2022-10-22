package com.gfs.helper.elements.model

import android.graphics.drawable.Drawable

data class MultiImageConfigModel(
        val backgroundColor: Int? = null,
        val backgroundDrawable: Drawable? = null,
        // 是否显示顶部计数文字
        val isShowCountText: Boolean = true,
        // 是否显示关闭按钮
        val isShowCloseIcon: Boolean = true,
        // 加载失败时的图片资源id
        val loadErrorImageResId: Int? = null,
        val loadErrorImageDrawable: Drawable? = null,
        // 加载中的图片资源id
        val placeholderImageResId: Int? = null,
        val placeholderImageDrawable: Drawable? = null,
        // 图片宽高
        val imageWidth: Int? = null,
        val imageHeight: Int? = null
)
