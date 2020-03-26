package com.gts.trackmypath.presentation

import org.junit.Test
import org.junit.Assert.assertEquals

class UtilsTest {

    @Test
    fun `buildUri returns right uri`() {
        val expected = "https://farm01.staticflickr.com/10/101_xxx.jpg"
        // when
        val farm = "01"
        val server = "10"
        val id = "101"
        val secret = "xxx"
        val result = buildUri(farm, server, id, secret)
        // then
        assertEquals(expected, result)
    }
}
