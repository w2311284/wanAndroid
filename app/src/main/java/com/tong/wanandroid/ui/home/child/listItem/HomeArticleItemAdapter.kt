package com.tong.wanandroid.ui.home.child.listItem

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.tong.wanandroid.common.services.model.ArticleModel

abstract class HomeArticleItemAdapter(val context: Context, var data: List<ArticleModel>) :
    RecyclerView.Adapter<HomeArticleItemAdapter.VH>() {
}