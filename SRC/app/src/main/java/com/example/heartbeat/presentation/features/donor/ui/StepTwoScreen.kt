package com.example.heartbeat.presentation.features.donor.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.heartbeat.presentation.features.donor.viewmodel.DonorFormState

@Composable
fun StepTwoScreen(
    formState: DonorFormState,
    onUpdate: (dateOfBirth: String, age: Int, gender: String, willingToDonate: Boolean, about: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = formState.dateOfBirth,
            onValueChange = {
                onUpdate(it, 19, formState.gender, false, formState.about)
            },
            label = { Text("DOB") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = formState.gender,
            onValueChange = {
                onUpdate(formState.dateOfBirth, formState.age, it, formState.willingToDonate, formState.about)
            },
            label = { Text("Gender") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = formState.about,
            onValueChange = {
                onUpdate(formState.dateOfBirth, formState.age, formState.gender, formState.willingToDonate, it)
            },
            label = { Text("About") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}