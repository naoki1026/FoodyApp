package com.example.foodyapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Android用の依存関係インジェクションライブラリで、プロジェクトの依存関係の注入(DI）を手動で行うための
// ボイラーテンプレートが減る。
@HiltAndroidApp
class MyApplication : Application() {}