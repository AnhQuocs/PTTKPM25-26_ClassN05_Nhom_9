package com.example.heartbeat.presentation.features.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.heartbeat.ui.dimens.Dimens

@Composable
fun MainScreen() {
    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
            .padding(Dimens.PaddingM)
    ) {
        Text(
            "Welcome to Heart Beat"
        )
    }
}