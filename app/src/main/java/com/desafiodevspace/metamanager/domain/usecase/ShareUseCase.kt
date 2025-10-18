package com.desafiodevspace.metamanager.domain.usecase

import android.content.Context
import android.content.Intent
import androidx.core.app.ShareCompat
import com.desafiodevspace.metamanager.data.model.Goal
import javax.inject.Inject

class ShareUseCase @Inject constructor() {
    operator fun invoke(context: Context, goal: Goal) {
        val planText = "Meta: ${goal.title}\n\n" +
            goal.dailyTasks.joinToString("\n\n") { dailyTask ->
                "Dia ${dailyTask.day}:\n" + dailyTask.tasks.joinToString("\n") { task ->
                    "- ${task.description}"
                }
            }

        val intent = ShareCompat.IntentBuilder(context)
            .setType("text/plain")
            .setSubject("Meu Plano para: ${goal.title}")
            .setText(planText)
            .createChooserIntent()

        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
