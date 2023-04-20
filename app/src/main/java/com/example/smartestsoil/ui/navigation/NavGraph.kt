package com.example.smartestsoil.ui.navigation



import com.example.smartestsoil.ui.screens.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartestsoil.model.AuthenticationMode
import com.example.smartestsoil.ui.screens.*
import com.example.smartestsoil.ui.screens.authentication.Authentication
import com.example.smartestsoil.ui.screens.authentication.AuthenticationButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

@Composable
fun NavGraph(modifier: Modifier = Modifier, navController: NavHostController) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavRoute.Authentication.path
    ) {
        addAuthentication(navController, this)

        addPlantListView(navController, this)
        addHome(navController, this)
        addAccount(navController, this)
        addDetails(navController, this)
        addInfo(navController,this)

    }
}

private fun addAuthentication(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.composable(route = NavRoute.Authentication.path) {
        Authentication(navController = navController)
    }
}
private fun addPlantListView(
    navController: NavController,
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.composable(route = NavRoute.PlantListView.path) {

        PlantListView(db= FirebaseFirestore.getInstance(), navController = navController, viewModel())
    }
}
private fun addHome(
    navController: NavController,
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.composable(route = NavRoute.Home.path) {

        Home(navController = navController)
    }
}
private fun addDetails(
    navController: NavController,
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.composable(route = NavRoute.Detail.path) {

        Details(navController = navController )
    }
}
private fun addAccount(
    navController: NavController,
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.composable(route = NavRoute.Account.path) {

        val userEmail=Firebase.auth.currentUser?.email?:""
        Account(navController = navController, userEmail = userEmail )
    }
}
private fun addInfo(
    navController: NavController,
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.composable(route = NavRoute.Info.path) {

        Info(navController = navController, )
    }
}
/*fun popUpToHome(navController: NavHostController) {
    navController.popBackStack(NavRoute.Home.path, inclusive = false)
}*/


