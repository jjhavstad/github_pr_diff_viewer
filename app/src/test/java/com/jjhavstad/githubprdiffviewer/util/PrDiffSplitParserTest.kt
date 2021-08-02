package com.jjhavstad.githubprdiffviewer.util

import com.jjhavstad.githubprdiffviewer.models.PrDiff
import com.jjhavstad.githubprdiffviewer.models.PrDiffSplit
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import java.nio.charset.Charset

class PrDiffSplitParserTest : DescribeSpec({
    describe("PrDiffSplitParser") {
        val prDiffSplitParser = PrDiffSplitParser()
        val prDiffResponseParser = PrDiffResponseParser()
        lateinit var diffInput: String
        lateinit var prDiffInput: PrDiff
        beforeEach {
            javaClass.classLoader?.getResourceAsStream("sample_pr_diff.txt")?.use {
                diffInput = it.readBytes().toString(Charset.defaultCharset())
            }
            prDiffInput = prDiffResponseParser.loadAndParse(diffInput)
        }
        describe("when parsing a PR diff file", {
            lateinit var prDiffSplit: PrDiffSplit
            beforeEach {
                prDiffSplit = prDiffSplitParser.loadAndParse(prDiffInput)
            }
            it("should create a list of PR diff split objects", {
                prDiffSplit.prSplits.size shouldBe 450
                val numLinesInAdd = prDiffSplit.prSplits[0].add.sumOf {
                    (if (it == '\n') 1 else 0) as Int
                }
                val numLinesInRemove = prDiffSplit.prSplits[0].remove.sumOf {
                    (if (it == '\n') 1 else 0) as Int
                }
                numLinesInAdd shouldBeExactly numLinesInRemove
            })
        })
    }
})
