package com.example.urfu.profile.presentation


import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.util.Calendar

@Composable
fun EditProfileScreen(
    onDone: () -> Unit
) {
    val vm: EditProfileViewModel = hiltViewModel()
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var resumeUrl by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf("") }

    var favoriteTime by remember { mutableStateOf("") }
    var timeError by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

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
        Log.d("DEBUG", "Camera result: success=$success")
        if (success) avatarUri = cameraUri.toString()
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        Log.d("DEBUG", "Gallery result: uri=$uri")
        uri?.let { avatarUri = it.toString() }
    }

    fun openGallery() {
        Log.d("DEBUG", "Opening gallery")
        galleryLauncher.launch("image/*")
    }

    val isSaveEnabled = !timeError && favoriteTime.isNotBlank()

    Scaffold(
        floatingActionButton = {
            Box(
                modifier = Modifier.alpha(if (isSaveEnabled) 1f else 0.4f)
            ) {
                FloatingActionButton(
                    onClick = {
                        Log.d(
                            "DEBUG",
                            "FAB clicked. isSaveEnabled=$isSaveEnabled time=$favoriteTime"
                        )

                        if (isSaveEnabled) {
                            vm.save(name, resumeUrl, avatarUri, onDone)
                            scheduleNotification(context, name, favoriteTime)
                        }
                    }
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Сохранить")
                }
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
                    .clickable {
                        Log.d("DEBUG", "Avatar box clicked, showing source dialog")
                        showSourceDialog = true
                    },
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

            Spacer(Modifier.height(12.dp))

            // Время любимой пары
            OutlinedTextField(
                value = favoriteTime,
                onValueChange = {
                    favoriteTime = it
                    timeError = !isValidTime(it)
                    Log.d("DEBUG", "Time changed: $it valid=${!timeError}")
                },
                label = { Text("Время любимой пары (HH:mm)") },
                isError = timeError,
                trailingIcon = {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Выбрать время",
                        modifier = Modifier.clickable {
                            Log.d("DEBUG", "Opening TimePicker")
                            showTimePicker = true
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            if (timeError) {
                Text("Введите время в формате HH:mm", color = Color.Red)
            }

            if (showTimePicker) {
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        val formatted = String.format("%02d:%02d", hour, minute)
                        Log.d("DEBUG", "TimePicker selected: $formatted")
                        favoriteTime = formatted
                        timeError = false
                    },
                    12, 0, true
                ).show()
                showTimePicker = false
            }

            // Диалоги выбора фото
            if (showSourceDialog) {
                showImageSourceDialog(
                    onGallery = {
                        Log.d("DEBUG", "Gallery selected in dialog")
                        showSourceDialog = false
                        showPermissionDialog = true
                    },
                    onCamera = {
                        Log.d("DEBUG", "Camera selected in dialog")
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
                        Text(
                            text = "Да",
                            modifier = Modifier
                                .clickable {
                                    Log.d("DEBUG", "Permission dialog: Yes")
                                    showPermissionDialog = false
                                    openGallery()
                                }
                                .padding(8.dp)
                        )
                    },
                    dismissButton = {
                        Text(
                            text = "Нет",
                            modifier = Modifier
                                .clickable {
                                    Log.d("DEBUG", "Permission dialog: No")
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

fun isValidTime(text: String): Boolean {
    return Regex("^([01]\\d|2[0-3]):[0-5]\\d$").matches(text)
}

fun scheduleNotification(context: Context, name: String, time: String) {
    Log.d("DEBUG", "scheduleNotification called with time=$time")

    val parts = time.split(":")
    if (parts.size != 2) {
        Log.e("DEBUG", "Invalid time format: $time")
        return
    }

    val hour = parts[0].toIntOrNull()
    val minute = parts[1].toIntOrNull()

    if (hour == null || minute == null) {
        Log.e("DEBUG", "Cannot parse time: $time")
        return
    }

    Log.d("DEBUG", "Parsed hour=$hour minute=$minute")

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)

        if (before(Calendar.getInstance())) {
            add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    Log.d("DEBUG", "Alarm scheduled for: ${calendar.time}")

    val intent = Intent(context, PairReceiver::class.java).apply {
        putExtra("name", name)
    }

    val pending = PendingIntent.getBroadcast(
        context,
        1001,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Гарантированное срабатывание
    alarm.setAlarmClock(
        AlarmManager.AlarmClockInfo(calendar.timeInMillis, pending),
        pending
    )

    Log.d("DEBUG", "Alarm setAlarmClock called")
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
