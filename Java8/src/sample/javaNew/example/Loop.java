package sample.javaNew.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class Loop implements Runnable {
	static String[] input={"64.242.88.10,india,second heighest populous country in the word,narendra modi"
							,"74.542.98.30,america,super power in the world,barak obama"
							,"52.562.68.77,russia,communist country in the world,putin"
							,"44.592.56.34,england,colonial country in the world,mark vaughan"
							,"35.675.48.39,korea,dangerous country in the world,kim john"};
	
	
	
	public static void main(String[] args){
		System.out.println("started");
		Thread t= new Thread(new Loop());
		t.start();
		
		
	}
	public static int getRandom(int max){
		 return (int) (Math.random()*max); 
	}

	@Override
	public void run() {
		File file = new File("C:/Users/Vostro/Desktop/input/input.txt");
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		//pw.println("Hello World");
		
		int count=1;	
		for(int i=0;i<count;i++){
			pw.println(input[getRandom(5)]);
			pw.println(input[getRandom(5)]);
			pw.println(input[getRandom(5)]);
			
			for(int j=0;j<=2;j++){
				pw.println(input[getRandom(5)]);
				pw.println(input[getRandom(5)]);
				
			}
			count++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		pw.close();
	}
}