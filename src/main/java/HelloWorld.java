import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

import static spark.Spark.*;

public class HelloWorld {

	static Connection conn = null;
	public static void main(String[] args) throws  SQLException
	{

		String url = "jdbc:oracle:thin:groupf1901/D0NEhxpaD@128.196.27.219:1521:MIS00";


		DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

		conn = DriverManager.getConnection(url);

		if (conn != null) {
			System.out.println("Connected");
		}


		get("/1", (req, res) ->  a1("test") );
		get("/2", (req, res) ->  a1("test") );
		get("/3", (req, res) ->  a1("test") );
		get("/4", (req, res) ->  a1("test") );
		get("/5", (req, res) ->  a1("test") );
		get("/6", (req, res) ->  a1("test") );
		get("/7", (req, res) ->  a1("test") );
		get("/8", (req, res) ->  a1("test") );
		get("/9", (req, res) ->  a1("test") );
		get("/10", (req, res) ->  a1("test") );
		get("/", (req, res) -> renderContent());


	}

	private static String renderContent()
	{
		try
		{
			// If you are using maven then your files
			// will be in a folder called resources.
			// getResource() gets that folder
			// and any files you specify.
			URL url = HelloWorld.class.getResource("index.html");

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


	public static String a1(String view_name){

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