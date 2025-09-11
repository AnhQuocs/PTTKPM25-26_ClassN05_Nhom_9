package com.example.heartbeat.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.heartbeat.R
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.BloodRed
import com.example.heartbeat.ui.theme.GraySecondary

enum class TabItem(@DrawableRes val iconsRes: Int, @StringRes val label: Int) {
    Home(R.drawable.ic_home, R.string.home),
    Search(R.drawable.ic_search, R.string.search),
    Setting(R.drawable.ic_setting, R.string.setting)
}

@Composable
fun BottomAppBar(
    currentIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = TabItem.entries.toTypedArray()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
    ) {
        Surface(
            shadowElevation = 8.dp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.HeightML)
        ) {
            NavigationBar(
                containerColor = Color.White,
                modifier = Modifier
                    .height(Dimens.HeightML)
            ) {
                tabs.forEachIndexed { index, tab ->
                    val selected = currentIndex == index

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = Dimens.PaddingSM, top = Dimens.PaddingS)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onTabSelected(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = tab.iconsRes),
                                contentDescription = null,
                                modifier = Modifier.size(Dimens.SizeML),
                                colorFilter = if(selected) ColorFilter.tint(BloodRed) else ColorFilter.tint(GraySecondary)
                            )

                            Text(
                                text = stringResource(id = tab.label),
                                style = MaterialTheme.typography.labelSmall,
                                color = if(selected) BloodRed else GraySecondary
                            )
                        }
                    }
                }
            }
        }
    }
}