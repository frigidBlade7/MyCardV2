package com.codedevtech.mycardv2.listeners

import android.view.View


interface ItemViewInteraction <K> {
    fun onItemClicked(item: K, view: View)

}