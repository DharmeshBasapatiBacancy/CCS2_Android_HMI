package com.bacancy.ccs2androidhmi.views.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bacancy.ccs2androidhmi.R
import com.bacancy.ccs2androidhmi.base.BaseFragment
import com.bacancy.ccs2androidhmi.databinding.FragmentFaultInformationBinding
import com.bacancy.ccs2androidhmi.databinding.FragmentGunsHomeScreenBinding
import com.bacancy.ccs2androidhmi.util.gone

class FaultInfoFragment : BaseFragment() {

    private lateinit var binding: FragmentFaultInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFaultInformationBinding.inflate(layoutInflater)
        setScreenHeaderViews()
        setupViews()
        return binding.root
    }

    override fun setScreenHeaderViews() {
        binding.apply {
            incHeader.tvHeader.text = getString(R.string.lbl_fault_information)
            incHeader.tvSubHeader.gone()
        }
    }

    override fun setupViews() {
    }
}