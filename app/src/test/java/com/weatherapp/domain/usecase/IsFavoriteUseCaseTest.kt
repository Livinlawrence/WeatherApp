package com.weatherapp.domain.usecase

import com.weatherapp.domain.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class IsFavoriteUseCaseTest {
    private val repository: WeatherRepository = mockk()
    private lateinit var useCase: IsFavoriteUseCase

    @Test
    fun `when repository returns true, usecase returns true`() = runTest {
        useCase = IsFavoriteUseCase(repository)
        coEvery { repository.isFavorite("loc_1") } returns true
        val result = useCase("loc_1")
        assertTrue(result)
    }
}