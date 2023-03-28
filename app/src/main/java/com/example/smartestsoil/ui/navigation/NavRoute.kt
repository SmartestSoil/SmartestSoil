package com.example.smartestsoil.ui.navigation

sealed class NavRoute(val path: String) {

    object Authentication: NavRoute("authentication")

    object Home: NavRoute("home")
    object Account: NavRoute("account")
    object Detail: NavRoute("detail")
    object Info: NavRoute("info")
    object Locations: NavRoute("locations")




    // build navigation path (for screen navigation)
    fun withArgs(vararg args: String): String {
        return buildString {
            append(path)
            args.forEach{ arg ->
                append("/$arg")
            }
        }
    }

    // build and setup route format (in navigation graph)
    fun withArgsFormat(vararg args: String) : String {
        return buildString {
            append(path)
            args.forEach{ arg ->
                append("/{$arg}")
            }
        }
    }
}