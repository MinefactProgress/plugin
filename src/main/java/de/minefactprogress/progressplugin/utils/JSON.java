package de.minefactprogress.progressplugin.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSON {

    public static JSONObject parse(String toParse) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(toParse);
            return json;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
