package com.example.urfu.profile.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.URLUtil

fun openPdf(context: Context, url: String) {
    if (!URLUtil.isValidUrl(url)) return

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(Uri.parse(url), "application/pdf")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(intent)
}
