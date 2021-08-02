package com.jjhavstad.githubprdiffviewer.util

import com.jjhavstad.githubprdiffviewer.models.Pr
import org.json.JSONArray

class PrResponseParser : ApiResponseParser<String, List<Pr>> {
    override fun loadAndParse(input: String): List<Pr> {
        return mutableListOf<Pr>().also { _prList ->
            val prArray = JSONArray(input)
            for (i in 0 until prArray.length()) {
                val prObject = prArray.getJSONObject(i)
                val id = prObject.getLong(PR_ID_KEY)
                val title = prObject.getString(PR_TITLE_KEY)
                val url = prObject.getString(PR_URL_KEY)
                val diffUrl = prObject.getString(PR_DIFF_URL_KEY)
                val state = prObject.getString(PR_STATE_KEY)
                if (state == PR_STATE_OPEN_VALUE) {
                    _prList.add(
                        Pr(
                            id = id,
                            title = title,
                            url = url,
                            diffUrl = diffUrl
                        )
                    )
                }
            }
        }
    }

    companion object {
        private val PR_ID_KEY = "id"
        private val PR_TITLE_KEY = "title"
        private val PR_URL_KEY = "url"
        private val PR_DIFF_URL_KEY = "diff_url"
        private val PR_STATE_KEY = "state"
        private val PR_STATE_OPEN_VALUE = "open"
    }
}
