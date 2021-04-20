package com.spaceandjonin.mycrd.fragments.scan

interface CustomCropContract {
    interface View {
        //fun updateRotationCounter(counter: String)
        fun rotate(counter: Int)
    }

    interface Presenter {
        fun bindView(view: View)
        fun unbindView()
        fun onRotateClick()
    }
}