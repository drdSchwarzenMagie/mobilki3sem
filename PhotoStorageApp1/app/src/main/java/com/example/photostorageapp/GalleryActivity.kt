package com.example.photostorageapp

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.GridView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GalleryActivity : AppCompatActivity() {

    private lateinit var galleryGridView: GridView
    private lateinit var searchView: SearchView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        galleryGridView = findViewById(R.id.galleryGridView)
        searchView = findViewById(R.id.searchView)
        dbHelper = DatabaseHelper(this)

        loadImages(null)  // Загрузка всех изображений изначально

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                loadImages(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadImages(newText)
                return true
            }
        })

        galleryGridView.setOnItemClickListener { _, _, position, _ ->
            // Получаем фотографию по позиции
            val selectedPhoto = dbHelper.getPhotos(null)[position]
            val bitmap = selectedPhoto.bitmap

            // Создаем Intent для перехода в FullScreenActivity
            val intent = Intent(this, FullScreenActivity::class.java)
            intent.putExtra("imageBitmap", bitmap)
            startActivity(intent)
        }
    }

    private fun loadImages(query: String?) {
        val photos = dbHelper.getPhotos(query)
        if (photos.isEmpty()) {
            Toast.makeText(this, "Фотографии не найдены", Toast.LENGTH_SHORT).show()
        } else {
            val adapter = PhotoAdapter(this, photos) // Создание адаптера
            galleryGridView.adapter = adapter
        }
    }
}
