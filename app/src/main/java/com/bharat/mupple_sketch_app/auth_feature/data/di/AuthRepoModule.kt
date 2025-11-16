package com.bharat.mupple_sketch_app.auth_feature.data.di

import com.bharat.mupple_sketch_app.auth_feature.data.repo.AuthRepoIml
import com.bharat.mupple_sketch_app.auth_feature.domain.repo.AuthRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepoModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepoIml(
        authRRepoIml: AuthRepoIml
    ) : AuthRepo

}