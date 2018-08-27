package com.github.joaotfernandes.newsroompaging.db

import androidx.room.TypeConverter
import java.util.Date

class Converters {

    @TypeConverter
    fun fromTimestamp(timestamp: Long) = Date(timestamp)

    @TypeConverter
    fun dateToTimestamp(date: Date) = date.time

}
