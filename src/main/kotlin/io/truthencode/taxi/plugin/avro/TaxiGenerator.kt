package io.truthencode.taxi.plugin.avro

import lang.taxi.generators.Processor
import lang.taxi.generators.TaxiProjectEnvironment
import lang.taxi.generators.WritableSource

/**
 * Marker interface for taxi generators.
 * Mirroring the generator-api to the extent that it is possible.
 */
interface TaxiGenerator {
    fun generate(processors: List<Processor>, environment: TaxiProjectEnvironment): List<WritableSource>
}

