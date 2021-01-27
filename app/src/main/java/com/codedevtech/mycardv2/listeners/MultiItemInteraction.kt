package com.codedevtech.mycardv2.listeners


abstract class MultiItemInteraction <K> {

    abstract fun onItemClicked(item: K)
}