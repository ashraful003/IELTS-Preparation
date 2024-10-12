package com.example.ieltspreparation.presentation.util

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Base64
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharePreferenceUtil@Inject constructor(@ApplicationContext context: Context) :
    ISharedPreferencesUtil {
    private val sharedPref = context.getSharedPreferences(
        "com.example.ieltspreparation.PREFERENCE_FILE_KEY",
        Context.MODE_PRIVATE
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setString(key: String, value: String?) {
        val editor = sharedPref.edit()
        editor.putString(encryptString(key), value)
        value?.let { editor.apply() } ?: editor.remove(key)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getString(key: String, defValue: String): String {
        return sharedPref.getString(encryptString(key), defValue)!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun encryptString(input: String?): String? {
        input?.let {
            return Base64.getEncoder().encodeToString(input.toByteArray())
        } ?: run {
            return null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun logout() {
        setAuthToken("")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAuthToken(): String? {
        with(getString(AUTH_TOKEN, "")) {
            if (isNullOrEmpty()) {
                return null
            } else return this
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setAuthToken(token: String) {
        setString(AUTH_TOKEN, token)
    }

    companion object {
        const val AUTH_TOKEN = "auth_token"
    }
}