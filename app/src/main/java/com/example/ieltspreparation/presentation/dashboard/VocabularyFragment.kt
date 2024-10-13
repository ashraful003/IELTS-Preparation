package com.example.ieltspreparation.presentation.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ieltspreparation.R
import com.example.ieltspreparation.databinding.FragmentVocabularyBinding
import com.example.ieltspreparation.presentation.util.IPActivityUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VocabularyFragment : Fragment() {
    val actionAddVocabulary = Navigation.createNavigateOnClickListener(R.id.action_vocabularyFragment_to_addVocabularyFragment)
    private lateinit var binding:FragmentVocabularyBinding
    @Inject
    lateinit var activityUtil: IPActivityUtil

    lateinit var  adapter:VocabularyListAdapter
    lateinit var userArray : ArrayList<VocabularyModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_vocabulary, container, false)
        binding.model = this
        activityUtil.hideBottomNavigation(true)
        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }
        userArray = arrayListOf()
        adapter = VocabularyListAdapter(userArray,this.requireContext())
        binding.vocabularyListRecycle.layoutManager = LinearLayoutManager(requireContext())
        binding.vocabularyListRecycle.adapter = adapter

        return binding.root
    }
}