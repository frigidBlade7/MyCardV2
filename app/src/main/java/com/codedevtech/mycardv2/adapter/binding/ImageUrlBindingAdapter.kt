package com.codedevtech.mycardv2.adapter.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.codedevtech.mycardv2.R
import com.codedevtech.mycardv2.models.Resource
import com.codedevtech.mycardv2.models.User


@BindingAdapter("imageUrl")
    fun setImageUrl(imageView: ImageView, imageUrl: String?){
        if(!imageUrl.isNullOrEmpty())
            Glide.with(imageView.context).asBitmap().load(imageUrl)
                .placeholder(R.drawable.user_default).error(R.drawable
                    .user_default).diskCacheStrategy(DiskCacheStrategy.DATA).transform(CenterCrop(),CircleCrop()).thumbnail(0.1f).into(imageView)
        else
            Glide.with(imageView.context).clear(imageView)
    }

    @BindingAdapter("imageUrlResource")
    fun setImageUrlResource(imageView: ImageView, imageUrlResource: Resource<User?>){
        when(imageUrlResource){
            is Resource.Success->{
                if(imageUrlResource.data?.profileUrl!!.isEmpty())
                    Glide.with(imageView.context).asBitmap().load(imageUrlResource.data.profileUrl).transform(CenterCrop(),CircleCrop()).thumbnail(0.1f).into(imageView)

            }
        }
    }
