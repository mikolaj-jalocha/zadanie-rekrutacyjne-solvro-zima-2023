package com.mikolaj.solvro


import com.mikolaj.solvro.data.network.TaskApi
import com.mikolaj.solvro.data.network.TaskInterceptor
import com.mikolaj.solvro.data.network.TaskRepository
import com.mikolaj.solvro.data.network.TaskRepositoryImpl
import com.mikolaj.solvro.util.Constants.API_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(TaskInterceptor())
        }.build()
    }

    @Provides
    @Singleton
    fun provideTaskApi(httpClient: OkHttpClient): TaskApi {
        return Retrofit.Builder()
            .baseUrl(API_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaskApi::class.java)
    }


    @Provides
    @Singleton
    fun provideTaskRepository(api: TaskApi): TaskRepository {
        return TaskRepositoryImpl(api)
    }

}