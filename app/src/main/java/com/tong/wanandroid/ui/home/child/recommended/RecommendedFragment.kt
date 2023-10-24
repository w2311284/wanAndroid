package com.tong.wanandroid.ui.home.child.recommended

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.tong.wanandroid.common.services.model.BannerModel
import com.tong.wanandroid.databinding.FragmentRecommendedBinding
import com.tong.wanandroid.ui.home.child.item.HomeAdapter
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder

class RecommendedFragment : Fragment() {

    private var _binding: FragmentRecommendedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = RecommendedFragment()
    }

    private lateinit var viewModel: RecommendedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(RecommendedViewModel::class.java)
        _binding = FragmentRecommendedBinding.inflate(inflater, container, false)

        initRefreshLayout()
        initRecycleView()
        initBanner()
        return binding.root
    }

    fun initRefreshLayout(){
        val swipeRefreshLayout = binding.recommendedRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    fun initRecycleView(){
        val recycleView = binding.recommendedList
        val adapter = HomeAdapter()
//        viewModel
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