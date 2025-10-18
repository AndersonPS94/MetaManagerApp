package com.desafiodevspace.metamanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.desafiodevspace.metamanager.data.model.Goal

@Database(entities = [Goal::class], version = 1)
@TypeConverters(Converters::class)
abstract class GoalDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
}
