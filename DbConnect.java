// MSIS Capstone 498 - Spring 2016 - Northwestern University - Team 4
// 04-25-2016

// DbConnect class contains the method getConnection() to connect to a database 
// (either localhost or remote server) and uses post() to accept an appropriate 
// ArrayList and INSERT statement for a given table to populate the database
package cis498;

import java.sql.*;
import java.util.ArrayList;


public class DbConnect {

	// this method connects to the database using a localhost or remote server method
	public static Connection getConnection() throws Exception{
		
		try{
			String driver = "com.mysql.jdbc.Driver";
			
			// to connect to localhost MySql database
			String url = "jdbc:mysql://localhost:3306/exams";
			String username = "root";
			String pwd = "";

			// to connect to inmotion hosting account			
//			String url = "jdbc:mysql://70.39.248.227:3306/deltau5_du_database";
//			String username = "deltau5_team4";
//			String pwd = "deltau!";
			
//			String url = "jdbc:mysql://192.249.123.44:3306/kartpa5_exams";
//			String username = "kartpa5_deltau5";
//			String pwd = "deltau!";
			
			Class.forName(driver);
			
			Connection conn = DriverManager.getConnection(url, username, pwd);
			System.out.println("Connected");			
			return conn;
			
		} catch(Exception e) {
			System.out.println(e);			
		}
		return null;
	} // end - getConnection() 

	
	// post() accepts arraylist of the table values and the specific insert statement
	// method inserts parsed and separated values from .txt file into appropriate table
	public static void post(ArrayList<String[]> list, String stmt) throws Exception{
		
		// ArrayList of properly assorted String[] of columns to insert into relevant table
		ArrayList<String[]> load = list;
		// relevant INSERT statement to use per table structure
		String insertStatement = stmt;
		
		try{
			// use getConnection() method to create a connection
			Connection con = getConnection();
			// holds INSERT statement to use in element inserting for loop (below)
			PreparedStatement posted = con.prepareStatement(insertStatement);			
			
				// Insert each element from each String from ArrayList into relevant DB table 
				for(int k = 0; k < load.get(0).length; k++) {						
						//System.out.println(load.get(0).length);
						for(int j = 0; j < load.size(); j++){
							 //System.out.println(j);
				    	     posted.setString (j+1, load.get(j)[k]); // this inserts array into values(?) of query
				    	     //System.out.println(j+"-"+k);
						}
					posted.addBatch();
					posted.executeUpdate();
			    } // end for() to insert each element into table
	    	  
		} catch(Exception e){
			System.out.println(e);
		} finally {
			System.out.println("Insert Completed:");
		} // end - try/finally
		
	} // end - post()
	

// commented out section holds various query methods that may be used in future	
	
//	// select records from DB and print them out
//		public void selectData(){
//			
//			try{
//				String query= "select * from buildings";
//				rs = st.executeQuery(query);
//				System.out.println("Records from DB");
//				
//				while(rs.next()){
//					String name = rs.getString("name");
//					String availability = rs.getString("availability");
//					System.out.println("Name: " + name + "\t" + 
//					"Availability on Weekends: " + availability);
//				}
//				
//			}catch(Exception ex){
//				System.out.println(ex);
//			}
//		}
//		
		
			
//		// insert records into DB
//			public void insertData(String insertStatement){
//						
//				String statement = insertStatement;
//					
//				try{
//					String insert = statement;
//					st.executeUpdate(insert);
//					System.out.println("Insert values into DB");
//											
//				}catch(Exception ex){
//					System.out.println(ex);
//				}
//			}
//		
//			
//			
//		// update records in DB
//			public void updateData(String updateStatement){
//				
//				String statement = updateStatement;
//							
//				try{
//					String update = statement;
//					st.executeUpdate(update);
//					System.out.println("Updated values in DB");
//											
//				}catch(Exception ex){
//					System.out.println(ex);
//				}
//			}
//		
//			
//		
//		// delete records from DB
//			public void deleteData(String deleteStatement){
//				
//				String statement = deleteStatement;
//							
//				try{
//					String delete = statement;
//					st.execute(delete);
//					System.out.println("Removed values in DB");
//											
//				}catch(Exception ex){
//					System.out.println(ex);
//				}
//			}
//			
//			
//			
//		// load data into DB table from a txt file	
//			public void loadData(String loadStatement)
//		    {
//				String statement = loadStatement;
//				
//				// file to be loaded must be located in wamp server directory for 'exams' database
//				// C:\wamp\bin\mysql\mysql5.1.53\data\exams <- for localhost location on Derek's cpu
//		 
//		        try
//		        {
//		        	String load = ""+statement+"";
//					st.executeUpdate(load);
//					System.out.println("Loaded values in DB");
//		        }
//		        catch(Exception e)
//		        {
//		            e.printStackTrace();
//		            st = null;
//		        }
//		    }

	
	
} // - end class DbConnect


