package com.spaceandjonin.mycrd.listeners


interface UnlabelledDetailItemInteraction {
    fun onTagClicked(item: String)
    fun onCopyClicked(item: String)
    fun onRemoveClicked(item: String)
    fun onItemClicked(item: String)
}