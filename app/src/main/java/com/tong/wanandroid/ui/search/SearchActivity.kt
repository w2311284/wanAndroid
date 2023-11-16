package com.tong.wanandroid.ui.search

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.tong.wanandroid.R
import com.tong.wanandroid.databinding.ActivitySearchBinding
import com.tong.wanandroid.ui.search.child.SearchBeginFragment
import com.tong.wanandroid.ui.search.child.SearchResultFragment


class SearchActivity : AppCompatActivity() {

    private var searchBeginFragment = SearchBeginFragment()
    private var searchResultFragment = SearchResultFragment()
    private var _binding: ActivitySearchBinding? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivitySearchBinding.inflate(layoutInflater)
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
    }

    private fun search(keywords: String) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEdit.windowToken,0)
        if (keywords.isBlank()) return
        if(searchResultFragment.isAdded.not()){

        }

    }
}