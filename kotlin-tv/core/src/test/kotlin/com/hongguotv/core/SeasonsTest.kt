package com.hongguotv.core

import org.junit.Assert.*
import org.junit.Test

class SeasonsTest {
    @Test fun parsesArabicSeasonSuffixWithPrefix() {
        val info=Seasons.parse("谁说没灵根不能修仙的？之无灵证道第36季")
        assertEquals("谁说没灵根不能修仙的？之无灵证道",info.baseTitle)
        assertEquals(36,info.season)
    }

    @Test fun parsesArabicSeasonSuffixWithoutDi() {
        val info=Seasons.parse("谁说没灵根不能修仙的？之无灵证道40季")
        assertEquals("谁说没灵根不能修仙的？之无灵证道",info.baseTitle)
        assertEquals(40,info.season)
    }

    @Test fun parsesChineseNumerals() {
        assertEquals(8,Seasons.parse("谁说没灵根不能修仙的？之无灵证道第八季").season)
        assertEquals(5,Seasons.parse("满院亲戚全是上古大妖第五季").season)
        assertEquals(10,Seasons.parse("某剧第十季").season)
        assertEquals(21,Seasons.parse("某剧第二十一季").season)
        assertEquals(42,Seasons.parse("某剧第四十二季").season)
    }

    @Test fun titlesWithoutSeasonSuffixKeepFullTitleAsBase() {
        val info=Seasons.parse("谁说没灵根不能修仙的？之无灵证道")
        assertEquals("谁说没灵根不能修仙的？之无灵证道",info.baseTitle)
        assertNull(info.season)
    }

    @Test fun namesEndingWithSeasonCharacterButNoPrefixAreNotSeasons() {
        val info=Seasons.parse("人间四季")
        assertEquals("人间四季",info.baseTitle)
        assertNull(info.season)
    }

    @Test fun badgePrefixesSeasonNumberBeforeEpisodeCount() {
        assertEquals("第36季 · 全 94 集",Seasons.badge("某剧第36季","全 94 集"))
        assertEquals("",Seasons.badge("某剧第36季",""))
        assertEquals("全 94 集",Seasons.badge("没有季数的剧","全 94 集"))
    }
}
