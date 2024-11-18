package com.demo.recipelist.ui.screen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.demo.recipelist.R
import com.demo.recipelist.RecipeListTopAppBar
import com.demo.recipelist.ui.AppViewModelProvider
import com.demo.recipelist.ui.navigation.NavigationDestination
import com.demo.recipelist.ui.theme.RecipeListTheme
import kotlinx.coroutines.launch

@Composable
fun PermissionScreen(
    viewModel: PermissionViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val tag = "TestTag"
    val context = LocalContext.current
    val cameraState = viewModel.cameraState

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher. You can use either a val, as shown in this snippet,
    // or a lateinit var in your onAttach() or onCreate() method.
    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                /*
                    If permission is granted. Continue the action or workflow in the app.
                    If Not, explain to the user that the feature is unavailable because the
                    feature requires a permission that the user has denied. At the
                    same time, respect the user's decision. Don't link to system
                    settings in an effort to convince the user to change their
                    decision.
                */
                if (isGranted) {
                    viewModel.onPermissionChange(Manifest.permission.CAMERA, isGranted)
                } else {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Camera currently disabled due to denied permission.")
                    }
                }
            }
        )

    var showExplanationDialogForCameraPermission by remember { mutableStateOf(false) }
    if (showExplanationDialogForCameraPermission) {
        CameraExplanationDialog(
            onConfirm = {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                showExplanationDialogForCameraPermission = false
                Log.d(tag, "Camera Permission Explanation Dialog: click confirm")
            },
            onDismiss = { showExplanationDialogForCameraPermission = false },
        )
        Log.d(tag, "Camera Permission Explanation Dialog: Show")
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            RecipeListTopAppBar(
                title = stringResource(R.string.test_screen_title),
                canNavigateBack = false,
            )
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            Button(onClick = {
                if (cameraState.hasCameraDevices) {
                    Log.d(tag, "Ask for camera permission")
                    when {
                        cameraState.hasCameraAccess -> {
                            /* TODO: go to camera screen */
                            Log.d(tag, "cameraState.hasCameraAccess: ${cameraState.hasCameraAccess}")
                        }
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            context.getActivity(), Manifest.permission.CAMERA) -> {
                            /*
                                向使用者解釋為什麼應用程式需要此權限才能使特定功能按預期運行，以及如果被拒絕，哪些功能將被停用。
                                在此 UI 中，包含一個「取消」或「不，謝謝」按鈕，讓使用者無需授予權限即可繼續使用應用程式。
                            */
                            showExplanationDialogForCameraPermission = true
                            Log.d(tag, "showExplanationDialogForCameraPermission")
                        }
                        else -> {
                            // You can directly ask for the permission.
                            // The registered ActivityResultCallback gets the result of this request.
                            cameraPermissionLauncher.launch(
                                Manifest.permission.CAMERA
                            )
                            Log.d(tag, "else")
                        }
                    }
                } else {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("No camera detected. This feature cannot be used.")
                    }
                }
            }) {
                Text(text = "Request Camera Permission")
            }
        }
    }
}


@Composable
fun CameraExplanationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Camera access") },
        text = { Text("Hello! This is CameraExplanationDialog Test") },
        icon = {
            Icon(
                Icons.Filled.CameraAlt,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Continue")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    )
}

fun Context.getActivity(): Activity {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}

@Preview(apiLevel = 33, showBackground = true)
@Composable
fun TestBodyPreview() {
    RecipeListTheme {
        CameraExplanationDialog(onConfirm = {}, onDismiss = {})
        // TestBody(LocalContext.current, hasCamera = { _ -> false })
    }
}