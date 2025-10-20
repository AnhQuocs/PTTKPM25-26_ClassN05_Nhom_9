package com.example.heartbeat.presentation.features.main.home

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.heartbeat.R
import com.example.heartbeat.domain.entity.users.AuthUser
import com.example.heartbeat.domain.entity.users.DonorAvatar
import com.example.heartbeat.presentation.features.users.donor.viewmodel.DonorFormState
import com.example.heartbeat.ui.dimens.AppSpacing
import com.example.heartbeat.ui.dimens.Dimens
import com.example.heartbeat.ui.theme.Green500

fun base64ToImageBitmap(base64: String): ImageBitmap? {
    return try {
        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}

@Composable
fun UserTopBar(avatar: DonorAvatar?, user: AuthUser, cityName: String) {
    val imgBitmap = remember(avatar?.avatarUrl) {
        avatar?.avatarUrl?.let { base64ToImageBitmap(it) }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.PaddingM)
            .padding(horizontal = Dimens.PaddingM),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (imgBitmap != null) {
            Image(
                bitmap = imgBitmap,
                contentDescription = null,
                modifier = Modifier
                    .size(Dimens.SizeXXL + 8.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_default_avatar),
                contentDescription = null,
                modifier = Modifier
                    .size(Dimens.SizeXXL + 8.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(AppSpacing.Medium))

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = user.username ?: "User name",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color.Black,
                modifier = Modifier.padding(start = Dimens.PaddingXS)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Green500,
                    modifier = Modifier.size(Dimens.SizeSM)
                )

                Spacer(modifier = Modifier.width(AppSpacing.Small))

                Text(
                    text = cityName,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                    color = Green500
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.ic_notification),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.Black),
            modifier = Modifier.size(Dimens.SizeML)
        )
    }
}