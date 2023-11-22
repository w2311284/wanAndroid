package com.tong.wanandroid.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.ProfileItemModel
import com.tong.wanandroid.databinding.FragmentProfileBinding
import com.tong.wanandroid.ui.navigator.child.adapter.TagChildrenAdapter

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    private lateinit var profileAdapter : ProfileAdapter

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        initView()
        initItems()

        return binding.root
    }

    private fun initView(){
        profileAdapter = ProfileAdapter(emptyList(), onClick = { pos,item -> onItemClick(pos,item)})
        binding.profileItemList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = profileAdapter
        }
    }

    private fun initItems() {
        profileAdapter.items = listOf(
            ProfileItemModel(
                R.drawable.ic_notification_48dp,
                getString(R.string.profile_item_title_message)
            ),
            ProfileItemModel(R.drawable.ic_share_48dp, getString(R.string.profile_item_title_share)),
            ProfileItemModel(R.drawable.ic_collect, getString(R.string.profile_item_title_favorite)),
            ProfileItemModel(R.drawable.ic_tool_48dp, getString(R.string.profile_item_title_tools))
        )
        profileAdapter.notifyItemRangeChanged(0, profileAdapter.itemCount)
    }

    private fun onItemClick(position: Int, item: ProfileItemModel) {

    }


}