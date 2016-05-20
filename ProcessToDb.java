// MSIS Capstone 498 - Spring 2016 - Northwestern University - Team 4
// 04-25-2016

// ProcessToDb class takes the parsed into columns and rows .txt file ArrayLists 
// from each .txt file and, based on a passed insert statement and table identification,
// process them based on their table structure order, then use DbConnect class to 
// post() the ArrayLists into the MySql database
package cis498;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

// this class properly arranges each 
public class ProcessToDb {
	
	// ArrayLists for courses, rooms, buildings
	ArrayList<String[]> courses;
	ArrayList<String[]> rooms;
	ArrayList<String[]> buildings;
		

// ProcessToDb constructor
	public ProcessToDb (ArrayList<String[]> course, ArrayList<String[]> room, ArrayList<String[]> building){		
		courses = course;
		rooms = room;
		buildings = building;		
	}
	
	
	// process method()
	public void process(String table, String stmt) throws Exception{
		
		String name = table;

		
		
		// perform data transformation processes to create columns for our database, then
		// move columns from arraylist .txt files into arraylists for course, teacher, department
		

		
		// Separate start and end times for course times into separate arrays	
		String start = "0000";
		String end = "0000";
		
		String[] times = courses.get(4);
		
		String[] startTime= new String[courses.get(4).length];
		String[] endTime= new String[courses.get(4).length];

		// read each line of the times and parse into start and end times arrays
		for(int i = 1; i < courses.get(4).length; i++){
				
			start = times[i].substring(0,4);
			end = times[i].substring(4);
			
			startTime[i] = start;
			endTime[i] = end;			
			//System.out.println("Start time: "+startTime[i]+ " End time: "+endTime[i]);

		} // end - for
	// end separate start and end times into separate arrays


		

		// course table section 
			
		if(name.equalsIgnoreCase("course")){
			 
			 // remove all null values from each array 
			 String[] call_Number = new String[courses.get(0).length-1];
			 String[] course_Number = new String[courses.get(0).length-1];
			 String[] teacher_Name = new String[courses.get(0).length-1];
			 String[] course_Room_number = new String[courses.get(0).length-1];
			 String[] course_Building_Name = new String[courses.get(0).length-1];
			 String[] course_Meeting_Days = new String[courses.get(0).length-1];
			 String[] course_Start_Time = new String[courses.get(0).length-1];
			 String[] course_End_Time = new String[courses.get(0).length-1];
			 String[] course_Enrollment = new String[courses.get(0).length-1];
			 String[] comp_Based_Exam = new String[courses.get(0).length-1];
			 String[] department_ID = new String[courses.get(0).length-1];
			 String[] schedule_Flag = new String[courses.get(0).length-1];
			 
			 // reposition elements in each array to remove the first row of row names
			 for(int i=1; i < courses.get(0).length; i++){
				 call_Number[i-1]= courses.get(2)[i];
				 course_Number[i-1]= courses.get(0)[i];
				 teacher_Name[i-1]= courses.get(5)[i];
				 course_Room_number[i-1]= courses.get(6)[i];
				 course_Building_Name[i-1]= courses.get(7)[i];
				 course_Meeting_Days[i-1]= courses.get(3)[i];
				 course_Start_Time[i-1]= startTime[i];
				 course_End_Time[i-1]= endTime[i];
				 course_Enrollment[i-1]= courses.get(8)[i];
				 comp_Based_Exam[i-1]= courses.get(10)[i];
				 department_ID[i-1]= courses.get(1)[i];
				 schedule_Flag[i-1]= Integer.toString(1);
			 }
			 
			 // get departmentName and convert to department_ID by using
			 // SELECT statement from department TABLE to choose ID related to dept Name
			 
			 // THIS WILL WORK 04222016 2046pm
			 //		 String query = "SELECT department_ID from department where department_Name ='"+department_ID[0]+"'";
			 //		 rs = selectSt.executeQuery(query);
			 //		 while (rs.next()){
			 //			 rs.getInt("department_ID");
			 //         		     System.out.println("Iteration: "+i+" -DeptNAME:"+department_ID[i]+" -DeptID: "+Integer.toString(rs.getInt("department_ID")));
			 //		 }
			  
			 
			 // select/add dept_ID field values and convert comp_Based_Exam from Y/N to 1/0
			 String[] deptID = new String[department_ID.length];
			 
			 // connection and resultSet objects for DEPARTMENT query
			 Connection conn = DbConnect.getConnection();
			 Statement selectSt = conn.createStatement();
			 ResultSet rs;
			 
			 // selects dept ID from department and matches it with course department Name
			 // and converts Y/N's to 0/1's for comp_Based_Exam field
			 for(int i=0; i < department_ID.length; i++){
				 String query = "SELECT department_ID from DEPARTMENT where department_Name ='"+department_ID[i]+"'";
				 rs = selectSt.executeQuery(query);
				 
					 while (rs.next()){
						 // insert selected ID for dept Name into dept_ID
						 deptID[i] = Integer.toString(rs.getInt("department_ID"));
					 } // end - while
					 
					 // convert comp_Based_Exam Y and N values to 1 and 0
					 if(comp_Based_Exam[i].equalsIgnoreCase("Y")){
						 comp_Based_Exam[i]="1";
					 } else {
						 comp_Based_Exam[i]="0";
					 } // end if/else
					 
			 } // end - for to select/add dept_ID and convert comp_Based_Exam from Y/N to 1/0
		
		     conn.close(); 
		 
		 		 
		     // create arraylist to store course table columns/elements
		     ArrayList<String[]> courseTable = new ArrayList<String[]>();
			 courseTable.add(call_Number); // call_Number
			 courseTable.add(course_Number); // course_Number
			 courseTable.add(teacher_Name); // teacher_Name
			 courseTable.add(course_Room_number); // course_Room_number
			 courseTable.add(course_Building_Name); // course_Building_Name
			 courseTable.add(course_Meeting_Days); // course_Meeting_Days
			 courseTable.add(course_Start_Time); 	 // course_Start_Time
			 courseTable.add(course_End_Time); 		 // course_End_Time
			 courseTable.add(course_Enrollment); 		 // course_Capacity
			 courseTable.add(comp_Based_Exam); // comp_Based_Exam
			 courseTable.add(deptID); // department_ID
			 courseTable.add(schedule_Flag);
			
			 // use DbConnect class post() method to insert into proper table
			 // based on table name and relevant INSERT statement
			 String stmnt = stmt;
			 DbConnect.post(courseTable, stmnt);
			 
	
		}  // end course section	
		
		

		
		
		//department section
		// build Arraylist of department Id and department name (unique names)
		// create array of unique department names (column 2 from courses)
				
		if(name.equalsIgnoreCase("department")){	
		
			String[] array = courses.get(1);
			Set<String> set = new LinkedHashSet<String>(Arrays.asList(array));
			set.removeAll(Collections.singleton(null)); // remove null values
			//System.out.println("Hashset after removal:" +set);
			
			//List<String> list = new ArrayList<String>(set);
			String[] department= set.toArray(new String[set.size()]);
			//System.out.println(buildings[2]); // displays 3rd element in string array
						
			// create ID numbers for each department
			String[] departmentID = new String[department.length];
			
				// create an ID column of null values so auto-increment function works when
				// passed to MySql database
				for(int id = 1; id < department.length; id++){
					//departmentID[id] = Integer.toString(id);
					departmentID[id] = null;
					//System.out.println("Department ID size: " + departmentID.length + "\nDepartment Array size: "+department.length);
				} // end - for 
				
				
				
			// create arraylist to store course table columns/elements
			ArrayList<String[]> departmentTable = new ArrayList<String[]>();		
			departmentTable.add(departmentID);
			departmentTable.add(department); // department ID
			//System.out.println(departmentTable.size());
				
			// use DbConnect class post() method to insert into proper table
			// based on table name and relevant INSERT statement
			String stmnt2 = stmt;
			DbConnect.post(departmentTable, stmnt2);
		
		} // end department section

		
		
		
		
		
		//teachers data table section	
		// split each string by the comma into two arrays
		// insert arrays into arraylist (with auto-increment variable)
		// update database table teachers! :)

		if(name.equalsIgnoreCase("teacher")){		

			// take teacher data and separate into column for name and avail
			String[] arrayNameNull = courses.get(5); // arrayName[0] = null // length 952 - shud be 951
			String[] arrayAvailNull = courses.get(9); // arrayAvail[0] = null // length 952 - shud be 951
		
			String[] arrayName= new String[arrayNameNull.length-1];
			String[] arrayAvail= new String[arrayAvailNull.length-1];
		
				// convert arrayName and arrayAvail arrays to get rid of null value at arrayName[0]
				for(int i=0; i<arrayNameNull.length-1; i++){			
					arrayName[i]= arrayNameNull[i+1];
					arrayAvail[i]= arrayAvailNull[i+1];
					//System.out.println(i+"ArrayName: "+arrayName[i]+" - ArrayAvail: "+arrayAvail[i]);			
				} // end - for remove null value
		
		
				// change all "ALL" strings to 1111110
				for(int i=0; i< arrayName.length; i++){
		
					//System.out.print(i+ "- Avail was "+arrayAvail[i]);
					if(arrayAvail[i].equalsIgnoreCase("ALL")){
						arrayAvail[i] = "1111110";
					}
					//System.out.println(" | Avail is now: "+arrayAvail[i]);			
				} // end - for ALL strings
		
		
			// get data, combine into one string separated by comma
			String[] combined = new String[arrayName.length];
			
				for(int i=0; i<arrayName.length; i++){
					combined[i] = arrayName[i]+"|"+arrayAvail[i];
					//System.out.println(combined[i]);
				} // end - for combine into one string
		
		
			// use hashset like above to find distinct values
			Set<String> combinedSet = new LinkedHashSet<String>(Arrays.asList(combined));
			combinedSet.removeAll(Collections.singleton(null)); // remove null values
			//System.out.println(combinedSet);
			
			// change hashset to String array to insert into Arraylist
			String[] combinedUnique = combinedSet.toArray(new String[combinedSet.size()]);
	
			
			// start here -> try to sort combinedUnique array []
			Arrays.sort(combinedUnique);
			
			// prepare to separate 
			String[] teacherName = new String[combinedUnique.length];
			String[] teacherAvail = new String[combinedUnique.length];
			
			// should be 739 rows
			//System.out.println(combinedUnique.length);
				
				// parse the line by pipe delimiter
				for(int i = 0; i < combinedUnique.length; i++){
					
					String[] items = combinedUnique[i].split("\\|");
					
						// insert items value into teacherName and teacherAvail
						
							// add to String array for availability field
							teacherName[i] = items[0];
							//System.out.println(i+"-Teacher name: "+items[0]+" - TeacherName :"+teacherName[i]);
							// add to String array for teacher name
							teacherAvail[i] = items[1];	
							//System.out.println(i+"-Item availability: "+items[1]+" - TeacherAvail :"+teacherAvail[i]);
						
				} // - end for to separate strings by pipe delimiter
		

		
			// create ID numbers for each teacher - THIS SHOWS ARRAY [0] is ABBOTT (yes!)
			String[] teacherID = new String[combinedUnique.length];
			
				// create an ID column of null values so auto-increment function works when
				// passed to MySql database
				for(int id = 0; id < combinedUnique.length; id++){
						//teacherID[id] = Integer.toString(id);
						teacherID[id] = null;
						//System.out.println(id + "- Teacher ID size: " + teacherID.length + "\nTeacher Array size: "+teacherID.length + " - Teacher NAME: "+teacherName[id]);
				} // end - for teacherID column
	
			
				
			// insert into arrayList to insert into teacher table in database
			ArrayList<String[]> teacherTable = new ArrayList<String[]>();
			teacherTable.add(teacherID); // teacher ID
			teacherTable.add(teacherName); // teacher name
			teacherTable.add(teacherAvail); // teacher availability
			
			// use DbConnect class post() method to insert into proper table
			// based on table name and relevant INSERT statement
			String stmnt3 = stmt;
			DbConnect.post(teacherTable, stmnt3);
				
	 }	// end - teachers table section
		

		
		
		
		
		
// room section
// build Arraylist of room Id and room name (unique names)
// room
				
		if(name.equalsIgnoreCase("rooms")){	
			
			String[] room_BuildingNull = rooms.get(1);
			String[] room_NumberNull = rooms.get(0);
			String[] room_CapacityNull = rooms.get(2);
			String[] room_Mx_DaysNull = rooms.get(3);
			String[] room_MxStart_timeNull = rooms.get(4);
			String[] room_DurationNull = rooms.get(5);
			String[] room_ComputerizedNull = rooms.get(6);
			
		
			String[] room_Building = new String[room_BuildingNull.length-1];
			String[] room_Number = new String[room_BuildingNull.length-1];
			String[] room_Capacity = new String[room_BuildingNull.length-1];
			String[] room_Mx_Days = new String[room_BuildingNull.length-1];
			String[] room_MxStart_time = new String[room_BuildingNull.length-1];
			String[] room_Duration = new String[room_BuildingNull.length-1];
			String[] room_Computerized = new String[room_BuildingNull.length-1];
			String[] room_ID = new String[room_BuildingNull.length-1];
			String[] room_Avail_Sat = new String[room_BuildingNull.length-1];
			
			
			// convert arrayName and arrayAvail arrays to get rid of null value at arrayName[0]
			for(int i=0; i<room_BuildingNull.length-1; i++){			
				room_Building[i]= room_BuildingNull[i+1];
				room_Number[i]= room_NumberNull[i+1];
				room_Capacity[i]= room_CapacityNull[i+1];
				room_Mx_Days[i]= room_Mx_DaysNull[i+1];
				room_MxStart_time[i]= room_MxStart_timeNull[i+1];
				room_Duration[i]= room_DurationNull[i+1];
				room_Computerized[i]= room_ComputerizedNull[i+1];
				room_ID[i]= room_Building[i] + room_Number[i];
			if(room_Building[i].equalsIgnoreCase("ACT") || room_Building[i].equalsIgnoreCase("AHA") || room_Building[i].equalsIgnoreCase("BRN") || room_Building[i].equalsIgnoreCase("EFEN") || room_Building[i].equalsIgnoreCase("FRH") || room_Building[i].equalsIgnoreCase("LHD") || room_Building[i].equalsIgnoreCase("SOKH")){			
				room_Avail_Sat[i] = "Y";
			} else {
				room_Avail_Sat[i] = "N";
			}
				
				//System.out.println(i+"room bldg: "+room_Building[i]+" - room number: "+room_Number[i]);	
				//System.out.println();
			} // end - for remove null value
			
			
					
			// create arraylist to store course table columns/elements
			ArrayList<String[]> roomTable = new ArrayList<String[]>();		
			roomTable.add(room_ID); // room ID
			roomTable.add(room_Building); // room building
			roomTable.add(room_Number); // room building
			roomTable.add(room_Avail_Sat); // room availability on Saturday
			roomTable.add(room_Capacity); // room capacity
			roomTable.add(room_Mx_Days); // room building
			roomTable.add(room_MxStart_time); // room building
			roomTable.add(room_Duration); // room building
			roomTable.add(room_Computerized); // room building					
	
			// use DbConnect class post() method to insert into proper table
			// based on table name and relevant INSERT statement
			String stmnt2 = stmt;
			DbConnect.post(roomTable, stmnt2);
		
		} // end room section
 
  } // end - method process()
	
} // end - class ProcessToDb()
