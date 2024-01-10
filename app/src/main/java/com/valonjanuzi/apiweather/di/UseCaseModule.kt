package com.valonjanuzi.apiweather.di

import com.valonjanuzi.apiweather.domain.usecase.FetchWeatherRestAndSoapDataUseCase
import com.valonjanuzi.apiweather.domain.usecase.FetchWeatherRestDataUseCase
import com.valonjanuzi.apiweather.domain.usecase.FetchWeatherSoapDataUseCase
import com.valonjanuzi.apiweather.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule{

    @Provides
    fun provideFetchRestWeatherDataUseCase(repository: WeatherRepository): FetchWeatherRestDataUseCase {
        return FetchWeatherRestDataUseCase(repository)
    }

    @Provides
    fun provideFetchSoapWeatherDataUseCase(repository: WeatherRepository): FetchWeatherSoapDataUseCase {
        return FetchWeatherSoapDataUseCase(repository)
    }


    @Provides
    fun providefetchWeatherRestAndSoapDataUseCase(repository: WeatherRepository): FetchWeatherRestAndSoapDataUseCase{
        return FetchWeatherRestAndSoapDataUseCase(repository)
    }

}
