package com.desafiodevspace.metamanager.di

import com.desafiodevspace.metamanager.data.repository.AIRepositoryImpl
import com.desafiodevspace.metamanager.data.repository.GoalRepositoryImpl
import com.desafiodevspace.metamanager.data.repository.HelpRepositoryImpl
import com.desafiodevspace.metamanager.domain.repository.AIRepository
import com.desafiodevspace.metamanager.domain.repository.GoalRepository
import com.desafiodevspace.metamanager.domain.repository.HelpRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGoalRepository(impl: GoalRepositoryImpl): GoalRepository

    @Binds
    @Singleton
    abstract fun bindAIRepository(impl: AIRepositoryImpl): AIRepository

    @Binds
    @Singleton
    abstract fun bindHelpRepository(impl: HelpRepositoryImpl): HelpRepository
}
