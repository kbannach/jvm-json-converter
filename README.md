# jvm-json-converter

Based on grammar described on [json.org](http://json.org/)

## API

    JsonConverter converter = new JsonConverter();
    converter.toJson(object);
    converter.fromJson(json, Object.class)

### Works
- Objects with primitive types
- Objects with collections


### Not works
- Collections of objects
- Nested objects

----
#### Contributors
- Piotr Lewandowski
- Krzysztof Bannach