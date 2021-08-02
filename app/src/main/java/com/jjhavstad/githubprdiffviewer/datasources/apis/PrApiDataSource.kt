package com.jjhavstad.githubprdiffviewer.datasources.apis

import com.jjhavstad.githubprdiffviewer.models.Pr
import com.jjhavstad.githubprdiffviewer.models.PrDiff
import java.lang.Exception
import kotlin.jvm.Throws

interface PrApiDataSource {
    @Throws(Exception::class)
    suspend fun requestAllPrs(url: String): List<Pr>?

    @Throws(Exception::class)
    suspend fun requestFileDiffs(url: String): PrDiff?
}
