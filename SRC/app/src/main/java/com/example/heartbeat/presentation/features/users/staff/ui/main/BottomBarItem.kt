package com.example.heartbeat.presentation.features.users.staff.ui.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.heartbeat.R
import com.example.heartbeat.presentation.components.TabEnum

enum class BottomBarItem(
    @DrawableRes override val iconRes: Int,
    @StringRes override val labelRes: Int
) : TabEnum {
    Home(R.drawable.ic_home, R.string.home),
    Create(R.drawable.ic_add, R.string.create),
    Calendar(R.drawable.ic_calendar, R.string.calendar),
    Setting(R.drawable.ic_setting, R.string.setting)
}