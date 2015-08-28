package com.spark.streaming;

import java.io.Serializable;



public class CountryBean implements Serializable {
	
	private static final long serialVersionUID = 4176038314600783599L;

	private String ip;
	private String country;
	private String descrip;
	private String leader;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getDescrip() {
		return descrip;
	}
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	
	public String toString(){
		return getIp()+" "+getCountry()+" "+getDescrip()+" "+getLeader();
	}
	
	public static CountryBean parseStreamLine(String line){
		String[] splitted=line.split(",");
		CountryBean cbean =new CountryBean();
		cbean.setIp(splitted[0]);
		cbean.setCountry(splitted[1]);
		cbean.setDescrip(splitted[2]);
		cbean.setLeader(splitted[3]);

		return cbean;
		
	}
}
