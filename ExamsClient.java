// MSIS Capstone 498 - Spring 2016 - Northwestern University - Team 4
// 04-25-2016

// ExamsClient class is the class with the main function that runs each class in Exams
// ImportTxtFile objects are created of the .txt files, read and parsed with readFile(), then
// processed into the appropriate columns and tables with ProcessToDb.process(), and inserted  
// into the database with DbConnect.post()
package cis498;

public class ExamsClient {
		
		// begin main method to run Exam Scheduling project files
		 public static void main(String[] args) throws Exception{
			 

//			 // read in .txt files (rooms.txt, courses.txt, buildings.txt)
			 ImportTxtFile importRooms = new ImportTxtFile();
			 importRooms.readFile("sunnaData/rooms.txt");
			 
			 ImportTxtFile importCourses = new ImportTxtFile();
			 importCourses.readFile("sunnaData/courses.txt");
		 
			 ImportTxtFile importBuildings = new ImportTxtFile(); // 26 rows and 2 columns for buildings.txt
			 importBuildings.readFile("sunnaData/buildings.txt"); // insert .txt to read and parse

			 // TEST FILES
//			 ImportTxtFile importRooms = new ImportTxtFile();
//			 importRooms.readFile("testFiles/T01/rooms.txt");
//			 
//			 ImportTxtFile importCourses = new ImportTxtFile();
//			 importCourses.readFile("testFiles/T01/courses.txt");
//		 
//			 ImportTxtFile importBuildings = new ImportTxtFile(); // 26 rows and 2 columns for buildings.txt
//			 importBuildings.readFile("testFiles/T01/buildings.txt"); // insert .txt to read and parse

			 
			 
			 // Send all parsed and individually separated columns of each text file completed in 
			 // ImportTxtFile.readFile() class/method to the ProcessToDb class 
			 ProcessToDb process = new ProcessToDb(importCourses.allColumns, importRooms.allColumns, importBuildings.allColumns);
			 
			 //Insert department table data
			 String insertStmt2 = "INSERT into DEPARTMENT (department_ID, department_Name) VALUES (?,?)";
			 process.process("department", insertStmt2);
    		 // Insert teacher table data
			 String insertStmt3 = "INSERT into TEACHER (teacher_ID, teacher_Name, proctor_Avail_Days) VALUES (?,?,?)";
			 process.process("teacher", insertStmt3);
			// Insert course table data
			 String insertStmt = "INSERT into COURSE (call_Number, course_Number, course_Room_Num, course_Building_Name, course_Meeting_Days, course_Start_Time, course_End_Time, comp_Based_Exam, department_ID) VALUES (?,?,?,?,?,?,?,?,?)";
			 process.process("course", insertStmt); // constructor for insertStmt and table to process
	
			 
		 } // - end main class


} // - end ExamsClient class