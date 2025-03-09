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

import io.github.oshai.kotlinlogging.KotlinLogging
import lang.taxi.generators.avro.TaxiGenerator
import java.io.File

class AvroFileTypeProcessor(
    private val fileType: AvroType,
) : FileTypeProcessor {
    private val logger = KotlinLogging.logger {}

    private var outDir: File? = null

    override fun process(
        directory: File,
        outDir: File,
    ): List<File> {
        val processedFiles = mutableListOf<File>()
        if (fileType != AvroType.Schema) {
            logger.warn { "File type not (yet) supported: ${fileType.name}" }
            return processedFiles
        }
        this.outDir = outDir

        require(directory.isDirectory) { "Input must be a directory: ${directory.absolutePath}" }
        logger.info { "INFO Processing Avro files in ${directory.absolutePath}" }

        scanForAvroFiles(directory).forEach {
            val taxiCode = TaxiGenerator().generate(it.toPath())
            logger.info { "just after generate Avro files in ${it.name}" }
            val outputFile = File(prepareOutputDirectory(outDir), it.nameWithoutExtension + ".taxi")
            if (!taxiCode.hasErrors) {
                logger.warn { "NO ERRORS on post generate Avro files in ${it.name}" }
                if (taxiCode.hasWarnings) {
                    logger.warn { "Converted name to taxi with ${taxiCode.warningCount} Warnings" }
                }
                outputFile
                    .bufferedWriter()
                    .use { writer -> taxiCode.taxi.forEach { m -> writer.write(m) } }
                processedFiles.add(outputFile)
            } else {
                logger.error { "HAZ ERRORS!!! Converted name to taxi with ${taxiCode.errorCount} Errors" }
                taxiCode.messages.forEach { logger.error { it } }
                error("Failed to convert ${it.absolutePath} to taxi")
            }
        }
        return processedFiles
    }

    /**
     * Scan for files with the specified extension
     * @param directory directory to scan
     * @return list of files with the specified extension
     */
    private fun scanForAvroFiles(directory: File): List<File> =
        directory
            .walk()
            .filter { it.isFile }
            .filter { it.extension == fileType.extension }
            .toList()

    /**
     * Verifies target destination
     */
    private fun prepareOutputDirectory(directory: File): File {
        // We should also check if we can write to the directory
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return directory
    }

    companion object {
        private const val AVRO_SCHEMA_EXTENSION = "avsc"
        private const val AVRO_PROTOCOL_EXTENSION = "avpr"
        private const val AVRO_IDL_EXTENSION = "avdl"
    }
}

interface FileTypeProcessor {
    fun process(
        directory: File,
        outDir: File,
    ): List<File>
}
