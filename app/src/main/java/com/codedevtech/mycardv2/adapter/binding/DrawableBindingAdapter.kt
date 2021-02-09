package com.codedevtech.mycardv2.adapter.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.models.SocialMediaProfile
import com.google.android.material.imageview.ShapeableImageView


    @BindingAdapter("socialMediaDrawableResource")
    fun setSocialMediaDrawableResource(imageView: ImageView, socialMedia: SocialMediaProfile.SocialMedia){

        when(socialMedia){
            SocialMediaProfile.SocialMedia.LinkedIn -> {
                Glide.with(imageView.context).load(R.drawable.linkedin___medium).into(imageView)
            }
            SocialMediaProfile.SocialMedia.Facebook -> {
                Glide.with(imageView.context).load(R.drawable.facebook___medium).into(imageView)
            }
            SocialMediaProfile.SocialMedia.Twitter -> {
                Glide.with(imageView.context).load(R.drawable.twitter___medium).into(imageView)
            }
            SocialMediaProfile.SocialMedia.Instagram -> {
                Glide.with(imageView.context).load(R.drawable.instagram___medium).into(imageView)
            }
            else -> Glide.with(imageView.context).load(R.drawable.cards).into(imageView)
        }
    }
