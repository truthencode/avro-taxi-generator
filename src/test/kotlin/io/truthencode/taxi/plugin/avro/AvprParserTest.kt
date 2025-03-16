package io.truthencode.taxi.plugin.avro

import io.truthencode.taxi.plugin.avro.TestFixtures.ToResourceAsPath
import kotlin.test.Test

class AvprParserTest {
val avpr = "avro/avpr/AvprProtocol.avpr".ToResourceAsPath()!!
    @Test
    fun testParse() {
        val parsed = TestFixtures.PARSER.parseProtocol(avpr)
        println(parsed)
    }
}