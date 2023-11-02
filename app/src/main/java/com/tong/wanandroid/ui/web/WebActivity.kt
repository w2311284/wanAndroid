package com.tong.wanandroid.ui.web

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import com.tong.wanandroid.R

class WebActivity : AppCompatActivity() {

    companion object {
        fun loadUrl(context: Context, url: String) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
    }
}