package com.tong.wanandroid.ui.home.child.item

import com.bumptech.glide.Glide
import com.tong.wanandroid.BaseViewHolder
import com.tong.wanandroid.common.services.model.BannerModel
import com.tong.wanandroid.common.services.model.Banners
import com.tong.wanandroid.databinding.ItemHomeBannerLayoutBinding
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder

class BannerViewHolder(private val binding: ItemHomeBannerLayoutBinding) : BaseViewHolder<Banners>(binding) {
    override fun bind(item: Banners) {
        val banner = binding.banner
        banner.apply {
            setAdapter(object : BannerImageAdapter<BannerModel>(item.banners) {
                    override fun onBindView(
                        holder: BannerImageHolder,
                        data: BannerModel,
                        position: Int,
                        size: Int
                    ) {
                        Glide.with(binding.banner)
                            .load(data.imagePath)
                            .into(holder.imageView)
                    }
                })
        }
        binding.executePendingBindings()
        TODO("Not yet implemented")
    }
}
