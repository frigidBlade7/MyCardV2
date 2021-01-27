package com.codedevtech.mycardv2

import android.content.ComponentCallbacks2
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.scanlibrary.IScanner

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}