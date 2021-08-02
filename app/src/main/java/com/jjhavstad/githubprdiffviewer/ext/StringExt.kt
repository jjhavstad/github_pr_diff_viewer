package com.jjhavstad.githubprdiffviewer.ext

import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan

fun String.applyBackgroundColorToRegion(
    color: Int,
    startDelimiter: Char,
    endDelimiter: Char
): SpannableString {
    var startIndex = 0
    var endIndex = this.indexOf(endDelimiter, startIndex+1)
    var searchForStartDelimiter: () -> Unit = {
        while (
            this[startIndex] != startDelimiter &&
            startIndex < this.length &&
            endIndex != -1
        ) {
            startIndex = endIndex+1
            endIndex = this.indexOf(endDelimiter, startIndex+1)
        }
    }

    return SpannableString(this).also {
        searchForStartDelimiter()
        while (startIndex < this.length && endIndex != -1) {
            it.setSpan(
                BackgroundColorSpan(color),
                startIndex,
                endIndex,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            startIndex = endIndex+1
            endIndex = this.indexOf(endDelimiter, startIndex+1)
            searchForStartDelimiter()
        }
    }
}
