package io.truthencode.taxi.plugin.avro

import com.squareup.kotlinpoet.ClassName
import io.toolisticon.kotlin.avro.AvroParser
import java.net.URL
import java.time.Instant

/**
 * full Credit (minus any muddling I injected) to https://github.com/toolisticon/avro-kotlin/blob/develop/avro-kotlin-generator/src/test/kotlin/TestFixtures.kt
 */
object TestFixtures {
    val NOW = Instant.parse("2024-08-21T23:19:02.152209Z")
    val NOW_SUPPLER = { NOW }
    val PARSER = AvroParser()

    //    val DEFAULT_PROPERTIES = DefaultAvroKotlinGeneratorProperties(nowSupplier = NOW_SUPPLER)
//    val DEFAULT_GENERATOR = AvroKotlinGenerator(properties = DEFAULT_PROPERTIES)
//    val DEFAULT_REGISTRY = AvroCodeGenerationSpiRegistry.load()
    /**
     * Loads a resource from the classpath
     * Adjusted the naming to make it explicitly obvious that it is using the string
     * as a path to a resource to avoid any confusion.
     * based on https://stackoverflow.com/q/42739807/400729 with comments in https://stackoverflow.com/a/45403183/400729
     * Need to move this to a utility class
     */
    fun String.ToResourceAsPath(): URL? = object {}.javaClass.getResource(this)

    fun parseDeclaration(path: String) = PARSER.parseSchema(path.ToResourceAsPath()!!)

    fun expectedSource(className: ClassName) = "generated/$className.txt".ToResourceAsPath()!!.readText()

    fun <K : Any, V : Any> Map<K, V>.toReadableString() = StringBuilder()
        .apply {
            this@toReadableString.forEach { (k, v) ->
                appendLine("$k:")
                appendLine("\t$v")
                appendLine()
            }
        }
        .toString()

}