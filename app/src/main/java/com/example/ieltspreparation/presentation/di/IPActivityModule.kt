package com.example.ieltspreparation.presentation.di

import android.app.Activity
import com.example.ieltspreparation.presentation.util.IPActivityUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object IPActivityModule {
    @Provides
    fun providedMGBActivityUtil(activity: Activity): IPActivityUtil {
        return IPActivityUtil(activity as IPActivityUtil.ActivityListener)
    }
}