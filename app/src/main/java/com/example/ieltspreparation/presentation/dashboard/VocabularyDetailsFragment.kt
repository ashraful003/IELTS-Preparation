package com.example.ieltspreparation.presentation.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.ieltspreparation.R
import com.example.ieltspreparation.databinding.FragmentVocabularyDetailsBinding
import com.example.ieltspreparation.presentation.util.IPActivityUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class VocabularyDetailsFragment : Fragment() {
    val actionUpdateVocabulary = Navigation.createNavigateOnClickListener(R.id.action_vocabularyDetailsFragment_to_updateVocabularyFragment)
    @Inject
    lateinit var activityUtil: IPActivityUtil
    private lateinit var binding: FragmentVocabularyDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_vocabulary_details, container, false)
        binding.model = this
        activityUtil.hideBottomNavigation(true)
        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }
}