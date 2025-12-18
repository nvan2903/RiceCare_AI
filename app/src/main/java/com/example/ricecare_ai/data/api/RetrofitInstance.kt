package com.example.ricecare_ai.data.api

import com.example.ricecare_ai.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit Instance Singleton
 */
object RetrofitInstance {
    
    private const val BASE_URL = BuildConfig.API_BASE_URL
    
    /**
     * Logging Interceptor for debugging
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
    
    /**
     * Custom interceptor for headers
     */
    private val headerInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build()
        chain.proceed(request)
    }
    
    /**
     * OkHttp Client
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(headerInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()
    
    /**
     * Retrofit Instance
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    /**
     * API Service Instance
     */
    val apiService: RiceCareApiService by lazy {
        retrofit.create(RiceCareApiService::class.java)
    }
    
    /**
     * Alias for backward compatibility
     */
    val api: RiceCareApiService get() = apiService
    
    /**
     * Get base URL for reference
     */
    fun getBaseUrl(): String = BASE_URL
}
