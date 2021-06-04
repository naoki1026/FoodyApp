package com.example.foodyapp.data

import com.example.foodyapp.data.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

// 全てのViewModelが型の同じインスタンスを共有するには以下のアノテーションをつける
// アクティビティ存続構成の存続期間中存在する必要があるバインディングのスコープ注釈
// Repositoryは、LocalDataSourceとRemoteDataSourceに依存している

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) {
    val remote = remoteDataSource
    val local = localDataSource
}