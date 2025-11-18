package com.bharat.mupple_sketch_app.personalization_feature.data.di

import com.bharat.mupple_sketch_app.personalization_feature.data.repo.PersonalizationRepoIml
import com.bharat.mupple_sketch_app.personalization_feature.domain.PersonalizationRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PersonalizationModule {

    @Binds
    @Singleton
    abstract fun providePersonalizationRepo(
        personalizationRepoIml: PersonalizationRepoIml
    ) : PersonalizationRepo

}