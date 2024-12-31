package com.marcohuijskes.runningapp.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class Run(
    var img: Bitmap? = null,
    var timeStamp: Long = 0L, // Timestamp in ms when the run was.
    var avgSpeedInKm: Float = 0f,
    var distanceInMeters: Int = 0,
    var timeInMillis: Long = 0L, // Total runtime in ms.
    var caloriesBurned: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}