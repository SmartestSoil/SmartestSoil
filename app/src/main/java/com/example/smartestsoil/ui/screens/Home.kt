package com.example.smartestsoil.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.smartestsoil.model.SensorData
import com.example.smartestsoil.ui.screens.authentication.AuthenticationButton
import com.example.smartestsoil.ui.theme.SmartestSoilTheme
import com.example.smartestsoil.viewModel.FirebaseAuthViewModel
import com.example.smartestsoil.viewModel.SensorViewModel



@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    SmartestSoilTheme(/*useSystemUiController = false*/) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Home()
        }
    }
}
@Composable
fun Home(sensorViewModel: SensorViewModel = viewModel()) {
    val firebaseAuthViewModel: FirebaseAuthViewModel = viewModel()
   /* Text(text = "this is the home")
    Button(
        onClick = { firebaseAuthViewModel.logout() }
    ) {
        Text(text = "Logout")
    }*/
   SensorList(sensorViewModel.sensordata)
}
@Composable
fun SensorList(sensordata: List<SensorData>) {
    val moistureValue = sensordata.lastOrNull()?.soil_moisture ?: "N/A"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            // Draw the moisture value
            Text(
                text = "$moistureValue%",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}