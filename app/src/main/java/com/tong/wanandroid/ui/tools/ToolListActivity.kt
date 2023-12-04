package com.tong.wanandroid.ui.tools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tong.wanandroid.common.services.model.ToolModel
import com.tong.wanandroid.databinding.ActivityToolListBinding
import com.tong.wanandroid.ui.profile.adapter.ProfileAdapter
import com.tong.wanandroid.ui.tools.adapter.ToolListAdapter
import com.tong.wanandroid.ui.web.WebActivity

class ToolListActivity : AppCompatActivity() {
    private var _binding: ActivityToolListBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: ToolListViewModel

    private var toolAdapter = ToolListAdapter(emptyList(),this@ToolListActivity::onToolClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityToolListBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[ToolListViewModel::class.java]
        setContentView(binding.root)

        initView()
    }

    private fun initView(){
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.toolList.apply {
            adapter = toolAdapter
            setHasFixedSize(true)
        }

        viewModel.toolsLiveData.observe(this){
            toolAdapter.items = it
            toolAdapter.notifyDataSetChanged()
        }
    }

    private fun onToolClick(position: Int, data: ToolModel) {
        WebActivity.loadUrl(this, data.link)
    }
}