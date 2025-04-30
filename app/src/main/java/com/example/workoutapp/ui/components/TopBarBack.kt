package com.example.workoutapp.ui.components

// TopBarBack.kt is used in nav scaffold, and allows the user to go back to a previous screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.workoutapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarBack(topBarText: String, navController: NavHostController) {
    TopAppBar(
        title = {
            // Shows the top bar text
            Text(text = topBarText)
        },
        // Navigate to the previous screen in the backstack
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        }
    )
}