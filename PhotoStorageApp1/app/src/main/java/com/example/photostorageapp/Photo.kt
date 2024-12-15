package com.example.photostorageapp

import android.graphics.Bitmap

data class Photo(
    val title: String,
    val description: String,
    val tags: String,
    val bitmap: Bitmap // Store bitmap here for display
)
