package com.jjhavstad.githubprdiffviewer.datasources.apis.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface RetrofitGitHubApi {
    @GET
    suspend fun get(@Url url: String): Response<String>
}
