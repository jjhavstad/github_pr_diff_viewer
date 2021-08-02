package com.jjhavstad.githubprdiffviewer.util

interface ApiResponseParser<T> {
    fun loadAndParse(input: String): T
}
