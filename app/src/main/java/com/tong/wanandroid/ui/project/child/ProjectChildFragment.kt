package com.tong.wanandroid.ui.project.child

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tong.wanandroid.R

class ProjectChildFragment : Fragment() {

    companion object {
        fun newInstance(id: Int) = ProjectChildFragment()
    }

    private lateinit var viewModel: ProjectChildViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_project_child, container, false)
    }

}