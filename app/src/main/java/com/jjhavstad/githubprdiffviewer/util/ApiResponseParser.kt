package com.jjhavstad.githubprdiffviewer.util

interface ApiResponseParser<in I, out T> {
    fun loadAndParse(input: I): T
}
