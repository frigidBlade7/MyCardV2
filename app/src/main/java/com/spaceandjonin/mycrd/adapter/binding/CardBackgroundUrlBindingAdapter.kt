package com.spaceandjonin.mycrd.adapter.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.spaceandjonin.mycrd.R


@BindingAdapter("companyLogoUrl")
    fun setCompanyLogoUrl(imageView: ImageView, companyLogoUrl: String){
        if(companyLogoUrl.isNotEmpty())
            Glide.with(imageView.context).asBitmap()
            .load(companyLogoUrl).placeholder(R.drawable.splash_icon).transform(CenterCrop()).thumbnail(0.1f).into(imageView)
        else
            Glide.with(imageView.context).asBitmap().load(R.drawable.splash_icon).into(imageView)

    }
