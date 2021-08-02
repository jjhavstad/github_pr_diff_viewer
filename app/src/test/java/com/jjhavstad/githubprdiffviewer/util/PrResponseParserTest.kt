package com.jjhavstad.githubprdiffviewer.util

import com.jjhavstad.githubprdiffviewer.models.Pr
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.nio.charset.Charset

class PrResponseParserTest : DescribeSpec({
    describe("PrResponseTest", {
        val prResponseParser = PrResponseParser()
        lateinit var input: String
        beforeEach {
            javaClass.classLoader?.getResourceAsStream("sample_pr.json")?.use {
                input = it.readBytes().toString(Charset.defaultCharset())
            }
        }
        describe("parsing a json response of the list of PRs for a repository", {
            lateinit var parseResult: List<Pr>
            beforeEach {
                parseResult = prResponseParser.loadAndParse(input)
            }
            it("should create a list of PR data objects", {
                parseResult.size shouldBe 5

                parseResult[0].id shouldBe 700094966L
                parseResult[0].title shouldBe "FIR/UAST: commonzie parent conversion"
                parseResult[0].url shouldBe "https://api.github.com/repos/JetBrains/intellij-kotlin/pulls/92"
                parseResult[0].diffUrl shouldBe "https://github.com/JetBrains/intellij-kotlin/pull/92.diff"

                parseResult[1].id shouldBe 699494516L
                parseResult[1].title shouldBe "[JVM Debugger] Add missing dependency to tests"
                parseResult[1].url shouldBe "https://api.github.com/repos/JetBrains/intellij-kotlin/pulls/90"
                parseResult[1].diffUrl shouldBe "https://github.com/JetBrains/intellij-kotlin/pull/90.diff"

                parseResult[2].id shouldBe 697610626L
                parseResult[2].title shouldBe "FIR/UAST: commonize UAnnotation"
                parseResult[2].url shouldBe "https://api.github.com/repos/JetBrains/intellij-kotlin/pulls/89"
                parseResult[2].diffUrl shouldBe "https://github.com/JetBrains/intellij-kotlin/pull/89.diff"

                parseResult[3].id shouldBe 696256866L
                parseResult[3].title shouldBe "FIR/UAST: commonize enum constant"
                parseResult[3].url shouldBe "https://api.github.com/repos/JetBrains/intellij-kotlin/pulls/88"
                parseResult[3].diffUrl shouldBe "https://github.com/JetBrains/intellij-kotlin/pull/88.diff"

                parseResult[4].id shouldBe 694742966L
                parseResult[4].title shouldBe "FIR/UAST: commonize UFunctionCallExpression"
                parseResult[4].url shouldBe "https://api.github.com/repos/JetBrains/intellij-kotlin/pulls/87"
                parseResult[4].diffUrl shouldBe "https://github.com/JetBrains/intellij-kotlin/pull/87.diff"
            })
        })
    })
})
