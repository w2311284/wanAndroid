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
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.ProfileItemModel
import com.tong.wanandroid.common.datastore.DataStoreManager
import com.tong.wanandroid.common.services.model.UserBaseModel
import com.tong.wanandroid.databinding.FragmentProfileBinding
import com.tong.wanandroid.ui.coin.MyCoinInfoActivity
import com.tong.wanandroid.ui.login.LoginActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.math.abs

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileAdapter : ProfileAdapter

//    private lateinit var dataStoreManager: DataStoreManager

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
//        dataStoreManager =  DataStoreManager.getInstance(requireContext())
        initView()
        initItems()

        return binding.root
    }

    private fun initView(){
        profileAdapter = ProfileAdapter(emptyList(), onClick = { pos,item -> onItemClick(pos,item)})
        binding.user = UserBaseModel()
        viewModel.userInfoLiveData.observe(viewLifecycleOwner){
            binding.user = it
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
                viewModel.isLogin = true
                viewModel.getUserInfo()
            }else{
                viewModel.isLogin = false
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
            ProfileItemModel(R.drawable.ic_tool_48dp, getString(R.string.profile_item_title_tools))
        )
        profileAdapter.notifyItemRangeChanged(0, profileAdapter.itemCount)
    }

    private fun onItemClick(position: Int, item: ProfileItemModel) {
        when (item.title) {
            getString(R.string.profile_item_title_message) -> {
                checkLogin {

                }
//                check {
//                    startActivity(Intent(requireContext(), MessageActivity::class.java))
//                }
            }
            getString(R.string.profile_item_title_share) -> {
                checkLogin {

                }
//                viewModel.accountState.value.checkLogin(requireContext()) {
//                    startActivity(
//                        intentTo(
//                            Activities.ShareList(
//                                bundle = bundleOf(Activities.ShareList.KEY_SHARE_LIST_USER_ID to viewModel.userId)
//                            )
//                        )
//                    )
//                }
            }
            getString(R.string.profile_item_title_favorite) -> {
                checkLogin {

                }
//                viewModel.accountState.value.checkLogin(requireContext()) {
//                    startActivity(Intent(requireContext(), CollectActivity::class.java))
//                }
            }
            getString(R.string.profile_item_title_tools) -> {
                checkLogin {

                }
//                startActivity(Intent(requireContext(), ToolListActivity::class.java))
            }
        }
    }

    fun checkLogin(action : () -> Unit){
        if (viewModel.isLogin) {
            action()
        } else {
            val context = requireContext()
            Toast.makeText(context, R.string.account_need_login, Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }


}