package com.openweathermap.org

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {


    private lateinit var mainViewModel: WeatherViewModel

    @Mock
    private lateinit var repositoryManager: WeatherRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mainViewModel = WeatherViewModel(repositoryManager)
    }

    @Test
    fun testGetRepositories() {
        mainViewModel.fetchCurrentLocationDetails("33.33","332.33","323dddddd")
        Mockito.verify(repositoryManager).fetchCurrentWeatherDetails("33.33","332.33","323dddddd")
    }
}