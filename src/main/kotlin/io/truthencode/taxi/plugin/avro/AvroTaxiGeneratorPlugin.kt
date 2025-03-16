package io.truthencode.taxi.plugin.avro

// import org.apache.maven.model.Build
// import org.apache.maven.model.Model
// import org.apache.maven.model.Plugin
// import org.apache.maven.model.PluginExecution
import lang.taxi.TaxiDocument
import lang.taxi.generators.Processor
import lang.taxi.generators.TaxiProjectEnvironment
import lang.taxi.generators.WritableSource
import lang.taxi.plugins.PluginWithConfig
import org.springframework.boot.info.BuildProperties
import org.springframework.stereotype.Component
import java.nio.file.Paths
import java.util.*

@Component
class AvroTaxiGeneratorPlugin(val buildInfo: BuildProperties?) :
    PluginWithConfig<AvroTaxiPluginConfig> {
    private lateinit var config: AvroTaxiPluginConfig

    val taxiVersion: String
        get() {
            return config.taxiVersion ?: buildInfo?.version ?: "develop"
        }

    override fun setConfig(config: AvroTaxiPluginConfig) {
        this.config = config
    }

      fun generateStuff(
        taxi: TaxiDocument,
        processors: List<Processor>,
        environment: TaxiProjectEnvironment
    ): List<WritableSource> {
        val outputPathRoot =
            if (config.maven == null) Paths.get(config.outputPath)
            else environment.outputPath.resolve("src/main/java")
        val defaultPackageName =
            config.generatedTypeNamesPackageName
                ?: environment
                    .project
                    .identifier
                    .name
                    .organisation
                    .replace("/", ".")
                    .replace("@", "")

        val generator: AvroTaxiGenerator = AvroTaxiGenerator(defaultPackageName)
        val sources =
            generator.generate( processors, environment).map {
                RelativeWriteableSource(outputPathRoot, it)
            }

        val mavenGenerated: List<WritableSource> = emptyList()

        return sources + mavenGenerated
    }

    //    private fun applyMavenConfiguration(taxi: TaxiDocument, processors: List<Processor>,
    // environment: TaxiProjectEnvironment): List<WritableSource> {
    //       val mavenKotinConfigurer: MavenModelConfigurer = { model: Model ->
    //          model.dependencies.add(org.apache.maven.model.Dependency().apply {
    //             groupId = "org.jetbrains.kotlin"
    //             artifactId = "kotlin-stdlib"
    //             version = property("kotlin.version")
    //          })
    //          model.dependencies.add(org.apache.maven.model.Dependency().apply {
    //             groupId = "org.taxilang"
    //             artifactId = "taxi-annotations"
    //             version = property("taxi.version")
    //          })

    //          if (model.properties == null) {
    //             model.properties = Properties()
    //          }
    //          model.properties.set("maven.compiler.source", config.jvmTarget)
    //          model.properties.set("maven.compiler.target", config.jvmTarget)

    //          model.properties.set("kotlin.version", config.kotlinVersion)
    //          model.properties.set("taxi.version", taxiVersion)

    //          if (model.build == null) {
    //             model.build = Build()
    //          }

    //          model.build.addPlugin(Plugin().apply {
    //             groupId = "org.jetbrains.kotlin"
    //             artifactId = "kotlin-maven-plugin"
    //             version = property("kotlin.version")

    //             executions.add(PluginExecution().apply {
    //                id = "compile"
    //                phase = "compile"
    //                goals = listOf("compile")
    //             })
    //          })

    //          model
    //       }
    //       val mavenGenerator = MavenPomGeneratorPlugin(mavenKotinConfigurer)
    //       mavenGenerator.setConfig(config.maven!!)

    //       return mavenGenerator.generate(taxi, processors, environment)
    //    }

    private fun property(s: String) = "\${$s}"

    override fun generate(
        taxi: TaxiDocument,
        processors: List<Processor>,
        environment: TaxiProjectEnvironment
    ): List<WritableSource> {
    }

    override val id: ArtifactId = ArtifactId("taxi-avro-generator", "0.0.1-SNAPSHOT")
}

data class AvroTaxiPluginConfig(
    val buildTool: BuildTool = BuildTool.MAVEN,
    val outputPath: String = "src/main/taxi",
    // val maven: MavenGeneratorPluginConfig?,
    val taxiVersion: String? = null,
    val avroVersion: String? = null,
    val maven: MavenConfig? = null,
    // Will default to the organisation name from the project if not defined
    val generatedTypeNamesPackageName: String? = null
)

/** The build tool to use for the generated project Currently only MAVEN is supported */
enum class BuildTool {
    MAVEN,
    GRADLE
}
