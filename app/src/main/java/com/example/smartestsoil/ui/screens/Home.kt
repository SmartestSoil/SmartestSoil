package com.example.smartestsoil.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

@Composable
fun FeedScreen(navController: NavController, url: String = "https://www.k-rauta.fi/kategoria/piha/puutarha/kasvit") {
    AndroidView(factory = { context ->
        WebView(context).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl(url)
            }
        })
}
