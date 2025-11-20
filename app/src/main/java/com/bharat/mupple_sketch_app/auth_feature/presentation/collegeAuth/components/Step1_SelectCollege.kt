package com.bharat.mupple_sketch_app.auth_feature.presentation.collegeAuth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Step1_SelectCollege(
    onBackClicked: () -> Unit,
    onCollegeSelected : () -> Unit
) {

    Scaffold(
        topBar = { Step1_SelectCollegeTopBar(onBackClicked)}
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Step1 Select College")
            Button(
                onClick = onCollegeSelected
            ) {
                Text("Next")
            }

        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step1_SelectCollegeTopBar(
    onBackClicked: () -> Unit
){
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = onBackClicked
            ) {
                Icon(Icons.Default.ArrowBack, null)
            }
        }
    )
}
