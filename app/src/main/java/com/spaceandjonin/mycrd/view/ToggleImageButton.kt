package com.spaceandjonin.mycrd.view

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.Checkable
import androidx.appcompat.widget.AppCompatImageButton
import androidx.customview.view.AbsSavedState
import java.util.*

class ToggleImageButton : AppCompatImageButton, Checkable {
    private var checked = false
    private var broadcasting = false
    private val onCheckedChangeListeners = LinkedHashSet<OnCheckedChangeListener>()
    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.checked = checked
        return savedState
    }

    fun addOnCheckChangedListener() {

    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        val savedState = state
        super.onRestoreInstanceState(savedState.superState)
        isChecked = savedState.checked
    }

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun setChecked(checked: Boolean) {
        if (isEnabled && this.checked != checked) {
            this.checked = checked
            refreshDrawableState()

            // Avoid infinite recursions if setChecked() is called from a listener
            if (broadcasting) {
                return
            }
            broadcasting = true
            for (listener in onCheckedChangeListeners) {
                listener.onCheckedChanged(this, this.checked)
            }
            broadcasting = false
        }
    }

    override fun isChecked(): Boolean {
        return checked
    }

    override fun toggle() {
        isChecked = !checked
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(button: ToggleImageButton?, isChecked: Boolean)
    }

    internal class SavedState : AbsSavedState {
        var checked = false

        constructor(superState: Parcelable?) : super(superState!!) {}
        constructor(source: Parcel, loader: ClassLoader?) : super(source, loader) {
            var loader = loader
            if (loader == null) {
                loader = javaClass.classLoader
            }
            readFromParcel(source)
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(if (checked) 1 else 0)
        }

        private fun readFromParcel(`in`: Parcel) {
            checked = `in`.readInt() == 1
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> =
                object : Parcelable.ClassLoaderCreator<SavedState> {
                    override fun createFromParcel(`in`: Parcel, loader: ClassLoader): SavedState {
                        return SavedState(`in`, loader)
                    }

                    override fun createFromParcel(`in`: Parcel): SavedState {
                        return SavedState(`in`, null)
                    }

                    override fun newArray(size: Int): Array<SavedState?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }

    override fun performClick(): Boolean {
        toggle()
        return super.performClick()
    }
}