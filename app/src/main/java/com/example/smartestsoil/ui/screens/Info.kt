package com.example.smartestsoil.ui.screens

import android.content.Intent
import android.icu.text.IDNA.Info
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.smartestsoil.R

@Composable
fun Info(navController: NavController) {


    Column(
        modifier = Modifier.fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "About us",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Text(
            text = "Our app is designed for plant enthusiasts to monitor soil moisture and pH levels in real-time. Receive alerts when your plants need attention and make informed decisions about their needs. Keep your plants healthy with ease using our intuitive platform.",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Contact us",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Address"
            )
            Text(
                text = "Yliopistokatu 9, 90570 Oulu",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Contact Email"
            )
            Text(
                text = "your.email@example.com",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "Phone Number"
            )
            Text(
                text = "+1 (123) 456-7890",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Text(
            text = "We care your privacy",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Text(
            text = "Privacy Policy",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(start = 8.dp)
                .clickable(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse("https://www.freeprivacypolicy.com/live/16460a40-5d9f-49b0-a713-ff1e49b18a86"))
                    navController.context.startActivity(intent) })
        )
        Spacer(modifier = Modifier.weight(1f))

    }

}

