package com.codedevtech.mycardv2.listeners


interface ItemInteraction <K> {
    fun onItemClicked(item: K)

}