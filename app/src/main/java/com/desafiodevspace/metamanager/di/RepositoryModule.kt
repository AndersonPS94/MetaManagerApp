package com.desafiodevspace.metamanager.di

import com.desafiodevspace.metamanager.data.repository.GoalRepositoryImpl
import com.desafiodevspace.metamanager.data.repository.OpenAIRepositoryImpl
import com.desafiodevspace.metamanager.domain.repository.GoalRepository
import com.desafiodevspace.metamanager.domain.repository.OpenAIRepository
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
    abstract fun bindOpenAIRepository(impl: OpenAIRepositoryImpl): OpenAIRepository
}
