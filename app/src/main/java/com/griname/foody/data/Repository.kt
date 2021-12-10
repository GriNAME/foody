package com.griname.foody.data

import com.griname.foody.data.database.LocalDataSource
import com.griname.foody.data.network.RemoteDataSource
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(
    val remoteDataSource: RemoteDataSource,
    val localDataSource: LocalDataSource
)