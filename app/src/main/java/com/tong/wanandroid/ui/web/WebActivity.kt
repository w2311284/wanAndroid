package com.tong.wanandroid.ui.web
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.tong.wanandroid.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {

    private var _binding: ActivityWebBinding? = null

    private val binding get() = _binding!!

    companion object {
        private const val extra_url = "url"
        private const val extra_id = "id"
        private const val extra_collect = "collect"

        fun loadUrl(context: Context, id: Int, url: String, isCollect: Boolean) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(extra_id, id)
            intent.putExtra(extra_url, url)
            intent.putExtra(extra_collect, isCollect)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val url = intent.getStringExtra(extra_url)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.web.apply {
            webChromeClient = object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    binding.webTitle.text = title
                }
            }
            webViewClient = WebViewClient()
            if (url != null) {
                loadUrl(url)
            }
        }
    }
}