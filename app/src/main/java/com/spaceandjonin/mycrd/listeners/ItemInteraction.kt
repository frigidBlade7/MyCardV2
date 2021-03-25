package com.spaceandjonin.mycrd.listeners


interface ItemInteraction <K> {
    fun onItemClicked(item: K)

}