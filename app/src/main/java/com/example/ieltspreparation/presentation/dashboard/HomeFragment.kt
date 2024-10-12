package com.example.ieltspreparation.presentation.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.ieltspreparation.R
import com.example.ieltspreparation.databinding.FragmentHomeBinding
import com.example.ieltspreparation.presentation.util.IPActivityUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    @Inject
    lateinit var activityUtil: IPActivityUtil
    val actionVocabulary = Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_vocabularyFragment)
    val actionGrammar = Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_grammarFragment)
    val actionIelts = Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_ieltsFragment)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.model = this
        activityUtil.hideBottomNavigation(false)
        return binding.root
    }
}