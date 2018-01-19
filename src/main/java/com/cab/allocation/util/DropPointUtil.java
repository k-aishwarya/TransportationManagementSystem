package com.cab.allocation.util;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class DropPointUtil {
	
	//To maintain the series of the drop Points and Target headquarter
	public static String points[];
	
	//To maintain the column name in the database - Postgres
	public static String column[];
	
	//To maintain the HashMap of the dropPoint database
	public static Map<Pair<String,String>, Integer> dropPointMap;	
	
	//To maintain the HashMap of the dropPoint table in sorted order
	public static Map<String,Map<String,Integer>> DropPointTable;
	
	public static int distanceMap(String a,String b){
		try {
			int res = dropPointMap.get(Pair.of(a, b));
			return res;
		}
		catch(Exception e){
			System.out.println("Data not found in the Table");
			return -1;
		}
	}
	
	public static JSONObject JSONStringToObject(String jsonString) {
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(jsonString);
		} catch (JSONException e) {
			System.out.println("Not a JSON object");
		}
		return jsonObj;
	}
}
