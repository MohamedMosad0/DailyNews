package com.mohamed.dailynews.ui.screens.maps

import android.Manifest
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mohamed.dailynews.R
import com.mohamed.dailynews.utils.isGpsEnabled


///1 Ask for permssion
///2 Check if gps is enabled
///3 Get location
///4 show location on google maps
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreenWrapper() {
    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    val context = LocalContext.current
    var isEnabled by remember { mutableStateOf(isGpsEnabled(context)) }

    DisposableEffect(Unit) {
        locationPermissionState.launchMultiplePermissionRequest()
        onDispose { }
    }

    Scaffold() { contentPadding ->
        Column() {
            when {
                locationPermissionState.shouldShowRationale -> {
                    Text(text = stringResource(id = R.string.location_permission_rationale))
                    Button(onClick = {
                        locationPermissionState.launchMultiplePermissionRequest()
                    }) {
                        Text(text = stringResource(id = R.string.request))
                    }
                }

                locationPermissionState.allPermissionsGranted -> {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (isEnabled) {
                            GoogleMapsView()
                        } else {
                            Text(text = stringResource(id = R.string.gps_disabled))
                        }


                        if (!isEnabled) {
                            Button(onClick = {
                                isEnabled = isGpsEnabled(context)
                            }) {
                                Text(text = stringResource(id = R.string.refresh_status))
                            }
                        }

                    }
                }

                else -> {
                    Text(text = stringResource(id = R.string.location_permission_denied))
                    Button(onClick = {
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        context.startActivity(intent)
                    }) {
                        Text(text = stringResource(id = R.string.open_settings))
                    }
                }
            }
        }
    }
}