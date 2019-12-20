import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class Server
{

	static Connection conn = null;
	public static void main(String[] args) throws  SQLException
	{




		get("/1", (req, res) ->  a1("v1") );
		get("/2", (req, res) ->  a1("v2") );
		get("/3", (req, res) ->  a1("v3") );
		get("/4", (req, res) ->  a1("v4") );
		get("/5", (req, res) ->  a1("v5") );
		get("/6", (req, res) ->  a1("v6") );
		get("/7", (req, res) ->  a1("v7") );
		get("/8", (req, res) ->  a1("v8") );
		get("/9", (req, res) ->  a1("v9") );
		get("/10", (req, res) ->  a1("v10") );
		get("/", (req, res) -> renderContent());
		get("/edit", (req, res) -> renderContent2());

		get("/branch", (req, res) ->  g("branch") );
		post("/branch", (req, res) ->  p("branch", req, res) );
		delete("/branch", (req, res) ->  d("branch", "BRANCH_NUM", req, res) );
		put("/branch", (req, res) ->  u("branch", "BRANCH_NUM", req, res) );

		get("/clients", (req, res) ->  g("clients") );
		post("/clients", (req, res) ->  p("clients", req, res) );
		delete("/clients", (req, res) ->  d("clients", "CLIENT_ID", req, res) );
		put("/clients", (req, res) ->  u("clients", "CLIENT_ID", req, res) );
//
		get("/brand", (req, res) ->  g("brand") );
		post("/brand", (req, res) ->  p("brand", req, res) );
		delete("/brand", (req, res) ->  d("brand", "BRAND_ID", req, res) );
		put("/brand", (req, res) ->  u("brand", "BRAND_ID", req, res) );
//
		get("/region", (req, res) ->  g("region") );
		post("/region", (req, res) ->  p("region", req, res) );
		delete("/region", (req, res) ->  d("region", "REGION_ID", req, res) );
		put("/region", (req, res) ->  u("region", "REGION_ID", req, res) );
//
		get("/course", (req, res) ->  g("course") );
		post("/course", (req, res) ->  p("course", req, res) );
		delete("/course", (req, res) ->  d("course", "COURSE_NUM", req, res) );
		put("/course", (req, res) ->  u("course", "COURSE_NUM", req, res) );





	}

	private static String renderContent2()
	{
		try
		{
			// If you are using maven then your files
			// will be in a folder called resources.
			// getResource() gets that folder
			// and any files you specify.
			URL url = Server.class.getResource("edit.html");

			// Return a String which has all
			// the contents of the file.
			Path path = Paths.get(url.toURI());
			return new String(Files.readAllBytes(path), Charset.defaultCharset());
		}
		catch (IOException | URISyntaxException e)
		{
			// Add your own exception handlers here.
		}
		return null;
	}

	private static String renderContent()
	{
		try
		{
			// If you are using maven then your files
			// will be in a folder called resources.
			// getResource() gets that folder
			// and any files you specify.
			URL url = Server.class.getResource("index.html");

			// Return a String which has all
			// the contents of the file.
			Path path = Paths.get(url.toURI());
			return new String(Files.readAllBytes(path), Charset.defaultCharset());
		}
		catch (IOException | URISyntaxException e)
		{
			// Add your own exception handlers here.
		}
		return null;
	}

	public static String g(String view_name){
		String url = "jdbc:oracle:thin:groupf1901/D0NEhxpaD@128.196.27.219:1521:MIS00";

		try
		{
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			conn = DriverManager.getConnection(url);
			conn.setAutoCommit(true);

			if (conn != null) {
				System.out.println("Connected");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}


		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from " + view_name);

			JSONArray json = new JSONArray();
			ResultSetMetaData rsmd = rs.getMetaData();
			while(rs.next()) {
				int numColumns = rsmd.getColumnCount();
				JSONObject obj = new JSONObject();
				for (int i=1; i<=numColumns; i++) {
					String column_name = rsmd.getColumnName(i);
					if (i==1){
						obj.put("KEY9988_"+column_name, rs.getObject(column_name));
					} else {
						obj.put(column_name, rs.getObject(column_name));
					}

				}
				json.put(obj);
			}
			return json.toString();

			//return res;
		}
		catch (SQLException e)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e1)
			{
				return a1(view_name);
			}
		}
		return " SORRY WE HAD AN ERROR. PLEASE REFRESH THE PAGE ";
	}


	public static String p(String view_name, Request a, Response b){

		String url = "jdbc:oracle:thin:groupf1901/D0NEhxpaD@128.196.27.219:1521:MIS00";

		try
		{
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			conn = DriverManager.getConnection(url);
			conn.setAutoCommit(true);

			if (conn != null) {
				System.out.println("Connected");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		try
		{
			Statement stmt;

			String query_set = "INSERT INTO " + view_name + " (";
			String names = "";

			JSONObject d = new JSONObject(a.body());

			for (String key : d.keySet())
			{
				names += key + ",";
			}
			query_set = query_set + names;
			query_set = query_set.substring(0, query_set.length() - 1);
			query_set = query_set + ") values (";

			for (String key : d.keySet())
			{
				query_set = query_set += "'" + d.get(key) + "',";
			}
			query_set = query_set.substring(0, query_set.length() - 1);
			query_set = query_set + ")";

			System.out.print(query_set);
			stmt = conn.createStatement();
			stmt.executeUpdate(query_set);
			return "1";

		} catch (Exception e)
		{
			e.printStackTrace();
		}
			return " SORRY WE HAD AN ERROR. PLEASE REFRESH THE PAGE ";
	}
	public static String d(String view_name, String pkey, Request a, Response b){
		String url = "jdbc:oracle:thin:groupf1901/D0NEhxpaD@128.196.27.219:1521:MIS00";

		try
		{
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			conn = DriverManager.getConnection(url);
			conn.setAutoCommit(true);

			if (conn != null) {
				System.out.println("Connected");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		Statement stmt;
		try
		{

			JSONObject  d = new JSONObject(a.body());
			String pkey_val = d.getString(pkey);
			stmt = conn.createStatement();
			stmt.executeUpdate("delete from "  + view_name + " where "+pkey+" = '" + pkey_val+"'");


			return "1";
				//return res;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return " SORRY WE HAD AN ERROR. PLEASE REFRESH THE PAGE ";
	}


	public static String u(String view_name, String pkey, Request a, Response b){
		String url = "jdbc:oracle:thin:groupf1901/D0NEhxpaD@128.196.27.219:1521:MIS00";

		try
		{
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			conn = DriverManager.getConnection(url);
			conn.setAutoCommit(true);

			if (conn != null) {
				System.out.println("Connected");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		Statement stmt;

		String query_set = "";

		JSONObject  d = new JSONObject(a.body());
		String pkey_val = d.getString(pkey);
		d.remove(pkey);
		for (String key : d.keySet()){
			query_set += " " + key + " = '" + d.get(key) + "',";
		}
		query_set = query_set.substring(0, query_set.length() - 1);


		String query = "UPDATE " + view_name + " set " + query_set + " WHERE " +pkey + " = '" + pkey_val +"'";

		try
		{
			stmt = conn.createStatement();
			int qr = stmt.executeUpdate(query);
			//conn.commit();


			return String.valueOf(qr);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return " SORRY WE HAD AN ERROR. PLEASE REFRESH THE PAGE ";
	}



	public static String a1(String view_name){
		String url = "jdbc:oracle:thin:groupf1901/D0NEhxpaD@128.196.27.219:1521:MIS00";

		try
		{
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			conn = DriverManager.getConnection(url);
			conn.setAutoCommit(true);

			if (conn != null) {
				System.out.println("Connected");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		Statement stmt = null;
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from " + view_name);
			String res = "";

			ArrayList<String> me = DBTablePrinter.printResultSet(rs);

			for (String b: me){
				res+=b;
			}

			return res;


		}
		catch (SQLException e)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e1)
			{
				return a1(view_name);
			}
		}
		return " SORRY WE HAD AN ERROR. PLEASE REFRESH THE PAGE ";
	}
}