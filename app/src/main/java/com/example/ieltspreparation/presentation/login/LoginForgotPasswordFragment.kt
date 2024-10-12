package com.example.ieltspreparation.presentation.login

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ieltspreparation.R
import com.example.ieltspreparation.databinding.FragmentLoginForgotPasswordBinding
import com.example.ieltspreparation.presentation.util.IPActivityUtil
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class LoginForgotPasswordFragment : Fragment() {
    @Inject
    lateinit var activityUtil : IPActivityUtil
    private lateinit var binding: FragmentLoginForgotPasswordBinding
    private lateinit var viewModel : LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login_forgot_password, container, false)
        binding.model = this
        activityUtil.hideBottomNavigation(true)
        val emailStream = RxTextView.textChanges(binding.emailEt)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe {
            binding.emailEt.error = if (it) getString(R.string.error_email) else null
            if (it) {
                binding.btnNext.isEnabled = false
                binding.btnNext.backgroundTintList =
                    ContextCompat.getColorStateList(requireActivity(), R.color.blue_500)
            } else {
                binding.emailEt.error = null
                binding.btnNext.isEnabled = true
                binding.btnNext.backgroundTintList =
                    ContextCompat.getColorStateList(requireActivity(), R.color.blue_300)
            }
        }
        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }
}