package com.dev.gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class GsonToObject {

	public static List<TruckBean> getTruckData(String path) throws JsonIOException, JsonSyntaxException, FileNotFoundException {

		
			List<TruckBean> data = new Gson().fromJson(new FileReader(path), new TypeToken<List<TruckBean>>(){}.getType());
		    for(TruckBean b:data){
			System.out.println("emloyee: "+b.toString());
			}
		    return data;
}
	}



