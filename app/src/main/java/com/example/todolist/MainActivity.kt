package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        * -> FragmentManager is the class responsible for performing actions on your app's fragments, such as adding,
        *    removing, or replacing them, and adding them to the back stack.
        *
        * -> Support Fragment manager -> Return the FragmentManager for interacting with fragments associated with this activity.
        *
        * -> as Keyword is used for typecasting
        *
        * */
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment

        /*
        * -> NavHostFragment : provides an area within your layout for self-contained navigation to occur.
        * -> Each NavHostFragment has navController
        *
        * -> NavHost :
        *  1. A host is a single context or container for navigation via a navController
        *  2. The NavHost is an empty view whereupon destinations(view) are swapped in and out as a user navigates through your app
        *
        * -> NavController : manages app navigation within a NavHost
        * */
        navController = navHostFragment.navController

        /*
        * -> By calling this method, the title in the action bar will automatically be updated when the destination changes
        * */
        setupActionBarWithNavController(navController)

    }

    /*
    * navController.navigateUp() -> Attempts to navigate up in the navigation hierarchy. Suitable for when the user presses
    * the "Up" button marked with a left (or start)-facing arrow in the upper left (or starting) corner of the app UI.
    *
    * nSupportNavigateUp comes from AppCompatActivity. You should override the method in the same activity where you define
    * your NavHostFragment (probably your MainActivity). You override it so that the NavigationUI can correctly support the
    * up navigation or even the drawer layout menu. AppCompatActivity and NavigationUI are two indepenent components, so you
    * override the method in order to connect the two. Note that if you set a toolbar with the navigation component, i.e.,
    * if you do something similar to
    *
    * */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}