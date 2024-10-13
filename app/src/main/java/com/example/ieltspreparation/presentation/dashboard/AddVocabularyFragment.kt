package com.example.ieltspreparation.presentation.dashboard

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.ieltspreparation.R
import com.example.ieltspreparation.databinding.FragmentAddVocabularyBinding
import com.example.ieltspreparation.presentation.util.IPActivityUtil
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class AddVocabularyFragment : Fragment() {
    @Inject
    lateinit var activityUtil : IPActivityUtil
    private lateinit var binding:FragmentAddVocabularyBinding
   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_vocabulary, container, false)
       binding.model = this
       activityUtil.hideBottomNavigation(true)

       binding.backIv.setOnClickListener {
           findNavController().popBackStack()
       }
       isEnableSaveButton(false)
       val wordEStream = RxTextView.textChanges(binding.EWordEt)
           .skipInitialValue()
           .map { name ->
               name.isEmpty()
           }

       val wordBStream = RxTextView.textChanges(binding.BwordEt)
           .skipInitialValue()
           .map { company ->
               company.isEmpty()
           }

       val synonymOneStream = RxTextView.textChanges(binding.synonymOneEt)
           .skipInitialValue()
           .map { details ->
               details.isEmpty()
           }

       val synonymTwoStream = RxTextView.textChanges(binding.synonymTwoEt)
           .skipInitialValue()
           .map { price ->
               price.isEmpty()
           }

       val antonymOneStream = RxTextView.textChanges(binding.antonymoneEt)
           .skipInitialValue()
           .map { self ->
               self.isEmpty()
           }

       val antonymTwoStream = RxTextView.textChanges(binding.antonymtwoEt)
           .skipInitialValue()
           .map { row ->
               row.isEmpty()
           }

       val invalidFiledStream = Observable.combineLatest(
           wordEStream,
           wordBStream,
           synonymOneStream,
           synonymTwoStream,
           antonymOneStream,
           antonymTwoStream
       ){wordEInvalid:Boolean, wordBInvalid:Boolean,synonymOneInvalid:Boolean,synonymTwoInvalid:Boolean, antonymOneInvalid:Boolean,antonymTwoInvalid:Boolean ->
           !wordEInvalid && !wordBInvalid && !synonymOneInvalid && !synonymTwoInvalid && !antonymOneInvalid && !antonymTwoInvalid
       }
       invalidFiledStream.subscribe { isValid ->
           isEnableSaveButton(isValid)
       }
       return binding.root
    }
    private fun isEnableSaveButton(isEnable:Boolean){
        if (isEnable){
            binding.btnSave.isEnabled = true
            binding.btnSave.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue_300)
        }
        else{
            binding.btnSave.isEnabled = false
            binding.btnSave.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue_500)
        }
    }
}