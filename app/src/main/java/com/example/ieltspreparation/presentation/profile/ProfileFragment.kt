package com.example.ieltspreparation.presentation.profile

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.ieltspreparation.R
import com.example.ieltspreparation.databinding.FragmentProfileBinding
import com.example.ieltspreparation.presentation.MainActivity
import com.example.ieltspreparation.presentation.util.IPActivityUtil
import com.example.ieltspreparation.presentation.util.SharePreferenceUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    val actionEditProfile = Navigation.createNavigateOnClickListener(R.id.action_profileFragment_to_updateProfileFragment)
    @Inject
    lateinit var sharedPrefs: SharePreferenceUtil
    @Inject
    lateinit var activityUtil: IPActivityUtil
    lateinit var binding: FragmentProfileBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.model = this
        activityUtil.hideBottomNavigation(false)
        binding.btnSignOut.setOnClickListener {
            logout(it)
        }
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun logout(view: View) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure!")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { _, _ ->
            sharedPrefs.setAuthToken("")
            activity?.let {
                startActivity(MainActivity.getLaunchIntent(it))
            }
        }
        builder.setNegativeButton("No") { _, _ -> }
        val alartDialog = builder.create()
        alartDialog.show()
    }
}