package com.example.photostorageapp

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class FullScreenActivity : AppCompatActivity() {

    private lateinit var fullScreenImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)

        fullScreenImageView = findViewById(R.id.fullScreenImageView)

        // Получаем изображение, переданное через Intent
        val bitmap = intent.getParcelableExtra<Bitmap>("imageBitmap")
        fullScreenImageView.setImageBitmap(bitmap)
    }
}
