package com.spaceandjonin.mycrd.adapter.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.spaceandjonin.mycrd.R
import com.spaceandjonin.mycrd.models.LabelDetail
import com.spaceandjonin.mycrd.models.SocialMediaProfile


@BindingAdapter("labelledDrawableResource")
    fun setLabelledDrawableResource(imageView: ImageView, item: LabelDetail){

        when(item.label){
            SocialMediaProfile.SocialMedia.LinkedIn.name -> {
                Glide.with(imageView.context).load(R.drawable.linkedin___medium).into(imageView)
            }
            SocialMediaProfile.SocialMedia.Facebook.name -> {
                Glide.with(imageView.context).load(R.drawable.facebook___medium).into(imageView)
            }
            SocialMediaProfile.SocialMedia.Twitter.name -> {
                Glide.with(imageView.context).load(R.drawable.twitter___medium).into(imageView)
            }
            SocialMediaProfile.SocialMedia.Instagram.name -> {
                Glide.with(imageView.context).load(R.drawable.instagram___medium).into(imageView)
            }
            imageView.context.getString(R.string.full_name) -> {
                Glide.with(imageView.context).load(R.drawable.name_label).into(imageView)
            }
            imageView.context.getString(R.string.company_name) -> {
                Glide.with(imageView.context).load(R.drawable.company_name_label).into(imageView)
            }
            imageView.context.getString(R.string.role) -> {
                Glide.with(imageView.context).load(R.drawable.role_label).into(imageView)
            }
            imageView.context.getString(R.string.work_location) -> {
                Glide.with(imageView.context).load(R.drawable.location_label).into(imageView)
            }
            imageView.context.getString(R.string.website) -> {
                Glide.with(imageView.context).load(R.drawable.website_label).into(imageView)
            }
            imageView.context.getString(R.string.note) -> {
                Glide.with(imageView.context).load(R.drawable.role_label).into(imageView)
            }
            //else -> Glide.with(imageView.context).load(R.drawable.cards).into(imageView)
        }

        when(item.parentLabel) {
            imageView.context.getString(R.string.phone_number) -> {
                Glide.with(imageView.context).load(R.drawable.phone_label).into(imageView)
            }
            imageView.context.getString(R.string.email_address) -> {
                Glide.with(imageView.context).load(R.drawable.mail_label).into(imageView)
            }
        }
    }
