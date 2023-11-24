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
import com.tong.wanandroid.common.store.UserInfoManager
import com.tong.wanandroid.databinding.FragmentProfileBinding
import com.tong.wanandroid.ui.login.LoginActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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

        viewModel.userInfoLiveData.observe(viewLifecycleOwner){
            binding.user = it
            lifecycleScope.launch {
                UserInfoManager.getInstance(requireContext()).cacheUserBaseInfo(it)
            }
        }

        binding.apply {
            profileItemList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = profileAdapter
            }
            arrayOf(userAvatar, userName, userId, userCoinCount).forEach {
                it.setOnClickListener {
                    checkLogin {}
                }
            }
            userCoinCount.setOnClickListener {
                checkLogin {

                }
            }
        }

        var isLoggedInFlow = UserInfoManager.getInstance(requireContext()).isLogIn

        isLoggedInFlow?.onEach { loggedIn ->
            if (loggedIn){
                viewModel.getUserInfo()
                viewModel.isLogin = true
            }else{
                viewModel.isLogin = false
            }
        }?.launchIn(lifecycleScope)


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