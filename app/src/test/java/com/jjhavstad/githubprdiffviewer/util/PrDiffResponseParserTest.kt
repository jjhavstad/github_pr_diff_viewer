package com.jjhavstad.githubprdiffviewer.util

import com.jjhavstad.githubprdiffviewer.models.PrDiff
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.date.before
import io.kotest.matchers.shouldBe
import java.nio.charset.Charset

class PrDiffResponseParserTest : DescribeSpec({
    describe("PrDiffResposneParser", {
        val prDiffResponseParser = PrDiffResponseParser()
        lateinit var input: String
        beforeEach {
            javaClass.classLoader?.getResourceAsStream("sample_pr_diff.txt")?.use {
                input = it.readBytes().toString(Charset.defaultCharset())
            }
        }
        describe("when parsing a PR diff file", {
            lateinit var parseResult: PrDiff
            beforeEach {
                parseResult = prDiffResponseParser.loadAndParse(input)
            }
            it("should create a diff for each file listed in the diff", {
                parseResult.fileDiffs.size shouldBe 450
                parseResult.fileDiffs[0].contains("BaseKotlinConverter.kt") shouldBe true
                parseResult.fileDiffs[98].contains("LambdaReturn.identifiers.fir.txt") shouldBe true
                parseResult.fileDiffs[398].contains("LambdaReturn.values.fir.txt") shouldBe true
            })
        })
    })
})
