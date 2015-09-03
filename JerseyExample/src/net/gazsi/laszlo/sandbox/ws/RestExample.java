package net.gazsi.laszlo.sandbox.ws;
import javax.ws.rs.GET;  
import javax.ws.rs.Path;  
import javax.ws.rs.PathParam;

@Path("hello")
public class RestExample {
	@GET
	@Path("{name}")
	public String sayHello(@PathParam("name") String name){
		StringBuilder string=new StringBuilder("SandBox | Hello ");
		string.append(name).append("!");
		return string.toString();
	}
}
