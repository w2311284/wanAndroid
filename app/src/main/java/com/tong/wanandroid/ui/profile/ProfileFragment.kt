package com.tong.wanandroid.ui.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.ProfileItemModel
import com.tong.wanandroid.common.datastore.DataStoreManager
import com.tong.wanandroid.common.services.model.UserBaseModel
import com.tong.wanandroid.databinding.FragmentProfileBinding
import com.tong.wanandroid.ui.coin.MyCoinInfoActivity
import com.tong.wanandroid.ui.collect.CollectActivity
import com.tong.wanandroid.ui.login.LoginActivity
import com.tong.wanandroid.ui.message.MessageActivity
import com.tong.wanandroid.ui.profile.adapter.ProfileAdapter
import com.tong.wanandroid.ui.share.ShareActivity
import com.tong.wanandroid.ui.tools.ToolListActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.math.abs

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var profileAdapter : ProfileAdapter =  ProfileAdapter(emptyList(),this@ProfileFragment::onItemClick)

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

        viewModel.userInfoLiveData.observe(viewLifecycleOwner){
            binding.user = it
            viewModel.userId = it.userInfo.id
            lifecycleScope.launch {
                DataStoreManager.getInstance(requireContext()).cacheUserBaseInfo(it)
            }
        }

        binding.apply {
            profileItemList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = profileAdapter
            }

            appBarLayout.addOnOffsetChangedListener{ appBarLayout, verticalOffset ->
                when {
                    verticalOffset == 0 -> viewModel.collapsingToolBarStateFlow.value =
                        CollapsingToolBarState.EXPANDED
                    abs(verticalOffset) >= appBarLayout.totalScrollRange -> viewModel.collapsingToolBarStateFlow.value =
                        CollapsingToolBarState.COLLAPSED
                    else -> viewModel.collapsingToolBarStateFlow.value =
                        CollapsingToolBarState.INTERMEDIATE
                }
            }

            arrayOf(userAvatar, userName, userId, userCoinCount).forEach {
                it.setOnClickListener {
                    checkLogin {}
                }
            }
            userCoinCount.setOnClickListener {
                checkLogin {
                    requireContext().startActivity(Intent(context, MyCoinInfoActivity::class.java))
                }
            }
        }

        lifecycleScope.launch {
            viewModel.collapsingToolBarStateFlow.distinctUntilChanged { old, new ->
                old == new
            }.collectLatest {
                if (it == CollapsingToolBarState.COLLAPSED) {
                    binding.collapsingToolbarLayout.title =
                        binding.user?.userInfo?.nickname
                } else binding.collapsingToolbarLayout.title = ""
            }
        }

        DataStoreManager.getInstance(requireContext()).isLogIn.onEach {
            if (it){
                viewModel.getUserInfo()
                viewModel.isLogin = true
            }else{
                viewModel.isLogin = false
                binding.user = UserBaseModel()
            }
        }.launchIn(lifecycleScope)

        lifecycleScope.launch {
            val isLogin = viewModel.getLoginCache(requireContext())
            DataStoreManager.getInstance(requireContext()).setLoggedIn(isLogin)
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
            ProfileItemModel(R.drawable.ic_tool_48dp, getString(R.string.profile_item_title_tools)),
            ProfileItemModel(R.drawable.ic_settings_suggest_48dp, getString(R.string.profile_item_title_quit))
        )
        profileAdapter.notifyItemRangeChanged(0, profileAdapter.itemCount)
    }

    private fun onItemClick(position: Int, item: ProfileItemModel) {
        when (item.title) {
            getString(R.string.profile_item_title_message) -> {
                checkLogin {
                    requireContext().startActivity(Intent(context, MessageActivity::class.java))
                }
            }
            getString(R.string.profile_item_title_share) -> {
                checkLogin {
                    ShareActivity.load(requireContext(),viewModel.userId)
                }
            }
            getString(R.string.profile_item_title_favorite) -> {
                checkLogin {
                    requireContext().startActivity(Intent(context, CollectActivity::class.java))
                }
            }
            getString(R.string.profile_item_title_tools) -> {
                checkLogin {
                    requireContext().startActivity(Intent(context, ToolListActivity::class.java))
                }
            }
            getString(R.string.profile_item_title_quit) -> {
                checkLogin {
                    logout()
                }
            }
        }
    }

    private fun checkLogin(action : () -> Unit){
        if (viewModel.isLogin) {
            action()
        } else {
            val context = requireContext()
            Toast.makeText(context, R.string.account_need_login, Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    private fun logout(){
        MaterialAlertDialogBuilder(requireContext())
            .setCancelable(true)
            .setTitle("确认退出登录？")
            .setPositiveButton("确认") { dialogInterface, _ ->
                dialogInterface.dismiss()
                viewModel.logout(requireContext())
            }
            .setNegativeButton("取消") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }


}