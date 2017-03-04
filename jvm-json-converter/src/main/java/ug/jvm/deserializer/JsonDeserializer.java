package ug.jvm.deserializer;

import static ug.jvm.json.JsonSyntax.ARRAY_END;
import static ug.jvm.json.JsonSyntax.ARRAY_START;
import static ug.jvm.json.JsonSyntax.OBJECT_END;
import static ug.jvm.json.JsonSyntax.OBJECT_START;
import java.util.Map;
import ug.jvm.json.JsonSyntax;
import ug.jvm.json.JsonSyntaxNotValidException;

public class JsonDeserializer {

   public <T> T fromJson(String json, Class<T> type) {
      json = json.trim();
      if (wraps(json, ARRAY_START, ARRAY_END)) {
         // array
      } else if (wraps(json, OBJECT_START, OBJECT_END)) {
         // object
         Map<String, String> nameValueStringMap = fetchMapFromJson(json);
      }
      throw new JsonSyntaxNotValidException("First character is not array nor object.\n Json: " + json);
   }

   /**
    * @param json
    * @return &lt;field name&gt; : &lt;string representing field value&gt; map
    */
   private Map<String, String> fetchMapFromJson(String json) {
      return JsonToStringMapConverter.convert(json);
   }

   private boolean wraps(String json, JsonSyntax startsWithElement, JsonSyntax endsWithElement) {
      return starts(json, startsWithElement) && ends(json, endsWithElement);
   }

   private boolean starts(String json, JsonSyntax syntaxElement) {
      return json.startsWith(syntaxElement.toString());
   }

   private boolean ends(String json, JsonSyntax syntaxElement) {
      return json.endsWith(syntaxElement.toString());
   }
}
