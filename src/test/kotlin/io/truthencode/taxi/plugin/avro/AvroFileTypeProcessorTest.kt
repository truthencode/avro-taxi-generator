/*-
 * #%L
 * Avro to Taxi build
 * %%
 * Copyright (C) 2025 Truthencode
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.truthencode.taxi.plugin.avro

import com.google.common.io.Resources
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.io.File
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertTrue

class AvroFileTypeProcessorTest {
    @TempDir
    lateinit var tempDir: Path

    val schemaProcessor = AvroFileTypeProcessor(AvroType.Schema)

    fun loadResource(res: String?): Path? {
        val res = res ?: "."
        return Path.of(
            Resources
                .getResource(res)
                .toURI(),
        )
    }

    val schemaPath = loadResource("avro/avsc")
    val protocolPath = loadResource("avro/avpr")
    val idlPath = loadResource("avro/avdl")

    @Test
    @DisplayName("Avro Schema files should successfully convert to taxi")
    fun processSchemaFilesTest() {
        val avroFiles =
            schemaProcessor.process(File(schemaPath!!.toUri()), File(tempDir.toUri()))
        assertTrue { avroFiles.size == 1 }
        tempDir.toFile().listFiles()?.forEach {
            println(it.name)
            it.bufferedReader().use {
                println(it.readText())
            }
        }
    }

    @DisplayName("Avro to Taxi Silently fail for non-JSON types")
    @ParameterizedTest
    @EnumSource(AvroType::class, names = ["Protocol", "Idl"])
    fun processProtocolFilesTest(fileType: AvroType) {
        val avroFiles =
            when (fileType) {
                AvroType.Idl ->
                    schemaProcessor.process(File(idlPath!!.toUri()), File(tempDir.toUri()))

//            AvroType.Protocol ->
//                schemaProcessor.process(File(protocolPath!!.toUri()), File(tempDir.toUri()))

                else -> emptyList()
            }

        assertTrue { avroFiles.isEmpty() }
    }
}
