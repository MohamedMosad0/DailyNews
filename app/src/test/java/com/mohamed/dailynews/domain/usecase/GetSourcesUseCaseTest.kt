package com.mohamed.dailynews.domain.usecase

import com.mohamed.dailynews.data.fakes.FakeNewsRepository
import com.mohamed.dailynews.domain.model.Source
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetSourcesUseCaseTest {

    private lateinit var repository: FakeNewsRepository
    private lateinit var useCase: GetSourcesUseCase

    @Before
    fun setUp() {
        repository = FakeNewsRepository()
        useCase = GetSourcesUseCase(repository)
    }

    @Test
    fun `execute returns list of sources from repository`() = runTest {
        val expected = listOf(
            Source(id = "1", name = "Source 1"),
            Source(id = "2", name = "Source 2")
        )
        repository.sourcesToReturn = expected

        val actual = useCase.execute("sports")

        assertEquals(expected, actual)
    }
}
