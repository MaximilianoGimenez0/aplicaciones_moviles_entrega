package com.example.helloworld


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.helloworld.ui.theme.HelloWorldTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloWorldTheme {
                NavigationManager()
            }
        }
    }
}

@Composable
fun NavigationManager() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { NavigationBar(navController) }) { padding ->
        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(padding)
        ) {
            composable("inicio") { HomeScreen(Modifier.fillMaxSize(), navController) }
            composable(
                "detail/{itemJson}",
                arguments = listOf(navArgument("itemJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val itemJson = backStackEntry.arguments?.getString("itemJson")

                val item = itemJson.let {
                    val decoded = URLDecoder.decode(it, "UTF-8")
                    Json.decodeFromString<ItemDetail>(decoded)
                }

                DetailScreen(item, {
                    navController.navigate("inicio") {
                        popUpTo("inicio") { inclusive = true }
                        launchSingleTop = true
                    }
                })
            }
            composable("google") { GoogleScreen(Modifier.fillMaxSize()) }
            composable("youtube") { YouTubeScreen(Modifier.fillMaxSize()) }
            composable("descubri2") { Descubri2(Modifier.fillMaxSize()) }
        }
    }
}


@Composable
fun NavigationBar(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = currentRoute == "inicio",
            onClick = {
                navController.navigate("inicio") {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            })
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Inicio") },
            label = { Text("Google") },
            selected = currentRoute == "google",
            onClick = { navController.navigate("google") { launchSingleTop = true } })
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Inicio") },
            label = { Text("YouTube") },
            selected = currentRoute == "youtube",
            onClick = { navController.navigate("youtube") { launchSingleTop = true } })
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, contentDescription = "Inicio") },
            label = { Text("Descubri-2") },
            selected = currentRoute == "descubri2",
            onClick = { navController.navigate("descubri2") { launchSingleTop = true } })
    }
}


@Composable
fun HomeScreen(modifier: Modifier, navController: NavController) {

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    val isTablet = when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.EXPANDED -> true   // tablet o pantalla grande
        else -> false                           // telefono o pantalla chica
    }

    if (isTablet) {
        ExpandedHomeView(modifier)
    } else {
        CompactHomeView(modifier, navController)
    }


}

@Composable
fun ExpandedHomeView(modifier: Modifier) {

    val navController = rememberNavController()


    val google = ItemDetail(1, "Google", "El mejor buscador de internet")
    val googleJson = Json.encodeToString(google)
    val googleEncoded = java.net.URLEncoder.encode(googleJson, "UTF-8")

    val youtube = ItemDetail(1, "Youtube", "Acá vas a encontrar los mejores videos")
    val youtubeson = Json.encodeToString(youtube)
    val youtubeEncoded = java.net.URLEncoder.encode(youtubeson, "UTF-8")

    val descubri2 = ItemDetail(1, "Descubri-2", "Un lugar para descubrir cosas geniales!")
    val descubri2Json = Json.encodeToString(descubri2)
    val descubri2Encoded = java.net.URLEncoder.encode(descubri2Json, "UTF-8")

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF2196F3), Color(0xFF0D47A1))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "¡Bienvenido! explorá: ",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )


            Button(
                modifier = Modifier.width(160.dp),
                onClick = {
                    navController.navigate("detail/$googleEncoded") {
                        launchSingleTop = true
                    }
                }) {
                Text(
                    text = "Google", color = Color.Black.copy(alpha = 0.9f), fontSize = 18.sp
                )
            }

            Button(
                modifier = Modifier.width(160.dp),
                onClick = {
                    navController.navigate("detail/$youtubeEncoded") {
                        launchSingleTop = true
                    }
                }) {
                Text(
                    text = "YouTube", color = Color.Black.copy(alpha = 0.9f), fontSize = 18.sp
                )
            }

            Button(
                modifier = Modifier.width(160.dp),
                onClick = {
                    navController.navigate("detail/$descubri2Encoded") {
                        launchSingleTop = true
                    }
                }) {
                Text(
                    text = "Descubri-2", color = Color.Black.copy(alpha = 0.9f), fontSize = 18.sp
                )
            }

        }
        NavHost(
            navController = navController,
            startDestination = "default"
        ) {
            composable("default") {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) { Text("Detalles") }

            }
            composable(
                "detail/{itemJson}",
                arguments = listOf(navArgument("itemJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val itemJson = backStackEntry.arguments?.getString("itemJson")

                val item = itemJson.let {
                    val decoded = URLDecoder.decode(it, "UTF-8")
                    Json.decodeFromString<ItemDetail>(decoded)
                }

                DetailScreen(item, {
                    navController.navigate("inicio") {
                        popUpTo("inicio") { inclusive = true }
                        launchSingleTop = true
                    }
                })
            }
        }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

        }
    }
}

