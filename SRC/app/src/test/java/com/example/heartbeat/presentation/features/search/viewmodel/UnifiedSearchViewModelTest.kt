package com.example.heartbeat.presentation.features.search.viewmodel

import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.domain.entity.search.SearchResultItem
import com.example.heartbeat.domain.entity.search.SearchSuggestionItem
import com.example.heartbeat.domain.usecase.recent_search.AddRecentSearchUseCase
import com.example.heartbeat.domain.usecase.recent_search.RecentSearchUseCase
import com.example.heartbeat.domain.usecase.search.UnifiedSearchUseCase
import com.example.heartbeat.domain.usecase.search.UnifiedSuggestionUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UnifiedSearchViewModelTest {

    private lateinit var viewModel: UnifiedSearchViewModel
    private val unifiedSearchUseCase = mockk<UnifiedSearchUseCase>(relaxed = true)
    private val unifiedSuggestionUseCase = mockk<UnifiedSuggestionUseCase>(relaxed = true)
    private val addRecentSearchUseCase = mockk<AddRecentSearchUseCase>(relaxed = true)
    private val recentSearchUseCase = RecentSearchUseCase(
        addRecentSearchUseCase = addRecentSearchUseCase,
        getRecentSearchUseCase = mockk(relaxed = true),
        clearAllRecentSearchUseCase = mockk(relaxed = true)
    )

    private val testDispatcher = StandardTestDispatcher()

    private val testEvent = Event(
        id = "E1",
        locationId = "L1",
        name = "Blood Drive",
        description = "Desc",
        date = "2024-10-10",
        time = "10:00",
        deadline = null,
        donorList = emptyList(),
        capacity = 50,
        donorCount = 0,
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(FirebaseAuth::class)
        viewModel = UnifiedSearchViewModel(
            unifiedSearchUseCase,
            unifiedSuggestionUseCase,
            recentSearchUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `comprehensive property coverage and initial state`() {
        assertEquals("", viewModel.query)
        assertTrue(viewModel.suggestions.isEmpty())
        assertTrue(viewModel.searchResults.isEmpty())
        assertFalse(viewModel.isLoading)
        assertFalse(viewModel.showSuggestions)

        assertNotNull(viewModel.unifiedSearchUseCase)
        assertNotNull(viewModel.unifiedSuggestionUseCase)
        assertNotNull(viewModel.recentSearchUseCase)
    }

    @Test
    fun `onQueryChanged with valid text updates suggestions and covers normalize`() = runTest {
        val mockSuggestions = listOf(SearchSuggestionItem.EventSuggestion(testEvent))
        coEvery { unifiedSuggestionUseCase(any()) } returns Result.success(mockSuggestions)

        viewModel.onQueryChanged("  Blood    Drive  ")

        assertEquals("  Blood    Drive  ", viewModel.query)
        assertTrue(viewModel.showSuggestions)

        advanceUntilIdle()
        assertEquals(mockSuggestions, viewModel.suggestions)
        coVerify { unifiedSuggestionUseCase("blood drive") }
    }

    @Test
    fun `onQueryChanged with blank text variants covers all isBlank branches`() = runTest {
        viewModel.onQueryChanged("")
        assertFalse(viewModel.showSuggestions)
        advanceUntilIdle()
        assertTrue(viewModel.suggestions.isEmpty())

        viewModel.onQueryChanged("   ")
        assertFalse(viewModel.showSuggestions)
        advanceUntilIdle()
        assertTrue(viewModel.suggestions.isEmpty())
    }

    @Test
    fun `onQueryChanged handles throwable during fetch and covers catch block`() = runTest {
        coEvery { unifiedSuggestionUseCase(any()) } throws Throwable("Fetch Error")

        viewModel.onQueryChanged("error")
        advanceUntilIdle()

        assertTrue(viewModel.suggestions.isEmpty())
    }

    @Test
    fun `onSearch success updates results and covers finally block`() = runTest {
        val mockResults = listOf(SearchResultItem.EventItem(testEvent))
        coEvery { unifiedSearchUseCase(any()) } returns Result.success(mockResults)

        viewModel.onQueryChanged("blood")
        viewModel.onSearch()

        assertTrue(viewModel.isLoading)
        advanceUntilIdle()

        assertEquals(mockResults, viewModel.searchResults)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `onSearch handles throwable and covers finally block on error`() = runTest {
        coEvery { unifiedSearchUseCase(any()) } throws Throwable("Search Error")

        viewModel.onSearch()
        assertTrue(viewModel.isLoading)
        advanceUntilIdle()

        assertTrue(viewModel.searchResults.isEmpty())
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `onSuggestionClicked handles all user and uid branches`() = runTest {
        val suggestion = SearchSuggestionItem.EventSuggestion(testEvent)
        val auth = mockk<FirebaseAuth>()
        val user = mockk<FirebaseUser>()
        every { FirebaseAuth.getInstance() } returns auth

        every { auth.currentUser } returns null
        viewModel.onSuggestionClicked(suggestion)
        assertEquals(testEvent.name, viewModel.query)
        assertFalse(viewModel.showSuggestions)
        coVerify(exactly = 0) { addRecentSearchUseCase(any(), any()) }

        every { auth.currentUser } returns user
        every { user.uid } returns ""
        viewModel.onSuggestionClicked(suggestion)
        coVerify(exactly = 0) { addRecentSearchUseCase(any(), any()) }

        every { user.uid } returns "user_123"
        coEvery { addRecentSearchUseCase(any(), any()) } returns Result.success(Unit)
        viewModel.onSuggestionClicked(suggestion)
        advanceUntilIdle()
        coVerify(exactly = 1) { addRecentSearchUseCase("user_123", any()) }
    }

    @Test
    fun `onSuggestionClicked handles throwable in catch block`() = runTest {
        val suggestion = SearchSuggestionItem.EventSuggestion(testEvent)
        val auth = mockk<FirebaseAuth>()
        val user = mockk<FirebaseUser>()
        every { FirebaseAuth.getInstance() } returns auth
        every { auth.currentUser } returns user
        every { user.uid } returns "user_123"

        coEvery { addRecentSearchUseCase(any(), any()) } throws Throwable("Save error")

        viewModel.onSuggestionClicked(suggestion)
        advanceUntilIdle()

        assertEquals(testEvent.name, viewModel.query)
    }

    @Test
    fun `clearQuery resets state properties to defaults`() {
        viewModel.onQueryChanged("Blood")
        viewModel.clearQuery()

        assertEquals("", viewModel.query)
        assertTrue(viewModel.suggestions.isEmpty())
    }

    @Test
    fun `normalize handling through search actions`() = runTest {
        coEvery { unifiedSearchUseCase(any()) } returns Result.success(emptyList<SearchResultItem>())

        viewModel.onQueryChanged("   Mixed   CASE   Spaces   ")
        viewModel.onSearch()
        advanceUntilIdle()

        coVerify { unifiedSearchUseCase("mixed case spaces") }
    }
}
