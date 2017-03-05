package ug.jvm.deserializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ug.jvm.json.JsonSyntaxNotValidException;

public abstract class JsonToStringMapConverter {

   /**
    * Converts json to a map containing &lt;field name&gt; : &lt;string
    * representing field value&gt; pairs.
    * 
    * @param json
    */
   public static Map<String, String> convert(String json) {
      List<String> items = getItems(json);
      return convertItemsToMap(items);
   }

   /**
    * Splits json to a list of parts representing its items, with validation.
    * 
    * @param json
    */
   private static List<String> getItems(String json) {
      List<String> items = new ArrayList<String>();
      int lastCommaPos = 0;
      for (int pos = 1; pos < json.length(); pos++) {
         // searches for ',', '}', ,'"', '[' or '{' characters and:
         // - adds part of the json between previous and current ',' character (for ',' and '}')
         // - skips to a ']' character (for '[') or
         // - skips to '}' character (for '{')
         if (json.charAt(pos) == ',' || json.charAt(pos) == '}') {
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

   private static int skipTo(String json, int pos, char skipTo) {
      do {
         pos++;
         if (pos == json.length()) {
            throw new JsonSyntaxNotValidException("Value syntax not valid.\n Json: " + json);
         }
      } while (json.charAt(pos) != skipTo);
      return pos;
   }

   /**
    * @param items
    * @return
    */
   private static Map<String, String> convertItemsToMap(List<String> items) {
      Map<String, String> map = new HashMap<String, String>(items.size());
      for (String item : items) {
         int commaIdx = item.indexOf(':');
         if (commaIdx == -1) {
            throw new JsonNotCompatibleException("No ':' character in a String representing a name:value pair.");
         }
         map.put(item.substring(0, commaIdx).trim(), item.substring(commaIdx + 1).trim());
      }
      return map;
   }
}
