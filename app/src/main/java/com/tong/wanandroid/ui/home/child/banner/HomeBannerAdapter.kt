package com.tong.wanandroid.ui.home.child.banner

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.tong.wanandroid.common.services.model.BannerModel
import com.youth.banner.adapter.BannerAdapter

class HomeBannerAdapter(items: List<BannerModel>, private val onClick: () -> Unit) : BannerAdapter<BannerModel, HomeBannerAdapter.BannerViewHolder>(items) {
    class BannerViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(SimpleDraweeView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        })
    }

    override fun onBindView(
        holder: BannerViewHolder,
        data: BannerModel,
        position: Int,
        size: Int
    ) {
        (holder.view as? SimpleDraweeView)?.apply {
            setImageURI(data.imagePath)
            setOnClickListener { onClick(data, position) }
        }
    }

}