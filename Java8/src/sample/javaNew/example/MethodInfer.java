package sample.javaNew.example;

import java.util.ArrayList;
import java.util.List;

public class MethodInfer {
	//static String[] name={"bhanuprathap","saranya"};
	@SuppressWarnings("unchecked")
	public static void main(String[] args){
		@SuppressWarnings("rawtypes")
		List names = new ArrayList();
		
	      names.add("Mahesh");
	      names.add("Suresh");
	      names.add("Ramesh");
	      names.add("Naresh");
	      names.add("Kalpesh");
			
		names.forEach(System.out::println);
	}
}
