package com.codedevtech.mycardv2.adapter.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.models.SocialMediaProfile
import com.google.android.material.imageview.ShapeableImageView


    @BindingAdapter("imageUrl")
    fun setImageUrl(imageView: ImageView, imageUrl: String?){
        if(!imageUrl.isNullOrEmpty())
            Glide.with(imageView.context).asBitmap().load(imageUrl).transform(CenterCrop(),CircleCrop()).thumbnail(0.1f).into(imageView)
    }
