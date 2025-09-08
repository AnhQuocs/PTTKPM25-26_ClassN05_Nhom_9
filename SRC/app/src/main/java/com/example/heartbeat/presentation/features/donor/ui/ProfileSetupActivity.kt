package com.example.heartbeat.presentation.features.donor.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heartbeat.BaseComponentActivity
import com.example.heartbeat.R
import com.example.heartbeat.presentation.components.AppLineGrey
import com.example.heartbeat.presentation.features.donor.viewmodel.DonorViewModel
import com.example.heartbeat.presentation.features.main.MainActivity
import com.example.heartbeat.presentation.features.system.province.viewmodel.ProvinceViewModel
import com.example.heartbeat.ui.dimens.AppShape
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSetupActivity : BaseComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ProfileSetupScreen()
        }
    }
}

@Composable
fun ProfileSetupScreen(
    donorViewModel: DonorViewModel = hiltViewModel(),
    provinceViewModel: ProvinceViewModel = hiltViewModel()
) {
    val formState by donorViewModel.formState.collectAsState()
    val provinces by provinceViewModel.provinces.collectAsState()
    val provinceNames = provinces.map { it.name }

    val bloodList = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    val genderList = listOf(
        stringResource(id = R.string.male),
        stringResource(id = R.string.female)
    )

    val pagerState = rememberPagerState(
        pageCount = { 3 },
        initialPage = 0
    )

    val context = LocalContext.current
    val activity = LocalActivity.current

    val submittedSuccess = stringResource(id = R.string.submitted_success)

    var showExitDialog by remember { mutableStateOf(false) }
    BackHandler(enabled = true) {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text(text = stringResource(id = R.string.exit_confirmation)) },
            text = { Text(text = stringResource(id = R.string.exit_message)) },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    activity?.finish()
                }) {
                    Text(text = stringResource(id = R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }

    LaunchedEffect(formState.isSubmitSuccess, formState.error) {

        if (formState.isSubmitSuccess) {
            Toast.makeText(context, submittedSuccess, Toast.LENGTH_SHORT).show()

            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        formState.error?.let { errorMsg ->
            Log.d("ProfileSetupActivity", errorMsg)
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            donorViewModel.clearError()
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

    val isButtonEnabled = when (pagerState.currentPage) {
        0 -> formState.isStepOneValid(provinceNames, bloodList)
        1 -> formState.isStepTwoValid(genderList)
        2 -> formState.isStepThreeValid()
        else -> false
    }

    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(1),
        snapAnimationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing)
    )

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Scaffold(
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.PaddingM)
                        .padding(bottom = Dimens.PaddingM),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (pagerState.currentPage) {
                        0 -> {
                            Button(
                                onClick = { donorViewModel.setStep(1) },
                                enabled = isButtonEnabled,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isButtonEnabled) BloodRed else BloodRed.copy(alpha = 0.2f)
                                ),
                                shape = RoundedCornerShape(AppShape.MediumShape),
                                modifier = Modifier
                                    .height(Dimens.HeightDefault)
                                    .fillMaxWidth(),
                            ) {
                                Text(
                                    text = stringResource(id = R.string.next),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        1 -> {
                            OutlinedButton(
                                onClick = { donorViewModel.setStep(0) },
                                border = BorderStroke(1.dp, BloodRed),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = BloodRed,
                                    containerColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(AppShape.MediumShape),
                                modifier = Modifier
                                    .fillMaxWidth(0.3f)
                                    .height(Dimens.HeightDefault)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.back),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                            Button(
                                onClick = { donorViewModel.setStep(2) },
                                enabled = isButtonEnabled,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isButtonEnabled) BloodRed else BloodRed.copy(alpha = 0.2f)
                                ),
                                shape = RoundedCornerShape(AppShape.MediumShape),
                                modifier = Modifier
                                    .height(Dimens.HeightDefault)
                                    .fillMaxWidth(0.5f),
                            ) {
                                Text(
                                    text = stringResource(id = R.string.next),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        2 -> {
                            OutlinedButton(
                                onClick = { donorViewModel.setStep(1) },
                                border = BorderStroke(1.dp, BloodRed),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = BloodRed,
                                    containerColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(AppShape.MediumShape),
                                modifier = Modifier
                                    .fillMaxWidth(0.3f)
                                    .height(Dimens.HeightDefault)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.back),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                            Button(
                                onClick = { donorViewModel.submitDonor(context = context) },
                                enabled = isButtonEnabled,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isButtonEnabled) BloodRed else BloodRed.copy(alpha = 0.2f)
                                ),
                                shape = RoundedCornerShape(AppShape.MediumShape),
                                modifier = Modifier
                                    .height(Dimens.HeightDefault)
                                    .fillMaxWidth(0.5f),
                            ) {
                                Text(
                                    text = stringResource(id = R.string.submit),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .padding(paddingValues)
                    .padding(top = Dimens.PaddingL),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.profile_setup),
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(AppSpacing.Large))

                Text(
                    text = stringResource(id = R.string.profile_setup_desc),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(AppSpacing.Large))

                AppLineGrey()

                Spacer(modifier = Modifier.height(AppSpacing.Large))

                Box(
                    modifier = Modifier
                        .size(Dimens.SizeXXLPlus)
                        .clip(CircleShape)
                        .background(color = BloodRed.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.PersonOutline,
                        contentDescription = null,
                        tint = BloodRed,
                        modifier = Modifier
                            .size(Dimens.SizeL)
                    )
                }

                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                when(pagerState.currentPage) {
                    0 -> ProfileSetupTitle(title = stringResource(id = R.string.personal_info))
                    1 -> ProfileSetupTitle(title = stringResource(id = R.string.basic_info))
                    2 -> ProfileSetupTitle(title = stringResource(id = R.string.upload_your_image))
                }

                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                HorizontalPager(
                    state = pagerState,
                    beyondViewportPageCount = 1,
                    userScrollEnabled = false,
                    flingBehavior = fling,
                    modifier = Modifier
                        .fillMaxWidth()
                ) { page ->
                    when (page) {
                        0 -> StepOneScreen(
                            formState = formState,
                            onUpdate = { name, phoneNumber, bloodGroup, city ->
                                donorViewModel.updatePersonalInfo(name, phoneNumber, bloodGroup, city)
                            }
                        )

                        1 -> StepTwoScreen(
                            formState = formState,
                            onUpdate = { dateOfBirth, age, gender, willingToDonate, about ->
                                donorViewModel.updateBasicInfo(
                                    dateOfBirth,
                                    age,
                                    gender,
                                    willingToDonate,
                                    about
                                )
                            }
                        )

                        2 -> StepThreeScreen(
                            onSelectAvatar = { uri -> donorViewModel.setLocalAvatar(uri) }
                        )
                    }
                }
            }
        }
    }

    if(formState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.2f))
                .pointerInput(Unit) { },
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = BloodRed)
        }
    }
}

@Composable
fun ProfileSetupTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
        color = Color.Black,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}