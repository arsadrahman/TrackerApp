package com.arsa.trackerapp.di

import android.content.Context
import androidx.room.Room
import com.arsa.trackerapp.repo.offline.OfflineDAO
import com.arsa.trackerapp.repo.offline.OfflineLocationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideOfflineDAO(offlineLocationDatabase: OfflineLocationDatabase): OfflineDAO {
        return offlineLocationDatabase.offlineDAO()
    }

    @Singleton
    @Provides
    fun provideOfflineLocationDatabase(@ApplicationContext context: Context): OfflineLocationDatabase {
        return OfflineLocationDatabase.getOfflineDB(context)
    }

}