package com.spaceandjonin.mycrd.listeners

import com.spaceandjonin.mycrd.models.LabelDetail


interface LabelledDetailItemInteraction {
    fun onRemoveLabelClicked(item: LabelDetail)
    fun onEditClicked(item: LabelDetail)
    fun onSwapClicked(item: LabelDetail)
}