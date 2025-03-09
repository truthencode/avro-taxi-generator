# avro-taxi-generator

Taxi Plugin wrapper to generate Avro2Taxi

This is a basic plugin attempt to generate taxi files from Avro schemas automatically when taxi build is called.

## Requirements

- Java 17+ (likely going to be 21)

This proof of concept and is targeted to initially support Avro Schema (avsc) format using the taxi-supplied avro2taxi component.

Additional improvements could be made to support other formats. My personal use case is to support Taxi-annotated Avro IDL (avdl) schemas. This may be via Apache's own avrotool to chain avdl -> avsc -> taxi or perhaps a custom avdl -> taxi plugin as the formats are similar.

```avdl
namespace org.acme.kafka.quarkus;

schema Movie;

record Movie {
    string @taxi.dataType("acme.MovieId") title;
    int year;
}
```

```taxi
namespace org.acme.kafka.quarkus {
   @lang.taxi.formats.AvroMessage
   closed model Movie {
      @lang.taxi.formats.AvroField(ordinal = 0) title : acme.MovieId
      @lang.taxi.formats.AvroField(ordinal = 1) year : org.acme.kafka.quarkus.movie.Year
   }
```
