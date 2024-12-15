package com.example.photostorageapp

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class PhotoAdapter(private val context: Context, private val photos: List<Photo>) : BaseAdapter() {

    override fun getCount(): Int = photos.size

    override fun getItem(position: Int): Photo = photos[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView: ImageView
        if (convertView == null) {
            imageView = LayoutInflater.from(context).inflate(R.layout.photo_item, parent, false) as ImageView
            imageView.layoutParams = ViewGroup.LayoutParams(300, 300) // Задайте размер изображения
        } else {
            imageView = convertView as ImageView
        }

        val photo = getItem(position)
        imageView.setImageBitmap(photo.bitmap)

        return imageView
    }
}
