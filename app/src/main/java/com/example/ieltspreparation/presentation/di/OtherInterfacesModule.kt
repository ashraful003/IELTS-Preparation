package com.example.ieltspreparation.presentation.di

import com.example.ieltspreparation.presentation.util.IPharedPreferencesUtil
import com.example.ieltspreparation.presentation.util.SharePreferenceUtil
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface OtherInterfacesModule {
    @Binds
    fun bindSharePreferencesUtil(sharePreferencesUtil: SharePreferenceUtil): IPharedPreferencesUtil
}