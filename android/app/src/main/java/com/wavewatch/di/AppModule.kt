package com.wavewatch.di

import android.content.Context
import com.wavewatch.data.repository.SecurityRepository
import com.wavewatch.data.source.AppUsageDataSource
import com.wavewatch.data.source.BluetoothDataSource
import com.wavewatch.data.source.NetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppUsageDataSource(
        @ApplicationContext context: Context
    ): AppUsageDataSource = AppUsageDataSource(context)

    @Provides
    @Singleton
    fun provideBluetoothDataSource(
        @ApplicationContext context: Context
    ): BluetoothDataSource = BluetoothDataSource(context)

    @Provides
    @Singleton
    fun provideNetworkDataSource(
        @ApplicationContext context: Context
    ): NetworkDataSource = NetworkDataSource(context)

    @Provides
    @Singleton
    fun provideSecurityRepository(
        appUsageDataSource: AppUsageDataSource,
        bluetoothDataSource: BluetoothDataSource,
        networkDataSource: NetworkDataSource
    ): SecurityRepository = SecurityRepository(
        appUsageDataSource,
        bluetoothDataSource,
        networkDataSource
    )
}
