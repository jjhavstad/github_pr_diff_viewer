package com.jjhavstad.githubprdiffviewer.models


data class PrDiffSplit(
    val prSplits: List<PrSplit>
) {
    data class PrSplit(
        val add: String,
        val remove: String
    )
}
