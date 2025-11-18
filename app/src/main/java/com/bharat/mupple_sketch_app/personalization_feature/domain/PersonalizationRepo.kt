package com.bharat.mupple_sketch_app.personalization_feature.domain

import kotlinx.coroutines.flow.Flow

interface PersonalizationRepo {
    fun savePersonalizationDetails() : Flow<Result<Unit>>
}