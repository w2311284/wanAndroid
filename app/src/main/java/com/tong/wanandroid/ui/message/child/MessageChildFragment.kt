package com.tong.wanandroid.ui.message.child

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.MessageModel
import com.tong.wanandroid.databinding.ActivityMessageBinding
import com.tong.wanandroid.databinding.FragmentMessageChildBinding
import com.tong.wanandroid.databinding.FragmentNavigatorChildBinding
import com.tong.wanandroid.ui.message.MessageViewModel
import com.tong.wanandroid.ui.message.MessageViewPagerAdapter
import com.tong.wanandroid.ui.message.adapter.MessageChildAdapter
import com.tong.wanandroid.ui.navigator.child.navigator.NavigatorChildViewModel
import com.tong.wanandroid.ui.project.child.ProjectAdapter
import com.tong.wanandroid.ui.web.WebActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MessageChildFragment : Fragment() {

    private var _binding: FragmentMessageChildBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MessageChildViewModel

    private val messageChildAdapter by lazy { MessageChildAdapter(this@MessageChildFragment::onItemClick) }

    private val tabTitle by lazy { arguments?.getString("key_message_child_tab_title") ?: MessageViewPagerAdapter.MESSAGE_TAB_NEW }

    companion object {
        fun newInstance(tab: String) = MessageChildFragment().apply {
            arguments = bundleOf("key_message_child_tab_title" to tab)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[MessageChildViewModel::class.java]
        _binding = FragmentMessageChildBinding.inflate(inflater, container, false)
        initView()

        return binding.root
    }

    private fun initView(){
        binding.messageList.apply {
            adapter = messageChildAdapter
        }

        lifecycleScope.launch {
            messageChildAdapter.loadStateFlow.collectLatest(this@MessageChildFragment::updateLoadStates)
        }

        lifecycleScope.launch {
            if (tabTitle == MessageViewPagerAdapter.MESSAGE_TAB_NEW) {
                viewModel.getUnreadMsgFlow.onStart {
//                    viewModel.clearUnreadMessage()
                }
            } else {
                viewModel.getReadiedMsgFlow
            }.collectLatest(messageChildAdapter::submitData)
        }
    }

    private fun onItemClick(position: Int, m: MessageModel) {
        WebActivity.loadUrl(requireContext(), m.fullLink)
    }

    private fun updateLoadStates(loadStates: CombinedLoadStates) {
        binding.loadingContainer.apply {
            emptyLayout.isVisible = loadStates.refresh is LoadState.NotLoading && messageChildAdapter.isEmpty()
            loadingProgress.isVisible = messageChildAdapter.itemCount == 0 && loadStates.source.refresh is LoadState.Loading
        }
    }

}