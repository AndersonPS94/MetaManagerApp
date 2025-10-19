package com.desafiodevspace.metamanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.desafiodevspace.metamanager.data.local.Converters
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

@Entity(tableName = "goals")
@TypeConverters(Converters::class)
data class Goal(
    @PrimaryKey
    @DocumentId
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val targetDate: Timestamp = Timestamp.now(),
    val dailyTasks: List<DailyTask> = emptyList()
)
