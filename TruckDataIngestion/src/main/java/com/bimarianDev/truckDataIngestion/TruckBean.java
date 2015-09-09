package com.bimarianDev.truckDataIngestion;

import java.io.Serializable;

public class TruckBean implements Serializable {

	private static final long serialVersionUID = -3533554529566865184L;

	private int Angle;
	private String version;

	private double Lat;
	private double Lon;
	private double Speed;
	private double Oil;
	private double Weight;
	private double Mile;
	
	private String TNo;
	private String VNo;
	private String groupId;
	private String deploymentId;
	private String Time;
	private String Acc;
	private String timestamp;
	private String Locate;
	private String Location;
	public int getAngle() {
		return Angle;
	}
	public void setAngle(int angle) {
		Angle = angle;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String string) {
		this.version = string;
	}
	public double getLat() {
		return Lat;
	}
	public void setLat(double lat) {
		Lat = lat;
	}
	public double getLon() {
		return Lon;
	}
	public void setLon(double lon) {
		Lon = lon;
	}
	public double getSpeed() {
		return Speed;
	}
	public void setSpeed(double speed) {
		Speed = speed;
	}
	public double getOil() {
		return Oil;
	}
	public void setOil(double oil) {
		Oil = oil;
	}
	public double getWeight() {
		return Weight;
	}
	public void setWeight(double weight) {
		Weight = weight;
	}
	public double getMile() {
		return Mile;
	}
	public void setMile(double mile) {
		Mile = mile;
	}
	public String getTNo() {
		return TNo;
	}
	public void setTNo(String tNo) {
		TNo = tNo;
	}
	public String getVNo() {
		return VNo;
	}
	public void setVNo(String vNo) {
		VNo = vNo;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getDeploymentId() {
		return deploymentId;
	}
	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	public String getAcc() {
		return Acc;
	}
	public void setAcc(String acc) {
		Acc = acc;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getLocate() {
		return Locate;
	}
	public void setLocate(String locate) {
		Locate = locate;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TruckBean [Angle=").append(Angle).append(", version=")
				.append(version).append(", Lat=").append(Lat).append(", Lon=")
				.append(Lon).append(", Speed=").append(Speed).append(", Oil=")
				.append(Oil).append(", Weight=").append(Weight)
				.append(", Mile=").append(Mile).append(", TNo=").append(TNo)
				.append(", VNo=").append(VNo).append(", groupId=")
				.append(groupId).append(", deploymentId=").append(deploymentId)
				.append(", Time=").append(Time).append(", Acc=").append(Acc)
				.append(", timestamp=").append(timestamp).append(", Locate=")
				.append(Locate).append(", Location=").append(Location)
				.append("]");
		return builder.toString();
	}


}
