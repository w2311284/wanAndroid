package com.tong.wanandroid.ui.navigator.child.series

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.common.services.model.ClassifyModel
import com.tong.wanandroid.databinding.FragmentNavigatorChildBinding
import com.tong.wanandroid.databinding.FragmentSeriesBinding
import com.tong.wanandroid.ui.navigator.child.adapter.TagChildrenAdapter
import com.tong.wanandroid.ui.navigator.child.adapter.TagTitleAdapter
import com.tong.wanandroid.ui.navigator.child.navigator.NavigatorChildViewModel
import com.tong.wanandroid.ui.navigator.child.series.detail.SeriesDetailActivity
import com.tong.wanandroid.ui.web.WebActivity

class SeriesFragment : Fragment() {

    private var _binding: FragmentSeriesBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = SeriesFragment()
    }

    private lateinit var viewModel: SeriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[SeriesViewModel::class.java]
        _binding = FragmentSeriesBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView(){
        viewModel.seriesListLiveData.observe(viewLifecycleOwner){
            generateLeftGroup(it)
            generateChildList(it)
            binding.loadingContainer.loadingProgress.isVisible = false
        }
        viewModel.getSeriesList()
    }

    fun generateLeftGroup(tags: List<Any>){
        binding.leftTagList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TagTitleAdapter(tags, onClick = {onTagClick(it)})
        }
    }

    fun generateChildList(tags: List<Any>){
        binding.tagChildrenList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TagChildrenAdapter(tags, onTagChildrenClick = {onTagChildrenItemClick(it)})
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (recyclerView.scrollState != RecyclerView.SCROLL_STATE_SETTLING){
                        val pos = (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition()
                        pos?.let {
                            tagChangeSelected(it)
                        }
                    }
                }
            })
        }
    }

    private fun onTagClick(pos: Int) {

        val smoothScroller = object : LinearSmoothScroller(this.context) {
            override fun getVerticalSnapPreference(): Int = SNAP_TO_START
            override fun getHorizontalSnapPreference() = SNAP_TO_START
        }
        smoothScroller.targetPosition = pos
        val layoutManager = binding.tagChildrenList.layoutManager
        layoutManager?.startSmoothScroll(smoothScroller)
    }

    private fun tagChangeSelected(pos: Int){

    }

    private fun onTagChildrenItemClick(m : Any){
        if(m is ClassifyModel){
            startActivity(Intent(context,SeriesDetailActivity::class.java).apply {
                putExtra(SeriesDetailActivity.series_detail_id,m.id)
            })
        }
    }

}