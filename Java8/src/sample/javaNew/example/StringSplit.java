package sample.javaNew.example;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StringSplit {
	
	static String[] input={"64.242.88.10,india,second heighest populous country in the word,narendra modi"
		,"74.542.98.30,america,super power in the world,barak obama"
		,"52.562.68.77,russia,communist country in the world,putin"
		,"44.592.56.34,england,colonial country in the world,mark vaughan"
		,"35.675.48.39,korea,dangerous country in the world,kim john"};
	public static int getRandom(int max){
		 return (int) (Math.random()*max); 
	}

	public static void main(String[] args) {

		List<CountryBean> list_cbean=new ArrayList<CountryBean>();
		for(int i=0;i<=10;i++){

			
			String[] splitted=input[getRandom(5)].split(",");
			CountryBean cbean =new CountryBean();
			cbean.setIp(splitted[0]);
			cbean.setCountry(splitted[1]);
			cbean.setDescrip(splitted[2]);
			cbean.setLeader(splitted[3]);
			list_cbean.add(cbean);
		}
		    
		for(CountryBean cb:list_cbean){
			System.out.println(cb.toString());
		}
	}
}


