package com.cineplusapp.cineplusspaapp.data.di

import com.cineplusapp.cineplusspaapp.data.remote.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

private const val BASE_URL = "http://10.0.2.2:3000/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides @Singleton
    fun provideLogging(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    // ---------- OKHTTP CLIENTS ----------

    // Cliente SOLO para auth (login/refresh): sin TokenAuthenticator
    @Provides @Singleton @Named("authClient")
    fun provideAuthClient(
        logging: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor // este ya omite /user/login y /auth/refresh
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(logging)
        .build()

    // Cliente general de API: con TokenAuthenticator (refresh en 401)
    @Provides @Singleton @Named("apiClient")
    fun provideApiClient(
        logging: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .authenticator(tokenAuthenticator)
        .addInterceptor(logging)
        .build()

    // ---------- RETROFITS ----------

    @Provides @Singleton @Named("authRetrofit")
    fun provideAuthRetrofit(@Named("authClient") client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Provides @Singleton @Named("apiRetrofit")
    fun provideApiRetrofit(@Named("apiClient") client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    // ---------- APIS ----------

    // AuthApi se crea con el retrofit de AUTH (sin TokenAuthenticator)
    @Provides @Singleton
    fun provideAuthApi(@Named("authRetrofit") retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    // ApiService normal con retrofit de API (con TokenAuthenticator)
    @Provides @Singleton
    fun provideApiService(@Named("apiRetrofit") retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}
