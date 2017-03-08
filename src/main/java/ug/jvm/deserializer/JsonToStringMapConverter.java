package ug.jvm.deserializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ug.jvm.json.JsonSyntaxNotValidException;

public abstract class JsonToStringMapConverter {

   private enum JSON_TYPE {
      OBJECT, ARRAY
   }

   /**
    * Converts json to a map containing &lt;field name&gt; : &lt;string
    * representing field value&gt; pairs.
    */
   public static Map<String, String> convertMap(String json) {
      List<String> items = getItems(json, JSON_TYPE.OBJECT);
      return convertItemsToMap(items);
   }

   /**
    * Splits json to a list of parts representing its items, with json syntax
    * validation.
    */
   private static List<String> getItems(String json, JSON_TYPE type) {
      validate(json, type);
      char endingChar = type.equals(JSON_TYPE.OBJECT) ? '}' : ']';
      List<String> items = new ArrayList<>();
      int lastCommaPos = 0;
      for (int pos = 1; pos < json.length(); pos++) {
         // searches for ',', '}', ,'"', or '[' characters and:
         // - adds part of the json between previous ',' character 
         //     and current character (for ',')
         // - skips to a ']' character (for '[') or
         // - skips to a '"' character (for '"') or
         // - skips to a '}' character (for '{') or
         // - adds part of the json between previous ',' character 
         //     and ending character (for character in endingChar variable)
         if (json.charAt(pos) == ',' || json.charAt(pos) == endingChar) {
            items.add(json.substring(lastCommaPos + 1, pos).trim());
            lastCommaPos = pos;
         } else if (json.charAt(pos) == '"') {
            pos = skipTo(json, pos, '"');
         } else if (json.charAt(pos) == '[') {
            pos = skipTo(json, pos, ']');
         } else if (json.charAt(pos) == '{') {
            pos = skipTo(json, pos, '}');
         }
      }
      return items;
   }

   /**
    * validates by first character od {@code json} and {@code type}
    * 
    * @throws JsonSyntaxNotValidException
    */
   private static void validate(String json, JSON_TYPE type) {
      char firstChar = json.charAt(0);
      if (firstChar == '{' && !type.equals(JSON_TYPE.OBJECT)) {
         throw new JsonSyntaxNotValidException("Json is a json object, yet is parsed as a json array.");
      } else if (firstChar == '[' && !type.equals(JSON_TYPE.ARRAY)) {
         throw new JsonSyntaxNotValidException("Json is a json array, yet is parsed as a json object.");
      }
   }

   private static int skipTo(String json, int pos, char skipTo) {
      do {
         pos++;
         if (pos == json.length()) {
            throw new JsonSyntaxNotValidException("Value syntax not valid.\n Json: " + json);
         }
      } while (json.charAt(pos) != skipTo);
      return pos;
   }

   private static Map<String, String> convertItemsToMap(List<String> items) {
      Map<String, String> map = new HashMap<>(items.size());
      for (String item : items) {
         int commaIdx = item.indexOf(':');
         if (commaIdx == -1) {
            throw new JsonNotCompatibleException("No ':' character in a String representing a name:value pair.");
         }
         map.put(item.substring(0, commaIdx).trim(), item.substring(commaIdx + 1).trim());
      }
      return map;
   }

   public static List<String> convertArray(String json) {
      return getItems(json, JSON_TYPE.ARRAY);
   }
}
