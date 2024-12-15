package com.example.photostorageapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TITLE TEXT," +
                "$COLUMN_DESCRIPTION TEXT," +
                "$COLUMN_TAGS TEXT," +
                "$COLUMN_IMAGE_PATH TEXT" + ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "photos.db"
        const val TABLE_NAME = "photos"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_TAGS = "tags"
        const val COLUMN_IMAGE_PATH = "image_path"
    }

    fun insertPhoto(title: String, description: String, tags: String, imagePath: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_TAGS, tags)
            put(COLUMN_IMAGE_PATH, imagePath)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getPhotos(query: String?): List<Photo> {
        val db = this.readableDatabase
        val photos = mutableListOf<Photo>()

        val selection = if (query != null) {
            "title LIKE ? OR description LIKE ? OR tags LIKE ?"
        } else {
            null
        }

        val selectionArgs = if (query != null) {
            arrayOf("%$query%", "%$query%", "%$query%")
        } else {
            null
        }

        val cursor: Cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)
        while (cursor.moveToNext()) {
            val title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
            val description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
            val tags = cursor.getString(cursor.getColumnIndex(COLUMN_TAGS))
            val imagePath = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PATH))

            // Load the bitmap from the path using the context
            val bitmap = BitmapFactory.decodeFile(imagePath) // Decode bitmap from the image path

            photos.add(Photo(title, description, tags, bitmap))
        }
        cursor.close()
        return photos
    }
}
