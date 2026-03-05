package com.weatherapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun TempColumn(label: String, temp: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$temp°",
            color = Color.White,
            fontSize = 18.sp
        )

        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}