@Composable
fun GoogleScreen(modifier: Modifier) {
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Browser("https://www.google.com")
    }
}

@Composable
fun YouTubeScreen(modifier: Modifier) {
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Browser("https://www.youtube.com")
    }
}

@Composable
fun Descubri2(modifier: Modifier) {
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Browser("https://maximilianogimenez0.github.io/apod-descubri-2/")
    }
}

@Composable
fun Browser(url: String) {
    AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
        WebView(context).apply {
            webViewClient = WebViewClient() // evita abrir navegador externo

            // Habilitar JavaScript
            settings.javaScriptEnabled = true

            // Habilitar almacenamiento local (localStorage, sessionStorage, IndexedDB)
            settings.domStorageEnabled = true

            loadUrl(url)
        }
    })
}

@Composable
fun DetailScreen(item: ItemDetail, onBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Text(" ${item.title}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(20.dp))

            Text(" ${item.description}", style = MaterialTheme.typography.titleMedium)

        }
    }
}

@Composable
fun CompactHomeView(modifier: Modifier, navController: NavController) {
    val google = ItemDetail(1, "Google", "El mejor buscador de internet")
    val googleJson = Json.encodeToString(google)
    val googleEncoded = java.net.URLEncoder.encode(googleJson, "UTF-8")

    val youtube = ItemDetail(1, "Youtube", "Acá vas a encontrar los mejores videos")
    val youtubeson = Json.encodeToString(youtube)
    val youtubeEncoded = java.net.URLEncoder.encode(youtubeson, "UTF-8")

    val descubri2 = ItemDetail(1, "Descubri-2", "Un lugar para descubrir cosas geniales!")
    val descubri2Json = Json.encodeToString(descubri2)
    val descubri2Encoded = java.net.URLEncoder.encode(descubri2Json, "UTF-8")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF2196F3), Color(0xFF0D47A1))
                )
            ), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Text(
                text = "¡Bienvenido! explorá: ",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )


            Button(
                modifier = Modifier.width(160.dp),
                onClick = {
                    navController.navigate("detail/$googleEncoded") {
                        launchSingleTop = true
                    }
                }) {
                Text(
                    text = "Google", color = Color.Black.copy(alpha = 0.9f), fontSize = 18.sp
                )
            }

            Button(
                modifier = Modifier.width(160.dp),
                onClick = {
                    navController.navigate("detail/$youtubeEncoded") {
                        launchSingleTop = true
                    }
                }) {
                Text(
                    text = "YouTube", color = Color.Black.copy(alpha = 0.9f), fontSize = 18.sp
                )
            }

            Button(
                modifier = Modifier.width(160.dp),
                onClick = {
                    navController.navigate("detail/$descubri2Encoded") {
                        launchSingleTop = true
                    }
                }) {
                Text(
                    text = "Descubri-2", color = Color.Black.copy(alpha = 0.9f), fontSize = 18.sp
                )
            }

        }
    }
}

@Serializable
data class ItemDetail(val id: Int, val title: String, val description: String)