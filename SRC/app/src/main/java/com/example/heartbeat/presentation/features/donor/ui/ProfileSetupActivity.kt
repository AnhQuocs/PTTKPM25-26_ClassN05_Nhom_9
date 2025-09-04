package com.example.heartbeat.presentation.features.donor.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.presentation.features.donor.viewmodel.DonorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSetupActivity : BaseComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

        }
    }
}

@Composable
fun ProfileSetupScreen(
    donorViewModel: DonorViewModel = hiltViewModel()
) {
    val formState by donorViewModel.formState.collectAsState()
    val pagerState = rememberPagerState(
        pageCount = { 3 },
        initialPage = 0
    )

    val context = LocalContext.current

    LaunchedEffect(formState.isSubmitSuccess) {
        if (formState.isSubmitSuccess) {
            Toast.makeText(context, "Submit thành công!", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(formState.currentStep) {
        if (pagerState.currentPage != formState.currentStep) {
            pagerState.animateScrollToPage(formState.currentStep)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        donorViewModel.setStep(pagerState.currentPage)
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = false
    ) { page ->
        when (page) {
            0 -> StepOneScreen(
                formState = formState,
                onNext = { donorViewModel.setStep(1) },
                onUpdate = { name, phoneNumber, bloodGroup, city ->
                    donorViewModel.updatePersonalInfo(name, phoneNumber, bloodGroup, city)
                },
            )
            1 -> StepTwoScreen(
                formState = formState,
                onUpdate = { dateOfBirth, age, gender, willingToDonate, about ->
                    donorViewModel.updateAdditionalInfo(dateOfBirth, age, gender, willingToDonate, about)
                },
                onBack = { donorViewModel.setStep(0) },
                onNext = { donorViewModel.setStep(2) }
            )
            2 -> StepThreeScreen(
                formState = formState,
                onBack = { donorViewModel.setStep(1) },
                onUpdate = { profileAvatar ->
                    donorViewModel.updatePersonalAvatar(profileAvatar)
                },
                onSubmit = { donorViewModel.submitDonor() }
            )
        }
    }
}