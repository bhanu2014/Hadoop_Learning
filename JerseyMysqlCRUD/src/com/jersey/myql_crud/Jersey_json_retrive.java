package com.jersey.myql_crud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/reqData")
public class Jersey_json_retrive {
	@GET 
	@Produces((MediaType.APPLICATION_JSON))
	public List<Employee> getEmployeeInfo(@QueryParam("limit")int limit) throws SQLException, ClassNotFoundException{
			Connection conn=ConnectionObj.getConnection();
			List<Employee> list=new ArrayList<>();
			String sql="Select * from employee limit ?";
			PreparedStatement stmt=conn.prepareStatement(sql);
			stmt.setInt(1, limit);
			ResultSet rs=stmt.executeQuery();
			
			while(rs.next()){
				Employee emp=new Employee();
				emp.setId( rs.getInt(1));
				emp.setName(rs.getString(2));
				emp.setSalary(rs.getInt(3));
				emp.setAddress(rs.getString(4));
				list.add(emp);
			}
			
		
		//System.out.println("list: "+list.size());
		return list;
	}
	@GET
	@Path("/create{id}/{name}/{salary}/address")
	public void InserteEmployee(@PathParam("id")int id,@PathParam("name")String name,@PathParam("salary")int salary,@PathParam("address") 
	String address) throws Exception{
		Connection conn=ConnectionObj.getConnection();
		PreparedStatement pst=conn.prepareStatement("INSERT INTO `emp_data`.`employee`(`id`,`name`,`salary`,`address`)VALUES(?,?,?,?");
		pst.setInt(0, id);
		pst.setString(1,name);
		pst.setInt(2, salary);
		pst.setString(3, address);
		Boolean b=pst.execute();
		if(!b){
			System.out.println("record not inserted");
		}
	}
	public static void main(String[] args) throws Exception {
		Connection conn=ConnectionObj.getConnection();
		String sql="Select * from employee where name = ?";
		PreparedStatement stmt=conn.prepareStatement(sql);
		stmt.setString(1, "ramu");
		ResultSet rs=stmt.executeQuery();
		Employee emp=new Employee();
		if (rs.next()){
			
			emp.setId( rs.getInt(1));
			emp.setName(rs.getString(2));
			emp.setSalary(rs.getInt(3));
			emp.setAddress(rs.getString(4));
			//list.add(emp);
		}
		
		System.out.println(emp);
	}
}
