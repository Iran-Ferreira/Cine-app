package com.example.myapplication.data.remote

import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object TranslationService {
    suspend fun translateToPtBr(text: String): String {
        if (text.isBlank() || text == "Sem sinopse disponível.") return text
        return withContext(Dispatchers.IO) {
            val googleResult = translateViaGoogle(text)
            if (googleResult.isNotBlank() && googleResult != text) {
                return@withContext googleResult
            }

            val myMemoryResult = translateViaMyMemory(text)
            if (myMemoryResult.isNotBlank() && myMemoryResult != text) {
                return@withContext myMemoryResult
            }

            text
        }
    }

    private fun translateViaGoogle(text: String): String {
        try {
            val encodedText = URLEncoder.encode(text, "UTF-8")
            val urlString = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=pt&dt=t&q=$encodedText"
            val url = URL(urlString)
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 4000
                readTimeout = 4000
                setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
            }

            if (connection.responseCode == 200) {
                val responseText = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonElement = JsonParser.parseString(responseText)
                if (jsonElement.isJsonArray) {
                    val outerArray = jsonElement.asJsonArray
                    if (outerArray.size() > 0 && outerArray[0].isJsonArray) {
                        val sentencesArray = outerArray[0].asJsonArray
                        val translatedSb = StringBuilder()
                        for (sentence in sentencesArray) {
                            if (sentence.isJsonArray && sentence.asJsonArray.size() > 0) {
                                val part = sentence.asJsonArray[0].asString
                                translatedSb.append(part)
                            }
                        }
                        val result = translatedSb.toString().trim()
                        if (result.isNotEmpty()) return result
                    }
                }
            }
        } catch (_: Exception) { }
        return text
    }

    private fun translateViaMyMemory(text: String): String {
        try {
            val sampleText = if (text.length > 400) text.substring(0, 400) else text
            val encodedText = URLEncoder.encode(sampleText, "UTF-8")
            val urlString = "https://api.mymemory.translated.net/get?q=$encodedText&langpair=en|pt-BR"
            val url = URL(urlString)
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 4000
                readTimeout = 4000
                setRequestProperty("User-Agent", "Mozilla/5.0")
            }

            if (connection.responseCode == 200) {
                val responseText = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonElement = JsonParser.parseString(responseText)
                if (jsonElement.isJsonObject) {
                    val obj = jsonElement.asJsonObject
                    if (obj.has("responseData") && obj.get("responseData").isJsonObject) {
                        val responseData = obj.getAsJsonObject("responseData")
                        if (responseData.has("translatedText")) {
                            val translated = responseData.get("translatedText").asString.trim()
                            if (translated.isNotEmpty() && !translated.contains("MYMEMORY WARNING")) {
                                return translated
                            }
                        }
                    }
                }
            }
        } catch (_: Exception) { }
        return text
    }
}
