package com.example.photostorageapp

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var tagsEditText: EditText

    private val REQUEST_IMAGE_CAPTURE = 1
    private var currentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        titleEditText = findViewById(R.id.titleEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        tagsEditText = findViewById(R.id.tagsEditText)

        findViewById<Button>(R.id.takePictureButton).setOnClickListener {
            dispatchTakePictureIntent()
        }

        findViewById<Button>(R.id.saveButton).setOnClickListener {
            saveImageMetadata()
        }
        findViewById<Button>(R.id.openGalleryButton).setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }

    }

    private fun dispatchTakePictureIntent() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                imageView.setImageBitmap(imageBitmap)
                saveImage(imageBitmap)
            } else {
                Log.e("PhotoStorage", "Ошибка: изображение не получено")
            }
        }
    }


    private fun saveImage(bitmap: Bitmap) {
        val file = File(filesDir, "image_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        currentPhotoPath = file.absolutePath
    }

    private fun saveImageMetadata() {
        val title = titleEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val tags = tagsEditText.text.toString()

        if (currentPhotoPath != null) {
            val dbHelper = DatabaseHelper(this)
            dbHelper.insertPhoto(title, description, tags, currentPhotoPath!!)

            Log.d("PhotoStorage", "Сохранено: $title, $description, $tags, $currentPhotoPath")
        }
    }
}