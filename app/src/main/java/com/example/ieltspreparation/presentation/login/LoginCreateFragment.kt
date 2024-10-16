package com.example.ieltspreparation.presentation.login

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.ieltspreparation.R
import com.example.ieltspreparation.databinding.FragmentLoginCreateBinding
import com.example.ieltspreparation.presentation.util.IPActivityUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject
@AndroidEntryPoint
class LoginCreateFragment : Fragment() {
    val actionSignIn = Navigation.createNavigateOnClickListener(R.id.action_loginCreateFragment_to_loginInputFragment)
    private lateinit var binding: FragmentLoginCreateBinding
    private lateinit var viewModel: LoginViewModel
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    @Inject
    lateinit var activityUtil:IPActivityUtil
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login_create, container, false)
        binding.model = this
        isEnableSignUpButton(false)
        activityUtil.hideBottomNavigation(true)
        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }
        auth = Firebase.auth
        database = Firebase.database.reference

        val fullNameStream = RxTextView.textChanges(binding.fullNameEt)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }
        fullNameStream.subscribe {
            binding.fullNameEt.error = if (it) getString(R.string.error_name) else null
        }
        val emailStream = RxTextView.textChanges(binding.emailEt)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe {
            binding.emailEt.error = if (it) getString(R.string.error_email) else null
        }
        val phoneNumberStream = RxTextView.textChanges(binding.phoneNumberEt)
            .skipInitialValue()
            .map { phone ->
                phone.length < 11
            }
        phoneNumberStream.subscribe {
            binding.phoneNumberEt.error = if (it) getString(R.string.error_number) else null
        }
        val locationStream = RxTextView.textChanges(binding.locationEt)
            .skipInitialValue()
            .map { location ->
                location.isEmpty()
            }
        locationStream.subscribe {
            binding.locationEt.error = if (it) getString(R.string.error_address) else null
        }
        val dobStream = RxTextView.textChanges(binding.dobEt)
            .skipInitialValue()
            .map { dob ->
                dob.isEmpty()
            }
        dobStream.subscribe {
            binding.dobEt.error = if (it) getString(R.string.error_dob) else null
        }
        binding.dobEt.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val datePickerDialog = DatePickerDialog(requireContext())
                datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                    val selectedDate = android.icu.util.Calendar.getInstance()
                    selectedDate.set(year, month, dayOfMonth)
                    val dateFormat = android.icu.text.SimpleDateFormat("dd/MM/yyyy", Locale.US)
                    val formattedDate = dateFormat.format(selectedDate.time)
                    binding.dobEt.setText(formattedDate)
                }
                datePickerDialog.show()
                return@OnTouchListener true
            }
            false
        })
        val passwordStream = RxTextView.textChanges(binding.passwordEt)
            .skipInitialValue()
            .map { password ->
                password.isEmpty()
            }
        val passwordConfirmStream = io.reactivex.Observable.merge(
            RxTextView.textChanges(binding.passwordEt)
                .skipInitialValue()
                .map { password ->
                    password.toString() != binding.confirmPassEt.text.toString()
                },
            RxTextView.textChanges(binding.confirmPassEt)
                .skipInitialValue()
                .map { passConfirm ->
                    passConfirm.toString() != binding.passwordEt.text.toString()
                })
        val invalidFiledStream = io.reactivex.Observable.combineLatest(
            fullNameStream,
            emailStream,
            phoneNumberStream,
            locationStream,
            dobStream,
            passwordStream,
            passwordConfirmStream
        ) { nameInvalid: Boolean, emailInvalid: Boolean, phoneInvalid: Boolean, locationInvalid: Boolean, dobInvalid: Boolean, passwordInvalid: Boolean, passwordConfirmInvalid: Boolean ->
            !nameInvalid && !emailInvalid && !phoneInvalid && !locationInvalid && !dobInvalid && !passwordInvalid && !passwordConfirmInvalid
        }
        invalidFiledStream.subscribe { isValid ->
            isEnableSignUpButton(isValid)
        }
        binding.continueButton.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) {

                        if (it.isSuccessful) {
                            Toast.makeText(
                                activity,
                                getString(R.string.auth_massage),
                                Toast.LENGTH_SHORT
                            ).show()
                            saveData()
                            findNavController().navigate(R.id.action_loginCreateFragment_to_loginInputFragment)
                        } else {
                            Toast.makeText(activity, it.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        return binding.root
    }

    fun saveData() {
        activityUtil.setFullScreenLoading(true)
        database = FirebaseDatabase.getInstance().getReference("User")
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val user = HashMap<String, String>()
        user.put("uid", userId)
        user.put("name", binding.fullNameEt.text.toString().trim())
        user.put("email", binding.emailEt.text.toString().trim())
        user.put("number", binding.phoneNumberEt.text.toString().trim())
        user.put("location", binding.locationEt.text.toString().trim())
        user.put("dob", binding.dobEt.text.toString().trim())
        user.put("password", binding.passwordEt.text.toString().trim())
        user.put("image","")
        user.put("user", "1")
        database.child(userId).setValue(user)
        activityUtil.setFullScreenLoading(false)
        binding.fullNameEt.text?.clear()
        binding.emailEt.text?.clear()
        binding.phoneNumberEt.text?.clear()
        binding.locationEt.text?.clear()
        binding.dobEt.text?.clear()
        binding.passwordEt.text?.clear()
        binding.confirmPassEt.text?.clear()
    }

    private fun isEnableSignUpButton(isEnable: Boolean) {
        if (isEnable == true) {
            binding.continueButton.isEnabled = true
            binding.continueButton.backgroundTintList =
                ContextCompat.getColorStateList(requireActivity(), R.color.blue_300)
        } else {
            binding.continueButton.isEnabled = false
            binding.continueButton.backgroundTintList =
                ContextCompat.getColorStateList(requireActivity(), R.color.blue_500)
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }
}