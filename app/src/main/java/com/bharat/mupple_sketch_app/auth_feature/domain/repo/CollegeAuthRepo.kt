package com.bharat.mupple_sketch_app.auth_feature.domain.repo

import com.bharat.mupple_sketch_app.auth_feature.domain.model.College
import kotlinx.coroutines.flow.Flow

interface CollegeAuthRepo {

     fun getAllColleges(): Flow<List<College>>
}