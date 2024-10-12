package com.example.ieltspreparation.presentation.util

interface IPharedPreferencesUtil {
    fun logout()
    fun getAuthToken():String?
    fun setAuthToken(token:String)
}