package com.example.heartbeat.presentation.features.users.staff.ui.home.approve_donation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.event.Event
import com.example.heartbeat.presentation.features.donation.viewmodel.DonationViewModel
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.system.setting.SettingTitle
import com.example.heartbeat.presentation.features.users.staff.ui.home.EventFetcher
import com.example.heartbeat.presentation.features.users.staff.ui.home.EventPendingApprovalCard
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.CompassionBlueLight
import com.example.heartbeat.ui.theme.CompassionBlueText
import com.google.accompanist.navigation.animation.AnimatedNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllPendingRequestsActivity : BaseComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            AnimatedNavHost(navController = navController, startDestination = "all_pending") {
                composable("all_pending") {
                    AllPendingRequestsScreen(
                        navController = navController,
                        onBackClick = { finish() }
                    )
                }

                composable(
                    route = "staff_approve/{eventId}",
                    arguments = listOf(navArgument("eventId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val eventId = backStackEntry.arguments?.getString("eventId")
                    ApproveScreen(eventId = eventId ?: "", navController = navController)
                }
            }
        }
    }
}

@Composable
fun AllPendingRequestsScreen(
    eventViewModel: EventViewModel = hiltViewModel(),
    donationViewModel: DonationViewModel = hiltViewModel(),
    navController: NavController,
    onBackClick: () -> Unit
) {
    val uiState by donationViewModel.uiState.collectAsState()
    val pendingList = uiState.donations.filter { it.status == "PENDING" }
    val groupedByEvent = pendingList.groupBy { it.eventId }
    val entries = groupedByEvent.entries.toList()

    val eventCache = remember { mutableStateMapOf<String, Event>() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.PaddingM + 2.dp)
            .padding(top = Dimens.PaddingM),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSM)
    ) {
        SettingTitle(
            text = stringResource(R.string.pending_requests),
            onClick = onBackClick
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        if(uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BloodRed)
            }
        } else {
            if (entries.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.no_pending_requests),
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    items(entries) { entry ->
                        val eventId = entry.key
                        val donations = entry.value

                        EventFetcher(eventId, eventViewModel, eventCache) { event ->
                            EventPendingApprovalCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(Dimens.HeightXL2)
                                    .animateContentSize(),
                                event = event,
                                pendingCount = donations.size,
                                navController = navController,
                                cardColor = CompassionBlueLight,
                                textColor = CompassionBlueText
                            )
                        }
                    }
                }
            }
        }
    }
}