package com.jjhavstad.githubprdiffviewer.models

data class Pr(
    val id: Long,
    val title: String,
    val url: String,
    val diffUrl: String
)
