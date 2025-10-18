package com.desafiodevspace.metamanager.data.local

import androidx.room.TypeConverter
import com.desafiodevspace.metamanager.data.model.DailyTask
import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(it, 0) }
    }

    @TypeConverter
    fun toTimestamp(timestamp: Timestamp?): Long? {
        return timestamp?.seconds
    }

    @TypeConverter
    fun fromDailyTaskList(value: String?): List<DailyTask>? {
        if (value == null) {
            return null
        }
        val listType = object : TypeToken<List<DailyTask>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toDailyTaskList(list: List<DailyTask>?): String? {
        if (list == null) {
            return null
        }
        return Gson().toJson(list)
    }
}
