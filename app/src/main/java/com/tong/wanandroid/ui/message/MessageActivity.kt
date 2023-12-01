package com.tong.wanandroid.ui.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tong.wanandroid.R
import com.tong.wanandroid.databinding.ActivityMessageBinding
import com.tong.wanandroid.databinding.ActivityMyCoinInfoBinding
import com.tong.wanandroid.ui.coin.MyCoinInfoViewModel

class MessageActivity : AppCompatActivity() {
    private var _binding: ActivityMessageBinding? = null
    private val binding get() = _binding!!

    private lateinit var childAdapter: MessageViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityMessageBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initView()
    }

    private fun initView(){
        childAdapter =
            MessageViewPagerAdapter(generateMessageTabs(), supportFragmentManager, lifecycle)
        binding.backIcon.setOnClickListener { finish() }

        binding.messageViewPager.apply {
            adapter = childAdapter
        }
        TabLayoutMediator(
            binding.messageTabLayout,binding.messageViewPager
        ){ tab: TabLayout.Tab, position: Int ->
            tab.text = childAdapter.items[position]
        }.apply(TabLayoutMediator::attach)
    }

    private fun generateMessageTabs() = listOf(
        MessageViewPagerAdapter.MESSAGE_TAB_NEW,
        MessageViewPagerAdapter.MESSAGE_TAB_HISTORY,
    )
}