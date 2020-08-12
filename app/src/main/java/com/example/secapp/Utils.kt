package com.example.secapp

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun showDialog(
    context: Context,
    title: String,
    message: String,
    positiveButton: String,
    positiveAction: (() -> Unit)? = null,
    negativeButton: String? = null,
    negativeAction: (() -> Unit)? = null
    ) {
    AlertDialog.Builder(context).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveButton) { _, _ -> positiveAction?.invoke() }
        setNegativeButton(negativeButton) { _, _ -> negativeAction?.invoke() }
        create()
        show()
    }
}

var statusProvider: Boolean = false