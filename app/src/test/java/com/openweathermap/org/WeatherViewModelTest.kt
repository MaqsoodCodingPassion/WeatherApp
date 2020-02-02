package com.openweathermap.org

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openweathermap.org.model.CurrentWeatherResponse
import com.openweathermap.org.service.Service
import io.reactivex.Single
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.io.File

class WeatherViewModelTest {

    private lateinit var weatherResponse: CurrentWeatherResponse

    @Mock
    private lateinit var service: Service

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val response = getJson("json/weather_response.json")
        val turnsType = object : TypeToken<CurrentWeatherResponse>() {}.type
        weatherResponse = Gson().fromJson<CurrentWeatherResponse>(response, turnsType)
        Mockito.`when`(service.getCurrentWeatherDetailsService("3222.222", "223.2", "2332e23333"))
            .thenReturn(Single.just(weatherResponse))
    }

    private fun getJson(path: String): String {
        val file = File("src/test/resources/$path")
        return String(file.readBytes())
    }

    @Test
    fun `Test getWeatherDetails API should not null`() {
        assertNotNull(service.getCurrentWeatherDetailsService("3222.222", "223.2", "2332e23333"))
    }

    @Test
    fun `Test getWeatherDetails data items are not null`() {
        assertNotNull(service.getCurrentWeatherDetailsService("3222.222", "223.2", "2332e23333")
            .test()
            .assertComplete()
            .assertValue {
                it.base != null
                it.name != null
                it.clouds!!.all != null
                it.visibility != null
                it.sys != null
                it.weather != null
                it.wind != null
                it.id != null
            })
    }
}