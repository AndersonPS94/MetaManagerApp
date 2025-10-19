package com.desafiodevspace.metamanager.domain.repository

import com.desafiodevspace.metamanager.data.model.Goal

interface HelpRepository {
    suspend fun getReplan(goal: Goal, userSituation: String): String
}
