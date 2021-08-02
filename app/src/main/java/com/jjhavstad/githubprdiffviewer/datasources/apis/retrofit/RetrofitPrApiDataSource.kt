package com.jjhavstad.githubprdiffviewer.datasources.apis.retrofit

import com.jjhavstad.githubprdiffviewer.datasources.apis.PrApiDataSource
import com.jjhavstad.githubprdiffviewer.models.Pr
import com.jjhavstad.githubprdiffviewer.models.PrDiff
import com.jjhavstad.githubprdiffviewer.util.PrDiffResponseParser
import com.jjhavstad.githubprdiffviewer.util.PrResponseParser

class RetrofitPrApiDataSource(
    private val retrofitGitHubApi: RetrofitGitHubApi
) : PrApiDataSource {
    private val prResponseParser = PrResponseParser()
    private val prDiffResponseParser = PrDiffResponseParser()

    override suspend fun requestAllPrs(url: String): List<Pr>? {
        val result = retrofitGitHubApi.get(url)
        return when (result.isSuccessful) {
            true -> result.body()?.let { prResponseParser.loadAndParse(it) }
            false -> throw Exception(
                "Error receiving destinations: ${result.raw().request().url()} -> ${result.code()} - ${result.message()}"
            )
        }
    }

    override suspend fun requestFileDiffs(url: String): PrDiff? {
        val result = retrofitGitHubApi.get(url)
        return when (result.isSuccessful) {
            true -> result.body()?.let { prDiffResponseParser.loadAndParse(it) }
            false -> throw Exception(
                "Error receiving destinations: ${result.raw().request().url()} -> ${result.code()} - ${result.message()}"
            )
        }
    }
}
