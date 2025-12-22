package com.example.urfu.profile.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import java.io.File

@Composable
fun EditProfileScreen(
    onDone: () -> Unit
) {
    val vm: EditProfileViewModel = hiltViewModel()
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var resumeUrl by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf("") }

    var showSourceDialog by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }


    val cameraFile = File(context.externalCacheDir, "camera_photo.jpg")
    val cameraUri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        cameraFile
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) avatarUri = cameraUri.toString()
    }


    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { avatarUri = it.toString() }
    }

    fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                vm.save(name, resumeUrl, avatarUri, onDone)
            }) {
                Icon(Icons.Default.Check, contentDescription = "Сохранить")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Аватар
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clickable { showSourceDialog = true },
                contentAlignment = Alignment.Center
            ) {
                if (avatarUri.isNotBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(avatarUri),
                        contentDescription = "Аватар",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Выбрать фото",
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = resumeUrl,
                onValueChange = { resumeUrl = it },
                label = { Text("URL резюме") },
                modifier = Modifier.fillMaxWidth()
            )

            if (showSourceDialog) {
                showImageSourceDialog(
                    onGallery = {
                        showSourceDialog = false
                        showPermissionDialog = true
                    },
                    onCamera = {
                        showSourceDialog = false
                        cameraLauncher.launch(cameraUri)
                    }
                )
            }

            if (showPermissionDialog) {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text("Доступ к фото") },
                    text = {
                        Text("Приложению нужен доступ к вашим фото, чтобы выбрать аватар. Разрешить?")
                    },
                    confirmButton = {
                        androidx.compose.material3.Text(
                            text = "Да",
                            modifier = Modifier
                                .clickable {
                                    showPermissionDialog = false
                                    openGallery()
                                }
                                .padding(8.dp)
                        )
                    },
                    dismissButton = {
                        androidx.compose.material3.Text(
                            text = "Нет",
                            modifier = Modifier
                                .clickable {
                                    showPermissionDialog = false
                                    onDone()
                                }
                                .padding(8.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun showImageSourceDialog(
    onGallery: () -> Unit,
    onCamera: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Выбрать источник") },
        text = {
            Column {
                Text(
                    "Галерея",
                    modifier = Modifier
                        .clickable { onGallery() }
                        .padding(vertical = 8.dp)
                )
                Text(
                    "Камера",
                    modifier = Modifier
                        .clickable { onCamera() }
                        .padding(vertical = 8.dp)
                )
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}
