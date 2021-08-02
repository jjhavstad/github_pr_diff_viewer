package com.jjhavstad.githubprdiffviewer.util

import com.jjhavstad.githubprdiffviewer.models.PrDiff

class PrDiffResponseParser : ApiResponseParser<String, PrDiff> {
    override fun loadAndParse(input: String): PrDiff {
        return PrDiff(
            fileDiffs = input.split(PR_DIFF_BEGIN_FILE_PATTERN).drop(1)
        )
    }

    companion object {
        private val PR_DIFF_BEGIN_FILE_PATTERN = "diff --git"
    }
}
