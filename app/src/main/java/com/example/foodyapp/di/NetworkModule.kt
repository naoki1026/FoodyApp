package com.example.foodyapp.di

import com.example.foodyapp.Constants.Companion.BASE_URL
import com.example.foodyapp.FoodRecipesApi
import com.example.foodyapp.models.FoodRecipe
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

// 提供したいオブジェクトを生成する
@Module

// モジュールをどのHiltコンポーネントにインストールするかを指定する
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    // javax.injectパッケージに付属している唯一のスコープアノテーションで、
    // アプリ全体で再利用したいオブジェクトにアノテーションをつけることができる
    @Singleton

    // @Injectに対応する注入物を生成するメソッドで、@Moduleに属している必要がある
    @Provides

    fun provideHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun providerConverterFactory() : GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient : OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit) : FoodRecipesApi {
        return retrofit.create(FoodRecipesApi::class.java)
    }
}