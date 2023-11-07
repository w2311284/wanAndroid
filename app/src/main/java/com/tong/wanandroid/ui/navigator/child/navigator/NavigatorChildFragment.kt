package com.tong.wanandroid.ui.navigator.child.navigator

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.common.services.model.NavigationModel
import com.tong.wanandroid.databinding.FragmentNavigatorChildBinding
import com.tong.wanandroid.ui.navigator.child.adapter.TagChildrenAdapter
import com.tong.wanandroid.ui.navigator.child.adapter.TagTitleAdapter
import com.tong.wanandroid.ui.web.WebActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NavigatorChildFragment : Fragment() {

    private var _binding: FragmentNavigatorChildBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = NavigatorChildFragment()
    }

    private lateinit var viewModel: NavigatorChildViewModel

    private lateinit var tagChildrenAdapter: TagChildrenAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[NavigatorChildViewModel::class.java]
        _binding = FragmentNavigatorChildBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    fun initView(){
        viewModel.navigationTagListLiveData.observe(viewLifecycleOwner){
            generateLeftGroup(it)
            generateChildList(it)
            binding.loadingContainer.loadingProgress.isVisible = false
        }
        viewModel.getNavigationList()
    }

    fun generateChildList(tags: List<Any>){
        tagChildrenAdapter = TagChildrenAdapter(tags, onTagChildrenClick = {onTagChildrenItemClick(it)})
        binding.tagChildrenList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tagChildrenAdapter
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

    fun generateLeftGroup(tags: List<Any>){
        binding.leftTagList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TagTitleAdapter(tags, onClick = {onTagClick(it)})
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
        if(m is ArticleModel){
            context?.let { WebActivity.loadUrl(it,m.id,m.link,m.collect) }
        }
    }

}