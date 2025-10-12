package com.example.heartbeat.presentation.features.users.staff.ui.home.approve_donation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.heartbeat.presentation.features.donation.viewmodel.DonationViewModel
import com.example.heartbeat.presentation.features.event.viewmodel.EventViewModel
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorFormState
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorViewModel
import com.example.heartbeat.presentation.features.users.staff.component.SubScreenTopBar
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens

@Composable
fun ApproveScreen(
    eventId: String,
    donationViewModel: DonationViewModel = hiltViewModel(),
    donorViewModel: DonorViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by donationViewModel.uiState.collectAsState()
    val donations = uiState.donations

    val selectedEvent by eventViewModel.selectedEvent.collectAsState()
    val donorCache by donorViewModel.donorCache.collectAsState()

    LaunchedEffect(eventId) {
        donationViewModel.observeDonationsByEvent(eventId)
        eventViewModel.getEventById(eventId)
    }

    Scaffold(
        topBar = {
            SubScreenTopBar(
                text = "Approve",
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        selectedEvent?.let { event ->
            Crossfade(targetState = uiState.isLoading, label = "donor_shimmer_fade") { isLoading ->
                if (isLoading) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(Dimens.PaddingM)
                    ) {
                        Text(
                            text = event.name
                        )

                        Spacer(modifier = Modifier.height(AppSpacing.Large))

                        LazyColumn(
                            modifier = Modifier
                                .padding(horizontal = Dimens.PaddingXS)
                                .fillMaxWidth()
                                .heightIn(max = Dimens.HeightShimmer)
                        ) {
                            items(2) {
                                DonorInfoShimmer()
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(Dimens.PaddingM)
                    ) {
                        Text(
                            text = event.name
                        )

                        Spacer(modifier = Modifier.height(AppSpacing.Large))

                        LazyColumn(
                            modifier = Modifier
                                .padding(horizontal = Dimens.PaddingXS)
                                .fillMaxWidth()
                                .heightIn(max = Dimens.HeightShimmer)
                        ) {
                            items(donations.filter { it.status == "PENDING" }) { donation ->
                                val donor = donorCache[donation.donorId]

                                LaunchedEffect(donation.donorId) {
                                    if (donor == null) {
                                        donorViewModel.fetchDonorAndCache(donation.donorId)
                                    }
                                }

                                donor?.let {
                                    val formState = DonorFormState(
                                        name = it.name,
                                        phoneNumber = it.phoneNumber,
                                        bloodGroup = it.bloodGroup,
                                        city = it.city,
                                        dateOfBirth = it.dateOfBirth,
                                        age = it.age,
                                        gender = it.gender
                                    )

                                    DonorInfo(
                                        formState = formState,
                                        onApprove = {
                                            donationViewModel.updateStatus(
                                                donationId = donation.donationId,
                                                status = "APPROVED"
                                            )
                                        },
                                        onReject = {
                                            donationViewModel.updateStatus(
                                                donationId = donation.donationId,
                                                status = "REJECTED"
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DonorInfo(
    formState: DonorFormState,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(AppShape.SmallShape))
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    formState.name,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formState.bloodGroup,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = formState.dateOfBirth,
                fontSize = 12.sp,
                color = Color.Gray
            )

            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }
        }

        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 44.dp, end = 8.dp, bottom = 8.dp)
            ) {
                Text("City: ${formState.city}")
                Text("Tuổi: ${formState.age}")
                Text("Giới tính: ${formState.gender}")
                Text("SĐT: ${formState.phoneNumber}")

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Duyệt")
                    }

                    OutlinedButton(
                        onClick = onReject,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                    ) {
                        Text("Từ chối")
                    }
                }
            }
        }

        Divider(color = Color(0xFFE0E0E0))
    }
}

@Composable
fun DonorInfoShimmer() {
    val transition = rememberInfiniteTransition(label = "donor_shimmer")
    val shimmerTranslate by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "donor_shimmer_translate"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.4f),
            Color.White.copy(alpha = 0.7f),
            Color.LightGray.copy(alpha = 0.4f)
        ),
        start = Offset(x = shimmerTranslate - 1000f, y = 0f),
        end = Offset(x = shimmerTranslate, y = 0f)
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(AppShape.SmallShape))
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(shimmerBrush, CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .height(12.dp)
                        .fillMaxWidth(0.5f)
                        .background(shimmerBrush, RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .height(10.dp)
                        .fillMaxWidth(0.1f)
                        .background(shimmerBrush, RoundedCornerShape(4.dp))
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .height(10.dp)
                    .width(80.dp)
                    .background(shimmerBrush, RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(shimmerBrush, CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))
        }

//        Spacer(modifier = Modifier.height(2.dp))

        Divider(color = Color(0xFFE0E0E0))
    }
}