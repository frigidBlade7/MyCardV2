package com.spaceandjonin.mycrd.adapter.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.models.SocialMediaProfile


@BindingAdapter("socialMediaDrawableResource")
fun setSocialMediaDrawableResource(
    imageView: ImageView,
    socialMedia: SocialMediaProfile.SocialMedia
) {

    when (socialMedia) {
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
