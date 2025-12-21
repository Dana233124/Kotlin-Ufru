package com.example.urfu.profile.data

import android.content.Context
import com.example.urfu.profile.domain.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileDataStore(
        @ApplicationContext context: Context
    ): ProfileDataStore = ProfileDataStore(context)

    @Provides
    @Singleton
    fun provideProfileRepository(
        dataStore: ProfileDataStore
    ): ProfileRepository = ProfileRepositoryImpl(dataStore)
}
