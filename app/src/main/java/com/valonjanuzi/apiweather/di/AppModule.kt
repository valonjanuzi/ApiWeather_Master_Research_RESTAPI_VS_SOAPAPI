package com.valonjanuzi.apiweather.di

import com.valonjanuzi.apiweather.SOAP.WeatherSoapService
import com.valonjanuzi.apiweather.domain.usecase.FetchWeatherRestDataUseCase
import com.valonjanuzi.apiweather.domain.usecase.FetchWeatherSoapDataUseCase
import com.valonjanuzi.apiweather.network.OpenWeatherMapApi
import com.valonjanuzi.apiweather.repository.WeatherRepository
import com.valonjanuzi.apiweather.repository.WeatherRepositoryImpl
import com.valonjanuzi.apiweather.util.Constants
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideOpenWeatherMapApi(retrofit: Retrofit): OpenWeatherMapApi =
        retrofit.create(OpenWeatherMapApi::class.java)

    @Singleton
    @Provides
    fun provideWeatherSoapService(): WeatherSoapService = WeatherSoapService()


    @Provides
    fun provideWeatherRepository(restApi: OpenWeatherMapApi, soapAPi: WeatherSoapService): WeatherRepository = WeatherRepositoryImpl(restApi, soapAPi)



}