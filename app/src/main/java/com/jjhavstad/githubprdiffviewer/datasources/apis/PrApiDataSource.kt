package com.jjhavstad.githubprdiffviewer.datasources.apis

import com.jjhavstad.githubprdiffviewer.models.Pr
import com.jjhavstad.githubprdiffviewer.models.PrDiff
import java.lang.Exception
import kotlin.jvm.Throws

interface PrApiDataSource {
    @Throws(Exception::class)
    fun requestAllPrs(path: String): List<Pr>?

    @Throws(Exception::class)
    fun requestFileDiffs(prPath: String): PrDiff?
}