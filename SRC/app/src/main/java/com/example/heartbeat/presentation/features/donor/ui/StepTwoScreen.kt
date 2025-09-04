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
    onUpdate: (dateOfBirth: String, age: Int, gender: String, willingToDonate: Boolean, about: String) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var dateOfBirth by remember { mutableStateOf(formState.dateOfBirth) }
    var age by remember { mutableIntStateOf(formState.age) }
    var gender by remember { mutableStateOf(formState.gender) }
    var willingToDonate by remember { mutableStateOf(formState.willingToDonate) }
    var about by remember { mutableStateOf(formState.about) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = { dateOfBirth = it },
            label = { Text("DOB") },
            modifier = Modifier.fillMaxWidth()
        )

        age = 19

        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("Gender") },
            modifier = Modifier.fillMaxWidth()
        )

        willingToDonate = true

        OutlinedTextField(
            value = about,
            onValueChange = { about = it },
            label = { Text("About") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    onBack()
                },
                modifier = Modifier
            ) {
                Text("Back")
            }

            Button(
                onClick = {
                    onUpdate(dateOfBirth, age, gender, willingToDonate, about)
                    onNext()
                },
                modifier = Modifier
            ) {
                Text("Next")
            }
        }
    }
}