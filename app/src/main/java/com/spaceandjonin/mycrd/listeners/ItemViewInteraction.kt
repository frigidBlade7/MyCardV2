package com.spaceandjonin.mycrd.listeners

import android.view.View


interface ItemViewInteraction <K> {
    fun onItemClicked(item: K, view: View, position: Int)

}