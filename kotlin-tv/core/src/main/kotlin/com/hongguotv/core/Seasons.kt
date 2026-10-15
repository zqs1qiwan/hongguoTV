// SPDX-License-Identifier: GPL-3.0-only
package com.hongguotv.core

// Parsing helpers for multi-season works, whose titles carry a season suffix such as
// "之无灵证道第36季", "之无灵证道40季" or "满院亲戚全是上古大妖第五季".
object Seasons {
    data class Info(val baseTitle: String, val season: Int?)

    // Arabic digits; "第" optional ("第36季" and "40季" both occur in real titles).
    private val arabic = Regex("^(.+?)\\s*第?([0-9]{1,3})季\\s*$")
    // Chinese numerals require "第" so names like "人间四季" are not misread as season 4.
    private val chinese = Regex("^(.+?)\\s*第([零一二两三四五六七八九十]+)季\\s*$")

    fun parse(title: String): Info {
        val trimmed = title.trim()
        arabic.find(trimmed)?.let { match -> return Info(match.groupValues[1].trim(), match.groupValues[2].toIntOrNull()) }
        chinese.find(trimmed)?.let { match -> return Info(match.groupValues[1].trim(), chineseNumber(match.groupValues[2])) }
        return Info(trimmed, null)
    }

    fun badge(title: String, countBadge: String): String {
        if (countBadge.isBlank()) return countBadge
        val season = parse(title).season ?: return countBadge
        return "第${season}季 · $countBadge"
    }

    private fun chineseNumber(text: String): Int? {
        val digits = mapOf('零' to 0, '一' to 1, '二' to 2, '两' to 2, '三' to 3, '四' to 4, '五' to 5, '六' to 6, '七' to 7, '八' to 8, '九' to 9)
        if (text == "十") return 10
        val tenIndex = text.indexOf('十')
        if (tenIndex < 0) return if (text.length == 1) digits[text[0]] else null
        val tens = if (tenIndex == 0) 1 else digits[text[tenIndex - 1]] ?: return null
        val ones = if (tenIndex + 1 < text.length) digits[text[tenIndex + 1]] ?: return null else 0
        return tens * 10 + ones
    }
}
