package com.spaceandjonin.mycrd.viewholders

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.spaceandjonin.mycrd.BR

class BaseViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    private var binding: ViewDataBinding? = binding


    fun bindTo(item: Any?) {
        binding!!.setVariable(BR.item, item)
        binding!!.executePendingBindings()
    }


    fun bindTo(item: Any?,position: Int) {
        binding!!.setVariable(BR.item, item)
        binding!!.setVariable(BR.position, position)
        binding!!.executePendingBindings()
    }

}