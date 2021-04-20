package com.spaceandjonin.mycrd.fragments.scan

internal class CustomCropPresenter : CustomCropContract.Presenter {

    private var view: CustomCropContract.View? = null
    private var counter = 0

    override fun bindView(view: CustomCropContract.View) {
        this.view = view
        //this.view?.updateRotationCounter(counter.toString())
    }

    override fun unbindView() {
        view = null
    }

    override fun onRotateClick() {
        counter += 90
        view?.rotate(90)
        if (counter == 360) counter = 0
        //view?.updateRotationCounter(counter.toString())
    }
}