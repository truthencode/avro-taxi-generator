# Demo / Test Example

This is a test example.

This demo includes one taxi file (person.taxi) and one Avro file.

The avro plugin should convert the Avro file to a taxi file.
The kotlin plugin should convert both the existing and the transliterated taxi files to a kotlin source.

```bash
# calls avro to taxi and taxi to kotlin plugins
taxi build
./mvnw verify to execute a demo using the generated kotlin classes
```

## Errata

Configuring the output directory for the generated kotlin files was not working when using the maven configuration in taxi.conf. (Hard-code of src/main/java)
