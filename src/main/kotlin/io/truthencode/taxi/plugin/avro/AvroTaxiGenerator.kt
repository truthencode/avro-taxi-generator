package io.truthencode.taxi.plugin.avro

import lang.taxi.generators.Processor
import lang.taxi.generators.TaxiProjectEnvironment
import lang.taxi.generators.WritableSource

class AvroTaxiGenerator(private val typeNamesTopLevelPackageName: String) : TaxiGenerator{

     override fun generate(
        processors: List<Processor>,
        environment: TaxiProjectEnvironment
    ): List<WritableSource> {
        // TODO("Not yet implemented")
        throw UnsupportedOperationException("Unimplemented method 'generate'")
    }
}
