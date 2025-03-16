package io.truthencode.taxi.plugin.avro

import lang.taxi.generators.WritableSource
import java.io.Closeable
import java.nio.file.Path

data class MySpecialWrapper(val path: String, val ioStream : Closeable) : Closeable by ioStream{

}

data class RelativeWriteableSource(val relativePath: Path, val source: WritableSource) : WritableSource by source {
    override val path: Path
        get() = relativePath.resolve(source.path)
}