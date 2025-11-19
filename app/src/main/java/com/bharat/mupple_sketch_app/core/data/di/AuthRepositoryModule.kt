package com.bharat.mupple_sketch_app.core.data.di

import com.bharat.mupple_sketch_app.core.data.repo.AuthRepositoryIml
import com.bharat.mupple_sketch_app.core.domain.repo.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {
    @Binds
    @Singleton
    abstract fun provideAuthRepoIml(
        authRepositoryIml: AuthRepositoryIml
    ) : AuthRepository
}