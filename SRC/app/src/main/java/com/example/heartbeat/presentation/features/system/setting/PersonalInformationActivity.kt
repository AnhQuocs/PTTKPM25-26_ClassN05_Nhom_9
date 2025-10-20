package com.example.heartbeat.presentation.features.system.setting

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.users.Donor
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalInformationActivity : BaseComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val donorId = intent.getStringExtra("donorId")

        setContent {
            donorId?.let { PersonalInformationScreen(donorId = it) }
        }
    }
}

@Composable
fun PersonalInformationScreen(
    donorId: String,
    donorViewModel: DonorViewModel = hiltViewModel()
) {
    val formState by donorViewModel.formState.collectAsState()
    val isLoading by donorViewModel.isLoading.collectAsState()

    LaunchedEffect(donorId) {
        donorViewModel.getDonorById(donorId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Cập nhật thông tin cá nhân",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = formState.name,
            onValueChange = { donorViewModel.updatePersonalInfo(it, formState.phoneNumber, formState.bloodGroup, formState.city) },
            label = { Text("Họ và tên") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = formState.phoneNumber,
            onValueChange = { donorViewModel.updatePersonalInfo(formState.name, it, formState.bloodGroup, formState.city) },
            label = { Text("Số điện thoại") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        OutlinedTextField(
            value = formState.bloodGroup,
            onValueChange = { donorViewModel.updatePersonalInfo(formState.name, formState.phoneNumber, it, formState.city) },
            label = { Text("Nhóm máu") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = formState.city,
            onValueChange = { donorViewModel.updatePersonalInfo(formState.name, formState.phoneNumber, formState.bloodGroup, it) },
            label = { Text("Thành phố") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = formState.dateOfBirth,
            onValueChange = {
                donorViewModel.updateBasicInfo(
                    dateOfBirth = it,
                    age = formState.age,
                    gender = formState.gender,
                    willingToDonate = formState.willingToDonate,
                    about = formState.about
                )
            },
            label = { Text("Ngày sinh") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = formState.about,
            onValueChange = {
                donorViewModel.updateBasicInfo(
                    dateOfBirth = formState.dateOfBirth,
                    age = formState.age,
                    gender = formState.gender,
                    willingToDonate = formState.willingToDonate,
                    about = it
                )
            },
            label = { Text("Giới thiệu bản thân") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 Nút cập nhật
        Button(
            onClick = {
                val updatedDonor = Donor(
                    donorId = donorId,
                    name = formState.name,
                    phoneNumber = formState.phoneNumber,
                    bloodGroup = formState.bloodGroup,
                    city = formState.city,
                    dateOfBirth = formState.dateOfBirth,
                    age = formState.age,
                    gender = formState.gender,
                    willingToDonate = formState.willingToDonate,
                    about = formState.about
                )
                donorViewModel.updateDonor(updatedDonor)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text("Cập nhật")
            }
        }

        // 🔹 Hiển thị lỗi hoặc thông báo
        formState.error?.let { error ->
            Text(
                text = "Lỗi: $error",
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (formState.isSubmitSuccess) {
            Text(
                text = "Cập nhật thành công!",
                color = Color(0xFF00C853),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}