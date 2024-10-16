package com.example.ieltspreparation.presentation.profile

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ieltspreparation.R
import com.example.ieltspreparation.databinding.FragmentUpdateProfileBinding
import com.example.ieltspreparation.presentation.util.IPActivityUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class UpdateProfileFragment : Fragment() {
    @Inject
    lateinit var activityUtil: IPActivityUtil
    private lateinit var binding: FragmentUpdateProfileBinding
    private var uid = FirebaseAuth.getInstance().currentUser!!.uid
    private lateinit var database: DatabaseReference
    private var storageRef = Firebase.storage
    private lateinit var uri: Uri
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_update_profile, container, false)
        binding.model = this
        database = Firebase.database.reference
        activityUtil.hideBottomNavigation(true)
        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }
        val fullNameStream = RxTextView.textChanges(binding.fullNameEt)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }
        fullNameStream.subscribe {
            binding.fullNameEt.error = if (it) getString(R.string.error_name) else null
        }
        val passwordStream = RxTextView.textChanges(binding.addressEt)
            .skipInitialValue()
            .map { address ->
                address.isEmpty()
            }
        val phoneNumberStream = RxTextView.textChanges(binding.phoneNumberEt)
            .skipInitialValue()
            .map { phone ->
                phone.length < 11
            }
        phoneNumberStream.subscribe {
            binding.phoneNumberEt.error = if (it) getString(R.string.error_number) else null
        }

        val dobStream = RxTextView.textChanges(binding.dobEt)
            .skipInitialValue()
            .map { dob ->
                dob.isEmpty()
            }
        dobStream.subscribe {
            binding.dobEt.error = if (it) getString(R.string.error_dob) else null
        }
        binding.dobEt.setOnTouchListener(View.OnTouchListener { e, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val datePickerDialog = DatePickerDialog(requireContext())
                datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, dayOfMonth)
                    val dateFormat = SimpleDateFormat("dd/mm/yyyy", Locale.US)
                    val formattedDate = dateFormat.format(selectedDate.time)
                    binding.dobEt.setText(formattedDate)
                }
                datePickerDialog.show()
                return@OnTouchListener true
            }
            false
        })
        showToData()
        binding.saveTv.setOnClickListener {
            updateData(
                binding.fullNameEt.text.toString().trim(),
                binding.addressEt.text.toString().trim(),
                binding.phoneNumberEt.text.toString().trim(),
                binding.dobEt.text.toString().trim()
            )
        }
        uploadImage()
        return binding.root
    }
    private fun updateData(name: String, address: String, phone: String, dob: String) {
        activityUtil.setFullScreenLoading(true)
        if (name.isNotEmpty() && address.isNotEmpty() && phone.isNotEmpty() && dob.isNotEmpty()){
            database = FirebaseDatabase.getInstance().getReference("User")
            val user = HashMap<String,Any>()
            user.put("name",name)
            user.put("location",address)
            user.put("number",phone)
            user.put("dob",dob)
            database.child(uid).updateChildren(user).addOnSuccessListener {
                findNavController().navigate(R.id.action_updateProfileFragment_to_profileFragment)
                activityUtil.setFullScreenLoading(false)
                binding.fullNameEt.text!!.clear()
                binding.addressEt.text!!.clear()
                binding.phoneNumberEt.text!!.clear()
                binding.dobEt.text!!.clear()
                Toast.makeText(activity, getString(R.string.update_massage), Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun uploadImage() {
        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                if (it != null) {
                    uri = it
                    activityUtil.setFullScreenLoading(true)
                    storageRef.getReference("images").child(System.currentTimeMillis().toString())
                        .putFile(uri)
                        .addOnSuccessListener {task ->
                            task.metadata!!.reference!!.downloadUrl
                                .addOnSuccessListener{
                                    val user = mapOf<String,String>(
                                        "image" to it.toString(),
                                    )
                                    val databaseReference = FirebaseDatabase.getInstance().getReference("User")
                                    databaseReference.child(uid).updateChildren(user)
                                        .addOnSuccessListener{
                                            activityUtil.setFullScreenLoading(false)
                                            showToData()
                                            Toast.makeText(activity,"Uploaded Successfully", Toast.LENGTH_SHORT).show()
                                        }.addOnFailureListener {
                                            Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
                                        }
                                }.addOnFailureListener {
                                    Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
                                }
                        }
                }
            }
        )
        binding.imageView.setOnClickListener {
            galleryImage.launch("image/*")
        }
    }
    private fun showToData(){
        activityUtil.setFullScreenLoading(true)
        database.child("User").child(uid).get().addOnSuccessListener {
            activityUtil.setFullScreenLoading(false)
            binding.fullNameEt.setText(it.child("name").value.toString())
            binding.addressEt.setText(it.child("location").value.toString())
            binding.phoneNumberEt.setText(it.child("number").value.toString())
            binding.dobEt.setText(it.child("dob").value.toString())
            Glide.with(this).load(it.child("image").value.toString()).into(binding.imageView)
        }
    }
}