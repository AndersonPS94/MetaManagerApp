package com.desafiodevspace.metamanager.data.model

data class DailyTask(
    val day: Int = 0,
    val tasks: List<Task> = emptyList()
)
