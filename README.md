# jvm-json-converter

Based on grammar described on [json.org](http://json.org/)

Supports serialization Object to JSON or from JSON.

## API

    JsonConverter converter = new JsonConverter();
    converter.toJson(object);
    converter.fromJson(json, Object.class)
    
## How to run

`mvn compile exec:java` - do nothing actually

`mvn compile test`

#### Assumptions:

  - Object class is always provided on first level  (not collection)
  - Supports only Bean objects
    - Access properties through Getters / Setters
    - Public no-args constructor

### Feautures

#### Serialization - [Tests](./src/test/java/ug/jvm/serializer/JsonSerializerTest.java)
- Objects with primitive types and strings
- Objects with collections
- Collections of primitives and objects
- Nested objects
- Handling nulls


#### Deserialization - [Tests](./src/test/java/ug/jvm/deserializer/JsonDeserializerTest.java)

- Objects with primitive types and strings
- Objects with collections
- Nested objects
- Handling nulls

----
#### Contributors
- Piotr Lewandowski
- Krzysztof Bannach
