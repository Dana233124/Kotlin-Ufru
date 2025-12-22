package com.example.urfu.profile.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.urfu.profile.presentation.domain.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.net.URL



@Composable
fun ProfileScreen(
    onEdit: () -> Unit
) {
    val vm: ProfileViewModel = hiltViewModel()
    val profile by vm.profile.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        if (profile.isEmpty()) {
            Button(onClick = onEdit) {
                Text("Создать профиль")
            }
        } else {
            if (profile.avatarUri.isNotBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = profile.avatarUri.takeIf { it.isNotBlank() }
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = profile.name,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(8.dp))

            Button(onClick = {
                Log.d("ResumeButton", "Нажали на кнопку Резюме")

                if (profile.resumeUrl.isBlank()) {
                    Log.e("ResumeButton", "resumeUrl пустой!")
                    return@Button
                }

                Log.d("ResumeButton", "resumeUrl = ${profile.resumeUrl}")

                scope.launch(Dispatchers.IO) {
                    val file = downloadPdf(context, profile.resumeUrl)

                    if (file == null) {
                        Log.e("ResumeButton", "Файл НЕ скачан")
                    } else {
                        Log.d("ResumeButton", "Файл скачан: ${file.absolutePath}")
                        openLocalPdf(context, file, profile.resumeUrl)
                    }
                }
            }) {
                Text("Резюме")
            }


            Spacer(Modifier.height(24.dp))

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Редактировать")
            }
        }
    }
}

fun Profile.isEmpty(): Boolean =
    name.isBlank() && resumeUrl.isBlank() && avatarUri.isBlank()

suspend fun downloadPdf(context: Context, url: String): File? {
    return try {
        Log.d("ResumeDownload", "Пробую скачать PDF: $url")

        val connection = URL(url).openConnection()
        val input = connection.getInputStream()
        val file = File(context.externalCacheDir, "resume.pdf")

        file.outputStream().use { output -> input.copyTo(output) }

        Log.d("ResumeDownload", "PDF скачан: ${file.absolutePath}")
        file
    } catch (e: Exception) {
        Log.e("ResumeDownload", "Ошибка скачивания", e)
        null
    }
}

fun openLocalPdf(context: Context, file: File, originalUrl: String) {
    Log.d("ResumeOpen", "Пробую открыть PDF: ${file.absolutePath}")

    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    try {
        context.startActivity(intent)
        Log.d("ResumeOpen", "Открыт локальный PDF")
    } catch (e: Exception) {
        Log.e("ResumeOpen", "Нет PDF viewer, открываю URL", e)

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(originalUrl))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(browserIntent)
    }
}
