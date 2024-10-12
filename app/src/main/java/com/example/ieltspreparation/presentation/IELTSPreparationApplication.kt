package com.example.ieltspreparation.presentation

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
@HiltAndroidApp
class IELTSPreparationApplication:Application() {
    companion object{
        @JvmStatic
        fun getApplication(context: Context) = context.applicationContext as IELTSPreparationApplication
    }
}