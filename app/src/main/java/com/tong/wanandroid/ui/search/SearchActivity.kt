package com.tong.wanandroid.ui.search

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tong.wanandroid.R
import com.tong.wanandroid.databinding.ActivitySearchBinding
import com.tong.wanandroid.ui.search.child.SearchBeginFragment
import com.tong.wanandroid.ui.search.child.SearchResultFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SearchActivity : AppCompatActivity() {

    private var searchBeginFragment = SearchBeginFragment()
    private var searchResultFragment = SearchResultFragment()
    private var _binding: ActivitySearchBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: SearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.searchFragmentContainer,
                    searchBeginFragment,
                    searchBeginFragment.javaClass.simpleName
                )
                .commit()
        } else {
            searchBeginFragment =
                supportFragmentManager.findFragmentByTag(SearchBeginFragment::class.java.simpleName) as? SearchBeginFragment
                    ?: searchBeginFragment

            searchResultFragment =
                supportFragmentManager.findFragmentByTag(SearchResultFragment::class.java.simpleName) as? SearchResultFragment
                    ?: searchResultFragment
        }

        initView()
    }

    private fun initView(){
        binding.backIcon.apply {
            setOnClickListener {
                if (supportFragmentManager.backStackEntryCount != 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    finish()
                }
            }
        }
        binding.searchIcon.apply {
            setOnClickListener {
                search(binding.searchEdit.text?.trim().toString())
            }
        }
        binding.searchEdit.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    search(text?.trim().toString())
                    true
                } else {
                    false
                }
            }
            setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    search(text?.trim().toString())
                    true
                } else {
                    false
                }
            }
            requestFocus()
        }

        viewModel.shortcutSearchLiveData.observe(this) {
            setSearchText(it)
            search(it)
        }

    }

    private fun search(keywords: String) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEdit.windowToken,0)
        if (keywords.isBlank()) return
        if(searchResultFragment.isAdded.not()){
            supportFragmentManager.beginTransaction()
                .hide(searchBeginFragment)
                .add(
                    R.id.searchFragmentContainer,
                    searchResultFragment,
                    searchResultFragment.javaClass.simpleName
                )
                .addToBackStack(null).commit()
        }
        viewModel.search(keywords)

    }

    private fun setSearchText(text: String) {
        binding.searchEdit.setText(text)
        binding.searchEdit.setSelection(text.length)
    }
}