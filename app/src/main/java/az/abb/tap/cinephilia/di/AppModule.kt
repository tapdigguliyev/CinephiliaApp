package az.abb.tap.cinephilia.di

import android.content.Context
import android.net.ConnectivityManager
import az.abb.tap.cinephilia.R
import az.abb.tap.cinephilia.data.network.tmdb.ApiService
import az.abb.tap.cinephilia.data.repository.MediaRepository
import az.abb.tap.cinephilia.utility.Constants.BASE_URL
import az.abb.tap.cinephilia.utility.NetworkStatusChecker
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor() = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideClient(loggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideMediaRepository(apiService: ApiService) = MediaRepository(apiService)

    @Provides
    @Singleton
    fun provideGlide(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_baseline_file_download_24)
            .error(R.drawable.ic_baseline_error_24)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )

    @Provides
    @Singleton
    fun provideNetworkStatusChecker(
        @ApplicationContext context: Context
    ): NetworkStatusChecker = NetworkStatusChecker(context.getSystemService(ConnectivityManager::class.java))
}