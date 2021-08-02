package com.jjhavstad.githubprdiffviewer.util

import com.jjhavstad.githubprdiffviewer.models.PrDiff
import com.jjhavstad.githubprdiffviewer.models.PrDiffSplit

class PrDiffSplitParser : ApiResponseParser<PrDiff, PrDiffSplit> {

    override fun loadAndParse(input: PrDiff): PrDiffSplit {
        val add = filterByDelimiterToExclude(input, MINUS_DELIMITER)
        var remove = filterByDelimiterToExclude(input, PLUS_DELIMITER)

        if (remove.size == 0) {
            repeat(add.size) {
                remove.add("")
            }
        } else if (add.size == 0) {
            repeat(remove.size) {
                add.add("")
            }
        } else {
            matchLineCountInAllFiles(
                from = add,
                to = remove,
                fromDelimiter = PLUS_DELIMITER,
                toDelimiter = MINUS_DELIMITER
            )
            matchLineCountInAllFiles(
                from = remove,
                to = add,
                fromDelimiter = MINUS_DELIMITER,
                toDelimiter = PLUS_DELIMITER
            )
        }

        return PrDiffSplit(
            mutableListOf<PrDiffSplit.PrSplit>().also {
                for (i in 0 until add.size) {
                    it.add(
                        PrDiffSplit.PrSplit(
                            add = add[i],
                            remove = remove[i]
                        )
                    )
                }
            }
        )
    }

    private fun filterByDelimiterToExclude(input: PrDiff, delimiter: Char): MutableList<String> {
        return mutableListOf<String>().also { _output ->
            input.fileDiffs.forEach { _fileDiff ->
                _output.add(
                    _fileDiff.split(NEWLINE_DELIMITER).filter {
                        it.isNotEmpty() && it[0] != delimiter
                    }.joinToString(NEWLINE_DELIMITER)
                )
            }
        }
    }

    protected fun matchLineCountInAllFiles(
        from: MutableList<String>,
        to: MutableList<String>,
        fromDelimiter: Char,
        toDelimiter: Char
    ) {
        from.forEachIndexed { index, a ->
            to[index] = matchLineCountInDiff(
                from = from[index],
                to = to[index],
                fromDelimiter = fromDelimiter,
                toDelimiter = toDelimiter
            )
        }
    }

    protected fun matchLineCountInDiff(
        from: String,
        to: String,
        fromDelimiter: Char,
        toDelimiter: Char
    ): String {
        val toSplit = to.split(NEWLINE_DELIMITER).toMutableList()
        val fromSplit = from.split(NEWLINE_DELIMITER).toMutableList()
        fromSplit.forEachIndexed { index, f ->
            val t = if (index < toSplit.size) toSplit[index] else ""
            if (f[0] == fromDelimiter) {
                if (t.isEmpty() || t[0] != toDelimiter) {
                    toSplit.add(index, BLANK_LINE)
                }
            }
        }
        return toSplit.joinToString(NEWLINE_DELIMITER)
    }

    companion object {
        protected val BLANK_LINE = " "
        protected val NEWLINE_DELIMITER = "\n"
        protected val PLUS_DELIMITER = '+'
        protected val MINUS_DELIMITER = '-'
    }
}
