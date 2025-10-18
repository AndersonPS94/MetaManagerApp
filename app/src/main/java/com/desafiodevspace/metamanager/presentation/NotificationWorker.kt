package com.desafiodevspace.metamanager.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.desafiodevspace.metamanager.R
import com.desafiodevspace.metamanager.domain.repository.GoalRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val goalRepository: GoalRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val goals = goalRepository.getAllGoals().first()
        val todayTasks = goals.flatMap { it.dailyTasks }.flatMap { it.tasks }.filter { !it.isCompleted }

        if (todayTasks.isNotEmpty()) {
            showNotification("Você tem ${todayTasks.size} tarefas para hoje!")
        }

        return Result.success()
    }

    private fun showNotification(message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("goal_notifications", "Notificações de Metas", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "goal_notifications")
            .setContentTitle("Meta Manager")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        notificationManager.notify(1, notification)
    }
}
