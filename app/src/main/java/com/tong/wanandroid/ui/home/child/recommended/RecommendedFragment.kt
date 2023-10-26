package com.tong.wanandroid.ui.home.child.recommended

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.tong.wanandroid.common.services.model.BannerModel
import com.tong.wanandroid.databinding.FragmentRecommendedBinding
import com.tong.wanandroid.ui.home.child.item.HomeAdapter
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecommendedFragment : Fragment() {

    private var _binding: FragmentRecommendedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = RecommendedFragment()
    }

    private lateinit var viewModel: RecommendedViewModel

    val homeAdapter = HomeAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(RecommendedViewModel::class.java)
        _binding = FragmentRecommendedBinding.inflate(inflater, container, false)

        initRefreshLayout()
        initRecycleView()
        return binding.root
    }

    fun initRefreshLayout(){
        val swipeRefreshLayout = binding.recommendedRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            homeAdapter.refresh()
        }
    }

    fun initRecycleView(){
        val recycleView = binding.recommendedList
        recycleView.adapter = homeAdapter
        lifecycleScope.launch {
            viewModel.getArticlesFlow.collectLatest {
                homeAdapter.submitData(it)
            }
        }
    }

    fun initBanner(){
//        val banner = binding.banner
//        viewModel.bannerResponse.observe(viewLifecycleOwner) { response ->
//            banner.apply {
//                setAdapter(object : BannerImageAdapter<BannerModel>(response) {
//                    override fun onBindView(
//                        holder: BannerImageHolder,
//                        data: BannerModel,
//                        position: Int,
//                        size: Int
//                    ) {
//                        Glide.with(this@RecommendedFragment)
//                            .load(data.imagePath)
//                            .into(holder.imageView)
//                    }
//                })
//            }
//        }
//        viewModel.getBanner()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}