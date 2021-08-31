package com.example.foodrecipe.repo

import com.example.foodrecipe.remote.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject



@ActivityRetainedScoped
//Scope annotation for bindings that should exist for the life of an activity, surviving configuration.
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource
) {
    val remote=remoteDataSource
}