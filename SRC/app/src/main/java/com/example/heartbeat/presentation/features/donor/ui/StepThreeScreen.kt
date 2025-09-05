package com.example.heartbeat.presentation.features.donor.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.heartbeat.presentation.features.donor.viewmodel.DonorFormState

@Composable
fun StepThreeScreen(
    formState: DonorFormState,
    onUpdate: (profileAvatar: String) -> Unit,
) {
    var profileAvatar by remember { mutableStateOf(formState.profileAvatar) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        profileAvatar = "https://"
        onUpdate(profileAvatar)
    }
}