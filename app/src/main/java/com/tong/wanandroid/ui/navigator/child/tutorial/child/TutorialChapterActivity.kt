package com.tong.wanandroid.ui.navigator.child.tutorial.child

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.databinding.ActivitySeriesDetailBinding
import com.tong.wanandroid.databinding.ActivityTutorialChapterBinding
import com.tong.wanandroid.ui.home.child.adapter.ArticleAction
import com.tong.wanandroid.ui.home.child.adapter.HomeAdapter
import com.tong.wanandroid.ui.navigator.child.adapter.TutorialChapterAdapter
import com.tong.wanandroid.ui.navigator.child.series.detail.SeriesDetailActivity
import com.tong.wanandroid.ui.navigator.child.series.detail.SeriesDetailViewModel
import com.tong.wanandroid.ui.web.WebActivity

class TutorialChapterActivity : AppCompatActivity() {

    companion object {
        const val tutorial_chapter_id = "tutorial_chapter_id"
        const val tutorial_chapter_title = "tutorial_chapter_title"
    }

    private var _binding: ActivityTutorialChapterBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: TutorialChapterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this)[TutorialChapterViewModel::class.java]
        _binding = ActivityTutorialChapterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initView()
    }

    fun initView(){
        binding.toolbar.title = intent.getStringExtra(tutorial_chapter_title)
        viewModel.getChapterList(intent.getIntExtra(tutorial_chapter_id,-1))

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        viewModel.chapterListLiveData.observe(this){
            val recycleView = binding.chapterList
            recycleView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = TutorialChapterAdapter(it, onClick = { pos,article -> onItemClick(pos, article) })
            }
        }

    }

    private fun onItemClick(pos: Int,article: ArticleModel) {
        WebActivity.loadUrl(this,article.id,article.link,article.collect)
    }

}