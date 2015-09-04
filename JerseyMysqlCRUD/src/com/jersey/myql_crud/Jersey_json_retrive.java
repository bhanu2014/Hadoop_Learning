package com.jersey.myql_crud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONObject;

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
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public void InserteEmployee(Employee emp) throws Exception {
		System.out.println("employee: "+emp.toString()	);
		Connection conn=ConnectionObj.getConnection();
		PreparedStatement pst=conn.prepareStatement("INSERT INTO employee(id,name,salary,address) VALUES(?,?,?,?)");
		pst.setInt(1, emp.getId());
		pst.setString(2,emp.getName());
		pst.setInt(3, emp.getSalary());
		pst.setString(4, emp.getAddress());
		int b=pst.executeUpdate();
	if(b!=0){
			System.out.println("record not inserted");
		}
	}
	@PUT
	@Path("/putdata")
	public void UpdateEmployee(String input) throws Exception {
		System.out.println(" location: "+ input);
		
		JSONObject json = new JSONObject(input);
		
		Connection conn=ConnectionObj.getConnection();
		PreparedStatement pst=conn.prepareStatement("UPDATE emp_data.employee "+
														"SET address =? "+
														"WHERE id = ?;");
		pst.setString(1, json.getString("address"));
		pst.setInt(2,json.getInt("id"));
		int b=pst.executeUpdate();
	if(b!=0){
			System.out.println("record not inserted");
		}
	}
	
	@DELETE
	@Path("/deletedata")
	public void DeleteEmployee(String input) throws Exception {
		System.out.println(" location: "+ input);
		
		JSONObject json = new JSONObject(input);
		
		Connection conn=ConnectionObj.getConnection();
		PreparedStatement pst=conn.prepareStatement("delete from emp_data.employee "+
														"WHERE id = ?;");
		pst.setInt(1,json.getInt("id"));
		int b=pst.executeUpdate();
	if(b!=0){
			System.out.println("record not inserted");
		}
	}
	
}
