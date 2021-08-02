package com.jjhavstad.githubprdiffviewer.datasources.apis.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitGitHubApiCreator {
    fun create(baseUrl: String, httpClientBuilder: OkHttpClient.Builder): RetrofitGitHubApi {
        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
        val retrofit = builder.client(httpClientBuilder.build()).build()
        return retrofit.create(RetrofitGitHubApi::class.java)
    }
}
