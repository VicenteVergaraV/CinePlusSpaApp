package com.cineplusapp.cineplusspaapp.data.di

import android.content.Context
import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import com.cineplusapp.cineplusspaapp.data.remote.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://10.0.2.2:3000/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides @Singleton
    fun provideLogging(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /** Retrofit “limpio” para /auth/refresh */
    @Provides @Singleton @Named("authOkHttp")
    fun provideAuthOkHttp(logging: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

    @Provides @Singleton @Named("authRetrofit")
    fun provideAuthRetrofit(@Named("authOkHttp") ok: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(ok)
            .build()

    /** Interceptor que inyecta Authorization (salta /user/login y /auth/refresh) */
    @Provides @Singleton
    fun provideAuthInterceptor(session: SessionManager): AuthInterceptor =
        AuthInterceptor(session) // ya excluye /user/login y /auth/refresh (:contentReference[oaicite:2]{index=2})

    /** Authenticator que refresca tokens usando el Retrofit “limpio” */
    @Provides @Singleton
    fun provideTokenAuthenticator(
        session: SessionManager,
        @Named("authRetrofit") authRetrofit: Retrofit
    ): TokenAuthenticator = TokenAuthenticator(session, authRetrofit)

    /** OkHttp principal con Interceptor + Authenticator */
    @Provides @Singleton @Named("mainOkHttp")
    fun provideMainOkHttp(
        logging: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .authenticator(tokenAuthenticator)  // <<— IMPORTANTE
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

    /** Retrofit principal (para ApiService) */
    @Provides @Singleton @Named("mainRetrofit")
    fun provideMainRetrofit(@Named("mainOkHttp") ok: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(ok)
            .build()

    @Provides @Singleton
    fun provideApiService(@Named("mainRetrofit") retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides @Singleton
    fun provideAuthApi(@Named("authRetrofit") retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)
}
