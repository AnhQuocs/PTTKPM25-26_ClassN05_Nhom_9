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
    fun `initial state and internal fields coverage`() {
        // Phủ các getter mặc định và internal fields
        assertNotNull(viewModel.unifiedSearchUseCase)
        assertNotNull(viewModel.unifiedSuggestionUseCase)
        assertNotNull(viewModel.recentSearchUseCase)
        
        assertEquals("", viewModel.query)
        assertTrue(viewModel.suggestions.isEmpty())
        assertTrue(viewModel.searchResults.isEmpty())
        assertFalse(viewModel.isLoading)
        assertFalse(viewModel.showSuggestions)
    }

    @Test
    fun `onQueryChanged with valid text updates suggestions`() = runTest {
        val mockSuggestions = listOf(SearchSuggestionItem.EventSuggestion(testEvent))
        coEvery { unifiedSuggestionUseCase(any()) } returns mockSuggestions

        viewModel.onQueryChanged("  Blood  ")
        
        assertEquals("  Blood  ", viewModel.query)
        assertTrue(viewModel.showSuggestions)
        
        advanceUntilIdle()
        assertEquals(mockSuggestions, viewModel.suggestions)
        coVerify { unifiedSuggestionUseCase("blood") } // Kiểm tra normalize
    }

    @Test
    fun `onQueryChanged with blank text clears state`() = runTest {
        viewModel.onQueryChanged("   ")
        assertFalse(viewModel.showSuggestions)
        
        advanceUntilIdle()
        assertTrue(viewModel.suggestions.isEmpty())
    }

    @Test
    fun `onQueryChanged handles throwable during fetch`() = runTest {
        coEvery { unifiedSuggestionUseCase(any()) } throws Throwable("Error")
        
        viewModel.onQueryChanged("error")
        advanceUntilIdle()
        
        assertTrue(viewModel.suggestions.isEmpty())
    }

    @Test
    fun `onSearch success updates results`() = runTest {
        val mockResults = listOf(SearchResultItem.EventItem(testEvent))
        coEvery { unifiedSearchUseCase(any()) } returns mockResults

        viewModel.onQueryChanged("blood")
        viewModel.onSearch()

        assertTrue(viewModel.isLoading)
        advanceUntilIdle()

        assertEquals(mockResults, viewModel.searchResults)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `onSearch handles throwable and finally block`() = runTest {
        coEvery { unifiedSearchUseCase(any()) } throws Throwable("Error")
        
        viewModel.onSearch()
        advanceUntilIdle()
        
        assertTrue(viewModel.searchResults.isEmpty())
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `onSuggestionClicked handles user null branch`() = runTest {
        val suggestion = SearchSuggestionItem.EventSuggestion(testEvent)
        val auth = mockk<FirebaseAuth>()
        every { FirebaseAuth.getInstance() } returns auth
        every { auth.currentUser } returns null

        viewModel.onSuggestionClicked(suggestion)

        assertEquals(testEvent.name, viewModel.query)
        assertFalse(viewModel.showSuggestions)
        coVerify(exactly = 0) { addRecentSearchUseCase(any(), any()) }
    }

    @Test
    fun `onSuggestionClicked handles uid null branch`() = runTest {
        val suggestion = SearchSuggestionItem.EventSuggestion(testEvent)
        val auth = mockk<FirebaseAuth>()
        val user = mockk<FirebaseUser>()
        every { FirebaseAuth.getInstance() } returns auth
        every { auth.currentUser } returns user
        every { user.uid } returns ""

        viewModel.onSuggestionClicked(suggestion)

        coVerify(exactly = 0) { addRecentSearchUseCase(any(), any()) }
    }

    @Test
    fun `onSuggestionClicked saves recent search successfully`() = runTest {
        val suggestion = SearchSuggestionItem.EventSuggestion(testEvent)
        val auth = mockk<FirebaseAuth>()
        val user = mockk<FirebaseUser>()
        every { FirebaseAuth.getInstance() } returns auth
        every { auth.currentUser } returns user
        every { user.uid } returns "user123"

        viewModel.onSuggestionClicked(suggestion)
        advanceUntilIdle()

        coVerify { addRecentSearchUseCase("user123", any()) }
    }

    @Test
    fun `onSuggestionClicked handles throwable in history save`() = runTest {
        val suggestion = SearchSuggestionItem.EventSuggestion(testEvent)
        val auth = mockk<FirebaseAuth>()
        val user = mockk<FirebaseUser>()
        every { FirebaseAuth.getInstance() } returns auth
        every { auth.currentUser } returns user
        every { user.uid } returns "user123"
        
        coEvery { addRecentSearchUseCase(any(), any()) } throws Throwable("Save error")

        viewModel.onSuggestionClicked(suggestion)
        advanceUntilIdle()
        
        // Không crash, query vẫn cập nhật
        assertEquals(testEvent.name, viewModel.query)
    }

    @Test
    fun `clearQuery resets search state`() {
        viewModel.onQueryChanged("Blood")
        viewModel.clearQuery()
        
        assertEquals("", viewModel.query)
        assertTrue(viewModel.suggestions.isEmpty())
    }

    @Test
    fun `normalize handles multiple spaces correctly`() = runTest {
        coEvery { unifiedSearchUseCase(any()) } returns emptyList()
        
        viewModel.onQueryChanged("  Heavy    Space  ")
        viewModel.onSearch()
        advanceUntilIdle()
        
        coVerify { unifiedSearchUseCase("heavy space") }
    }
}
