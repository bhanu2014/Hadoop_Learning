package sample.javaNew.example;

public class CountryBean {
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
}
