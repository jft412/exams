package cis498;

import java.io.*;
import static java.lang.Integer.parseInt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ParseDU {

    @SuppressWarnings({ "unchecked" })
    public static void main(String[] args) throws Exception{
        String courseData, roomData, buildingData;
        courseData = "sunnaData/courses.txt";
        roomData = "sunnaData/rooms.txt";
        buildingData = "sunnaData/buildings.txt";
                
        ArrayList<Section> courseList = new ArrayList<Section>();
        ArrayList<Room> roomList = new ArrayList<Room>();
        ArrayList<Professor> professorList = new ArrayList<Professor>();
        
        //using sets to easily create unique ArrayLists of these objects
        Set<String> weekendBuildings = new HashSet<>(); //contains only those buildings which are available on the weekend
        Set<String> campusClassrooms = new HashSet<>(); //contains only unique classroom names (building + number) on campus
        
        
        //BUILDING DATA - record the buildings which are open on the weekends
        BufferedReader input = new BufferedReader(new FileReader(new File(buildingData)));
        String line = input.readLine();
        line = input.readLine(); //the first line is headings
	
        while(line!=null){
            String cols[] = line.split("\\|");
            if(cols[1].equals("Y")){
                weekendBuildings.add(cols[0]);
            }
            line = input.readLine();
        }
        
        
        //ROOM DATA - create a unique list of all classrooms
        input = new BufferedReader(new FileReader(new File(roomData)));
        line = input.readLine();
        line = input.readLine(); //the first line is headings
	
        while(line!=null && !line.trim().equals("")){
            /*Data columns:
            Room	0
            Building	1
            Capacity	2
            maintenance_days	3
            Start_time	4
            Duration	5
            Computer_based_exams_enabled    6
            
            public Room(String buildingCodeIn, String roomNumberIn, boolean computerizedIn, int capacityIn, String maintenanceDayIn, 
                int maintenanceStartTimeIn, int maintenanceEndTimeIn, boolean weekendOk) {
            */ 
             
            String cols[] = line.split("\\|");
            
            //sets do not allow duplicates, so only create a new room object if the building+number name doesn't already exist in the set campusClassrooms
            if(campusClassrooms.add(cols[1]+cols[0])){
                roomList.add(new Room(cols[1], cols[0], cols[6].equals("Y"), parseInt(cols[2]), cols[3], parseInt(cols[4]+"00"), parseInt(cols[4]+"00")+parseInt(cols[5]+"00"), weekendBuildings.contains(cols[1]) ));
            }
	    line = input.readLine();
	}
        
        // sort room (roomList) by capacity (largest to smallest)
        Collections.sort(roomList); 
        
        //System.out.println("\nRooms with capacity largest to smallest:");
        
        // print out descending capacity levels per override toString() in Room class
//        for(Room classroom: roomList){
//            //System.out.println(classroom);
//	}
 
        
        //PROFESSOR DATA
        input = new BufferedReader(new FileReader(new File(courseData)));
        line = input.readLine();
        line = input.readLine(); //the first line is headings
        
        Set<String> profNames = new HashSet<>();
	
        while(line!=null && !line.trim().equals("")){
            /*Data columns:
                Course Number [0]
                Department [1]
                Call Number [2]
                Days [3]
                Time [4]
                Instructor [5]
                Room [6]
                Building [7]
                Number enrolled [8]
                Availability during finals week [9]
                Computer_based_exam [10] 
                -blank- [11] 
         
            public Professor(String fullNameIn, String departmentIn, String availabilityIn) {*/

             
            String cols[] = line.split("\\|");
            //sets do not allow duplicates, so only create a new professor object if the name doesn't already exist in the set profNames
            if(profNames.add(cols[5])){
                professorList.add( new Professor(cols[5], cols[1], cols[9]) );
            }
	    line = input.readLine();
	}
        
        
        //COURSE SECTION DATA
        input = new BufferedReader(new FileReader(new File(courseData)));
        line = input.readLine();
        line = input.readLine(); //the first line is headings, so start with line 2
	
        while(line!=null && !line.trim().equals("")){
            /*Data columns:
                Course Number [0]
                Department [1]
                Call Number [2]
                Days [3]
                Time [4]
                Instructor [5]
                Room [6]
                Building [7]
                Number enrolled [8]
                Availability during finals week [9]
                Computer_based_exam [10] 
                -blank- [11] 
            
                Section(int callNumber, Professor instructor, Room classroom, String department, int courseNumber, 
                        String offeredDays, int startTime, int endTime, int enrolled, boolean computerFinal) {
                 */
             
            String cols[] = line.split("\\|");
            Professor profToFind = null;
            for(Professor temp : professorList){
                if(temp.getFullName().equals(cols[5])){
                    profToFind = temp;
                }
            }
            
            Room roomToFind = null;
            for(Room temp : roomList){
                if(temp.getBuildingCode().equals(cols[7]) && temp.getRoomNumber().equals(cols[6])){
                    roomToFind = temp;
                }
            }
            
            if(roomToFind==null || profToFind==null){
                System.out.println("Error matching professor or classroom for this row of data:");
                System.out.println(line);
            }else{
                courseList.add(new Section(parseInt(cols[2]), 
                                           profToFind, 
                                           roomToFind, 
                                           cols[1], 
                                           parseInt(cols[0]), 
                                           cols[3], 
                                           parseInt(cols[4].substring(0,cols[4].length()-4)),
                                           parseInt(cols[4].substring(cols[4].length()-4)),
                                           parseInt(cols[8]),
                                           cols[10].equals("Y")
                                           ));
            }
	    line = input.readLine();
	}    
      
        
        // sort courses (courseList) by start time (ascending start times)
        Collections.sort(courseList); 
        
        System.out.println("Courses inputted: " + courseList.size());
        System.out.println("Professors inputted: " + professorList.size());
        System.out.println("Rooms inputted: " + roomList.size() + "\n");
        
        // print out ascending start times per override toString() in Section class
//        for(Section str: courseList){
//			System.out.println(str);
//	   	}
        
// ARRAYLIST of Arraylists of courses in each bucket
        // create arraylist of courses for each bucket, store these bucket arraylists into
        // a master arraylist
        @SuppressWarnings("rawtypes")
		ArrayList<ArrayList> bucketList= new ArrayList<ArrayList>();
        
        ArrayList<Section> bucketA =  new ArrayList<Section>();
        ArrayList<Section> bucketB =  new ArrayList<Section>();
        ArrayList<Section> bucketC =  new ArrayList<Section>();
        ArrayList<Section> bucketD =  new ArrayList<Section>();
        ArrayList<Section> bucketE =  new ArrayList<Section>();
        ArrayList<Section> bucketF =  new ArrayList<Section>();
        ArrayList<Section> bucketG =  new ArrayList<Section>();
        ArrayList<Section> bucketH =  new ArrayList<Section>();
        ArrayList<Section> bucketI =  new ArrayList<Section>();
        ArrayList<Section> bucketJ =  new ArrayList<Section>();
        ArrayList<Section> bucketK =  new ArrayList<Section>();
        ArrayList<Section> bucketL =  new ArrayList<Section>();
        ArrayList<Section> bucketM =  new ArrayList<Section>();
        ArrayList<Section> bucketN =  new ArrayList<Section>();
        ArrayList<Section> bucketO =  new ArrayList<Section>();
        ArrayList<Section> bucketP =  new ArrayList<Section>();
        ArrayList<Section> bucketQ =  new ArrayList<Section>();
        ArrayList<Section> bucketR =  new ArrayList<Section>();

        System.out.println("Bucket sizes:");
        
        // assign each section to a bucket based on start time and days offered (mon or tue)
        for(Section course: courseList){
        	if(course.getStartTime()<= 800 && (course.getOfferedDays().equals("10100") || (course.getOfferedDays().equals("10101")))){
        		course.setBucket("A");
                        bucketA.add(course);
                }else if(course.getStartTime()>= 830 && course.getStartTime()<= 930 && ((course.getOfferedDays().equals("10100") || (course.getOfferedDays().equals("10101"))))){
        		course.setBucket("B");
                        bucketB.add(course);
                }else if(course.getStartTime()>= 945 && course.getStartTime()<= 1045 && ((course.getOfferedDays().equals("10100") || (course.getOfferedDays().equals("10101"))))){
        		course.setBucket("C");
                        bucketC.add(course);
                }else if(course.getStartTime()>= 1100 && course.getStartTime()<= 1145 && ((course.getOfferedDays().equals("10100") || (course.getOfferedDays().equals("10101"))))){
        		course.setBucket("D");
                        bucketD.add(course);
        	}else if(course.getStartTime()>= 1150 && course.getStartTime()<= 1200 && ((course.getOfferedDays().equals("10100") || (course.getOfferedDays().equals("10101"))))){
        		course.setBucket("E");
                        bucketE.add(course);
        	}else if(course.getStartTime()>= 1230 && course.getStartTime()<= 1300 && ((course.getOfferedDays().equals("10100") || (course.getOfferedDays().equals("10101"))))){
        		course.setBucket("F");
                        bucketF.add(course);
        	}else if(course.getStartTime()>= 1330 && course.getStartTime()<= 1400 && ((course.getOfferedDays().equals("10100") || (course.getOfferedDays().equals("10101"))))){
        		course.setBucket("G");
                        bucketG.add(course);
        	}else if(course.getStartTime()>= 1500 && course.getStartTime()<= 1530 && ((course.getOfferedDays().equals("10100") || (course.getOfferedDays().equals("10101"))))){
        		course.setBucket("H");
                        bucketH.add(course);
        	}else if(course.getStartTime()>= 1600 && course.getStartTime()<= 1645 && ((course.getOfferedDays().equals("10100") || (course.getOfferedDays().equals("10101"))))){
        		course.setBucket("I");
                        bucketI.add(course);
        	}else if(course.getStartTime()<= 800 && course.getOfferedDays().equals("01010")){
        		course.setBucket("J");
                        bucketJ.add(course);
        	}else if(course.getStartTime()>= 900 && course.getStartTime()<= 945 && course.getOfferedDays().equals("01010")){
        		course.setBucket("K");
                        bucketK.add(course);
        	}else if(course.getStartTime()>= 1000 && course.getStartTime()<= 1030 && course.getOfferedDays().equals("01010")){
        		course.setBucket("L");
                        bucketL.add(course);
        	}else if(course.getStartTime()>= 1100 && course.getStartTime()<= 1130  && course.getOfferedDays().equals("01010")){
        		course.setBucket("M");
                        bucketM.add(course);
        	}else if(course.getStartTime()== 1200 && course.getOfferedDays().equals("01010")){
        		course.setBucket("N");
                        bucketN.add(course);
        	}else if(course.getStartTime()>= 1230 && course.getStartTime()<= 1300 && course.getOfferedDays().equals("01010")){
        		course.setBucket("O");
                        bucketO.add(course);
        	}else if(course.getStartTime()>= 1315 && course.getStartTime()<= 1430 && course.getOfferedDays().equals("01010")){
        		course.setBucket("P");
                        bucketP.add(course);
        	}else if(course.getStartTime()>= 1500 && course.getStartTime()<= 1600 && course.getOfferedDays().equals("01010")){
        		course.setBucket("Q");
                        bucketQ.add(course);
        	}else if(course.getStartTime()>= 1630 && course.getOfferedDays().equals("01010")){
        		course.setBucket("R");    
                        bucketR.add(course);                        
                }else{
                    System.out.println("Couldn't bucket course: " + course);
                }
        	//System.out.println("Bucketed successfully: " + course);
        }
        
        // Add courses in each bucket to bucketList ArrayList
        bucketList.add(bucketA);
        bucketList.add(bucketB);
        bucketList.add(bucketC);
        bucketList.add(bucketD);
        bucketList.add(bucketE);
        bucketList.add(bucketF);
        bucketList.add(bucketG);
        bucketList.add(bucketH);
        bucketList.add(bucketI);
        bucketList.add(bucketJ);
        bucketList.add(bucketK);
        bucketList.add(bucketL);
        bucketList.add(bucketM);
        bucketList.add(bucketN);
        bucketList.add(bucketO);
        bucketList.add(bucketP);
        bucketList.add(bucketQ);
        bucketList.add(bucketR);
        
        // print out courses in each bucket and the bucket size for verification/accuracy
        for(@SuppressWarnings("rawtypes") ArrayList bucket: bucketList){
            //System.out.println(bucket.toString()); 
            System.out.println("Bucket size: "+bucket.size());
        }

       
//  ___  _   ___ ___   _ 
// | _ \/_\ / __/ __| / |
// |  _/ _ \\__ \__ \ | |
// |_|/_/ \_\___/___/ |_|
//
// look at normal classroom, and if so, try the normal professor 

    int examDay = 0;
    int examSlotNumber = 0;
    
    for(ArrayList<Section> currentBucket : bucketList){ 
        //loop through all buckets
        Iterator<Section> it = currentBucket.iterator(); 
        Section currentSection;
        while(it.hasNext()) {
            currentSection = it.next();
            if(currentSection.getClassroom().roomFree(examDay, examSlotNumber)){ 
                //the regular classroom is free!
            	if(currentSection.isComputerFinal()){
                    //if the section needs a computerized final
                    if(currentSection.getClassroom().isComputerized() && 
                        currentSection.getClassroom().getCapacity() >= currentSection.getEnrolled()){
                        //classroom is computerized as we need, and it's big enough, so schedule the section for now
                        currentSection.getClassroom().scheduleExam(currentSection, examDay, examSlotNumber);
                        currentSection.setExamRoom(currentSection.getClassroom());
                        
                        //check normal professor, just for giggles
                        if(currentSection.getInstructor().professorFree(examDay, examSlotNumber)){
                            //professor is free, so just do the rest of scheduling
                            currentSection.getInstructor().scheduleRoom(currentSection.getExamRoom(), examDay, examSlotNumber);        
                            currentSection.setExamProctor(currentSection.getInstructor());
                            //remove the section from bucket and future passes
                            it.remove();
                        }else{
                            //normal professor is not free, so we'll leave the section/room scheduled and move on
                        }                   
                    }else{
                        //normal room is unsuitable - not free, or not big enough
                    	//hogging room?
                    }
                }else{ 
                    //section does not need computerized room
                    if(currentSection.getClassroom().getCapacity() >= 2*currentSection.getEnrolled()){
                        //the normal classroom is big enough, so schedule the for now
                        currentSection.getClassroom().scheduleExam(currentSection, examDay, examSlotNumber);
                        currentSection.setExamRoom(currentSection.getClassroom());
                            
                        //check normal professor, just for giggles 
                        if(currentSection.getInstructor().professorFree(examDay, examSlotNumber)){
                            //professor is free, so just do the rest of scheduling
                            currentSection.getInstructor().scheduleRoom(currentSection.getExamRoom(), examDay, examSlotNumber);        
                            currentSection.setExamProctor(currentSection.getInstructor());
                            //remove the section from bucket and future passes
                            it.remove();
                        }else{
                            //normal professor is not free, so we'll leave the section/room scheduled and move on
                        }
                    }else{
                        // normal classroom is not suitable
                    	// hogging room?
                    }
                } // - end ifelse if(currentSection.isComputerFinal())
            } // - end if(currentSection.getClassroom().roomFree(examDay, examSlotNumber))
        } // - end for currentBucket
                
//  ___  _   ___ ___   ___ 
// | _ \/_\ / __/ __| |_  )
// |  _/ _ \\__ \__ \  / / 
// |_|/_/ \_\___/___/ /___|
//   
// look at normal professor for availability
        
        it = currentBucket.iterator(); 
        while(it.hasNext()) {
            currentSection = it.next(); 
            if(currentSection.getInstructor().professorFree(examDay, examSlotNumber)){
                //the original professor is available, so schedule him/her
                currentSection.setExamProctor(currentSection.getInstructor());
            }else{
                //the original professor is not available at this time
            }
        } // - end while(it.hasNext())
        
//  ___  _   ___ ___   ____
// | _ \/_\ / __/ __| |__ /
// |  _/ _ \\__ \__ \  |_ \
// |_|/_/ \_\___/___/ |___/
//          
// all possible ideal scheduling has been done (between sections and their original instructors, and their original classrooms)
// now time to fill in the pieces and find alternates where needed

        it = currentBucket.iterator(); 
        while(it.hasNext()) {
            currentSection = it.next(); 
            if(currentSection.getExamRoom()==null){
                //find a classroom
                
                Room currentRoom = null;
                for(Room currentRoomTemp : roomList){ 
                    //count down to the smallest possible classroom that can accommodate this section
                    if(currentRoomTemp.getCapacity()>=currentSection.getEnrolled()){
                        currentRoom = currentRoomTemp;
                    }else{
                        break;
                    }
                } // - end for(Room currentRoomTemp : roomList)
                
                int i;
                for(i=roomList.indexOf(currentRoom);i>=0;i--){ 
                    //start with currentRoom and start looking at increasingly larger rooms until one is suitable and free
                    currentRoom = roomList.get(i);
                    
                    if(currentRoom.roomFree(examDay, examSlotNumber)){
                        if(currentSection.isComputerFinal()){
                            if(currentRoom.isComputerized()){
                                //computerized room located, so schedule it
                                currentRoom.scheduleExam(currentSection, examDay, examSlotNumber);
                                currentSection.setExamRoom(currentRoom);
                                if(currentSection.getExamProctor()!=null){
                                    currentSection.getExamProctor().scheduleRoom(currentRoom, examDay, examSlotNumber);
                                    it.remove();
                                }
                                break;
                            }else{
                                //need computerized room, so this isn't the one
                            }
                        }else{
                            if(currentRoom.getCapacity() >= 2*currentSection.getEnrolled()){
                                //any room (computerized or not) will do, so long as it's big enough
                                currentRoom.scheduleExam(currentSection, examDay, examSlotNumber);
                                currentSection.setExamRoom(currentRoom);
                                if(currentSection.getExamProctor()!=null){
                                    currentSection.getExamProctor().scheduleRoom(currentRoom, examDay, examSlotNumber);
                                    it.remove();
                                }
                                break;
                            }
                        }
                    }
                //currentSection.setExamProctor(currentSection.getInstructor());
                } // - end for(i=roomList.indexOf(currentRoom);i>=0;i--)
            } // - end if(currentSection.getExamRoom()==null)  (END LOOP TO FIND A ROOM)
            
            if(currentSection.getExamProctor()==null && currentSection.getExamRoom()!=null){
                //if proctor still unscheduled, but we have a room scheduled, find a proctor (otherwise skip, since it won't schedule anyway without a room)
                for(Professor currentProf : professorList){
                    //loop through all professors
                    if(currentProf.getDepartment().equals(currentSection.getDepartment())){
                        //only check this person if his/her department is correct
                        if(currentProf.professorFree(examDay, examSlotNumber)){
                            //current professor is free for the slot, so schedule!
                            currentSection.setExamProctor(currentProf);
                            currentProf.scheduleRoom(currentSection.getExamRoom(), examDay, examSlotNumber);
                            it.remove();
                            break;
                        }
                    }
                } // - end for(Professor currentProf : professorList)  
            } // - if(currentSection.getExamProctor())
        } // - end while(it.hasNext())
    

//  ___  _   ___ ___    ___ 
// | _ \/_\ / __/ __|  / , |_
// |  _/ _ \\__ \__ \ /__   _|
// |_|/_/ \_\___/___/    |_|
//       
// Clean up pass

    it = currentBucket.iterator(); 
        while(it.hasNext()) {
            currentSection = it.next();
            if(currentSection.getExamRoom()!=null && currentSection.getExamProctor()!=null){
                //System.out.println("Scheduled, pass 4: " + currentSection.getCallNumber() + "/" + currentSection.getExamProctor().getFullName() + "/" + currentSection.getExamProctor().getScheduledRoom(examDay, examSlotNumber));
                // course has an exam room and a proctor, so it's scheduled
                it.remove();
            }else if(currentSection.getExamRoom()!=null && currentSection.getExamProctor()==null){
                currentSection.setFlag("No available proctor.");
                currentSection.getExamRoom().scheduleExam(null, examDay, examSlotNumber);
                currentSection.setExamRoom(null);
            }else if(currentSection.getExamRoom()==null && currentSection.getExamProctor()!=null){
                currentSection.setFlag("No available classroom.");
                currentSection.getExamProctor().scheduleRoom(null, examDay, examSlotNumber);
                currentSection.setExamProctor(null);
            }else{
                currentSection.setFlag("Neither a proctor nor a classroom is available.");
            }
        } // - end while(it.hasNext())
    
        //next bucket is assigned to the next timeslot in the week's schedule
        examSlotNumber++;
        if(examSlotNumber>=5){
            examDay++;
            examSlotNumber=0;
        }
    } // End the loop through all the buckets (for(ArrayList<Section> currentBucket : bucketList) - line 313)

        int count = 0;
        
        System.out.println("\nUnscheduled courses:");
        for(ArrayList<Section> currentBucket : bucketList){ 
            //loop through all buckets
            Iterator<Section> it = currentBucket.iterator(); 
            Section currentSection;
            while(it.hasNext()) {
                currentSection = it.next();
                System.out.println(currentSection + currentSection.getFlag());
                count++;
            }
            //System.out.println("");
        } // - end for(ArrayList<Section> currentBucket : bucketList) 
        System.out.println("\nTotal courses still unscheduled: " + count + "\n");

        for(Room currentRoom : roomList){ 
            System.out.println(currentRoom.printSchedule());
        }
        
  
//        
//        
//// get data values to INSERT into database tables        
//        
//       // count how many courses have been scheduled
//        int countSchedSections = 0; 
//        for(Section currentSection : courseList){
//        	if(currentSection.getExamRoom()!=null){
//        		countSchedSections++;
//        	}
//        }
//  		
//        // create arrays to store values for each table column (array size based on # sections scheduled)
//        String[] call_Number = new String[countSchedSections];
//        String[] room_Number = new String[countSchedSections];
//        String[] weekend = new String[countSchedSections];
//        String[] exam_Start = new String[countSchedSections];
//        String[] exam_End = new String[countSchedSections];
//        String[] building = new String[countSchedSections];
//        String[] capacity = new String[countSchedSections];
//        String[] cpu = new String[countSchedSections];
//        String[] mx_Day = new String[countSchedSections];
//        String[] mx_Start = new String[countSchedSections];
//        String[] mx_End = new String[countSchedSections];
//    
//        // create Exam ID numbers for each exam
//		String[] exam_ID = new String[countSchedSections];
//		// create an Exam ID column of null values so auto-increment function works when
//		// passed to MySql database
//		for(int id = 0; id < countSchedSections; id++){
//			exam_ID[id] = null;
//		} // end - for 
//		
//		
//        int arrayCount = 0;
//      // for exam_room table INSERT statement  
//      for(Section currentSection : courseList){
//   	  
//    	      	  
//    	// create arraylist containing these values in this order
//    	// these will be inserted into exam_room table
//    	
//    	  if(currentSection.getExamRoom()!=null){
//    		 
////    		System.out.println("Room number: "+currentSection.getExamRoom().getRoomNumber());
//    		  room_Number[arrayCount] = String.valueOf(currentSection.getExamRoom().getRoomNumber());
////    		System.out.println("Room available on Saturday? "+currentSection.getExamRoom().weekendAvail());
//    		  weekend[arrayCount] = String.valueOf(currentSection.getExamRoom().weekendAvail());
////    		System.out.println("Room start time: "+currentSection.getStartTime());
//    		  exam_Start[arrayCount] = String.valueOf(currentSection.getStartTime());
////    		System.out.println("Room end time: "+currentSection.getCallNumber());
//    		  exam_End[arrayCount] = String.valueOf(currentSection.getEndTime());
////    		System.out.println("Building code: "+currentSection.getCallNumber());
//    		  building[arrayCount] = String.valueOf(currentSection.getExamRoom().getBuildingCode());
////    		System.out.println("Room capacity: "+currentSection.getExamRoom().getCapacity());
//    		  capacity[arrayCount] = String.valueOf(currentSection.getExamRoom().getCapacity());
////    		System.out.println("Room computerized? "+currentSection.getExamRoom().computerized);
//    		  cpu[arrayCount] = String.valueOf(currentSection.getExamRoom().computerized);
////    		System.out.println("Room mx day: "+currentSection.getExamRoom().getMaintenanceDay());
//    		  mx_Day[arrayCount] = String.valueOf(currentSection.getExamRoom().getMaintenanceDay());
////    		System.out.println("Room mx start time: "+currentSection.getExamRoom().getMaintenanceStartTime());
//    		  mx_Start[arrayCount] = String.valueOf(currentSection.getExamRoom().getMaintenanceStartTime());
////    		System.out.println("Room mx end time: "+currentSection.getExamRoom().getMaintenanceEndTime());
//    		  mx_End[arrayCount] = String.valueOf(currentSection.getExamRoom().getMaintenanceEndTime());
////   		System.out.println("Section call number: "+currentSection.getCallNumber());
//    		  call_Number[arrayCount] = String.valueOf(currentSection.getCallNumber());
//    		  
//    		  arrayCount++;
//    		
//    	}
//        	
//    	
//    }
//        
//
//      // create arraylist to store course table columns/elements
//	     ArrayList<String[]> examRoomTable = new ArrayList<String[]>();
//	     examRoomTable.add(exam_ID); // call_Number
//	     examRoomTable.add(building); // course_Number
//	     examRoomTable.add(room_Number); // course_Room_number
//	     examRoomTable.add(weekend); // course_Building_Name
//	     examRoomTable.add(exam_Start); // course_Meeting_Days
//	     examRoomTable.add(exam_End); 	 // course_Start_Time
//	     examRoomTable.add(capacity); 		 // course_End_Time
//	     examRoomTable.add(cpu); // comp_Based_Exam
//	     examRoomTable.add(mx_Day); // department_ID
//	     examRoomTable.add(mx_Start); // department_ID
//	     examRoomTable.add(mx_End); // department_ID
//	     examRoomTable.add(call_Number); // department_ID
//		
//		 // use DbConnect class post() method to insert into proper table
//		 // based on table name and relevant INSERT statement
//		 //System.out.println(courseTable.size());
//	  // Insert exam_room table data
//		 String insertStmt = "INSERT into EXAM_ROOM (exam_ID, building_Name, room_Number, Avail_Sat, room_Avail_Start_Time, room_Avail_End_Time, capacity, computer_Enabled, maint_Day, maint_Start_Time, maint_End_Time, course_Call_Num) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
//	     
//		 String stmnt = insertStmt;
//		 try {
//			DbConnect.post(examRoomTable, stmnt);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//      
//
//	
//		 
//		 
//// get data values to INSERT into EXAM_SCHEDULE table        
// 			Connection conn2 = DbConnect.getConnection();
// 			Statement selectSt2 = conn2.createStatement();
// 			Statement selectSt3 = conn2.createStatement();
// 			ResultSet rs2;
// 			ResultSet rs3;
//	  		
//	        // create arrays to store values for each table column (array size based on # sections scheduled)
//	        String[] exam_Day = new String[countSchedSections];
//	        String[] exam_Start_Time = new String[countSchedSections];
//	        String[] exam_End_Time = new String[countSchedSections];
//	        String[] department_ID = new String[countSchedSections];
//	        String[] teacher_ID = new String[countSchedSections];
//	        String[] proctor_Sub = new String[countSchedSections];
//			
//			
//	        int arrayCount2 = 0;
//	      // for exam_room table INSERT statement  
//	      for(Section currentSection : courseList){
//	    	
//	    	//  if(currentSection.getExamRoom()!=null){
//	    	  if(currentSection.getExamRoom()!=null){
//	    		
//	    		// get timeslot[][] - determine slot where call_Number located, i=day, j=time
//	    		// start_time and end_time found with getTimeslotEnd() to identify actual times
//			    //.timeSlots[3][1].getCallNumber());
////	    		System.out.println("Room info: "+currentSection.getExamRoom());
//	    		
////	    		System.out.println("Exam Day: "+currentSection.getExamRoom().getScheduledDay(currentSection.getExamRoom(), currentSection.getCallNumber()));
////	    		exam_Day[arrayCount2] = currentSection.getExamRoom().getScheduledDay(currentSection.getExamRoom(), currentSection.getCallNumber());
//
////	    		System.out.println("Exam Day Name: "+currentSection.getExamRoom().getTimeslotDay(currentSection.getExamRoom().getScheduledDay(currentSection.getExamRoom(), currentSection.getCallNumber())));
//     		    exam_Day[arrayCount2] = currentSection.getExamRoom().getTimeslotDay(currentSection.getExamRoom().getScheduledDay(currentSection.getExamRoom(), currentSection.getCallNumber()));
//	    		
////	    		System.out.println("Exam Time: "+currentSection.getExamRoom().getScheduledTime(currentSection.getExamRoom(), currentSection.getCallNumber()));
////	    		System.out.println("Exam Time Start: "+currentSection.getExamRoom().getTimeslotStart(currentSection.getExamRoom().getScheduledTime(currentSection.getExamRoom(), currentSection.getCallNumber())));
//     		    exam_Start_Time[arrayCount2] = currentSection.getExamRoom().getTimeslotStartSt(currentSection.getExamRoom().getScheduledTime(currentSection.getExamRoom(), currentSection.getCallNumber()));
//     		    
////	    		System.out.println("Exam Time: "+currentSection.getExamRoom().getScheduledTime(currentSection.getExamRoom(), currentSection.getCallNumber()));
////	    		System.out.println("Exam Time End: "+currentSection.getExamRoom().getTimeslotEnd(currentSection.getExamRoom().getScheduledTime(currentSection.getExamRoom(), currentSection.getCallNumber())));
//	    		exam_End_Time[arrayCount2] = currentSection.getExamRoom().getTimeslotEndSt(currentSection.getExamRoom().getScheduledTime(currentSection.getExamRoom(), currentSection.getCallNumber()));
//
//	    		
////	    		System.out.println("department_ID? "+currentSection.department);
//	    // select department_ID from Department table where department_Name = department
//	    		// connection and resultSet objects
//
//	 			String query2 = "SELECT department_ID from DEPARTMENT where department_Name ='"+currentSection.department+"'";
//			    rs2 = selectSt2.executeQuery(query2);
//			    while (rs2.next()){
//			    department_ID[arrayCount2] = Integer.toString(rs2.getInt("department_ID"));
//			    }
////			    conn2.close(); 
//	    		
////	    		department_ID[arrayCount2] = String.valueOf(currentSection.department);
// 		  	     		  	    
// 		  	    
// 		  	    
////	    		System.out.println("Instructor: "+currentSection.instructor.getFullName());
//			    String query3 = "SELECT teacher_ID from TEACHER where teacher_Name ='"+currentSection.instructor.getFullName()+"'";
//			    rs3 = selectSt3.executeQuery(query3);
//			    while (rs3.next()){
//			    teacher_ID[arrayCount2] = Integer.toString(rs3.getInt("teacher_ID"));
//			    }
//	    		//teacher_ID[arrayCount2] = String.valueOf(currentSection.instructor);
//	    		  
//	    		// Proctor a substitute proctor?
//		    		String substitute;
//		    		if(currentSection.getExamProctor() == currentSection.instructor){
//		    			substitute = "F";
//		    		} else {
//		    			substitute = "T";
//		    		}		    		
////		    	System.out.println("Room end time: "+currentSection.getCallNumber());
//		    	  proctor_Sub[arrayCount2] = String.valueOf(substitute); 
////	    		System.out.println("call number: "+currentSection.getCallNumber());
//	    		  call_Number[arrayCount2] = String.valueOf(currentSection.getCallNumber());
//	    		  
//	    		  arrayCount2++;	    		
//	    	}	    	
//	    }		 
//		  	
//// INSERT into the EXAM_SCHEDULE 	    	
//	   // create arraylist to store course table columns/elements
//		     ArrayList<String[]> examScheduleTable = new ArrayList<String[]>();
//		     examScheduleTable.add(exam_Day); // call_Number
//		     examScheduleTable.add(exam_Start_Time); // course_Number
//		     examScheduleTable.add(exam_End_Time); // course_Room_number
//		     examScheduleTable.add(department_ID); // course_Building_Name
//		     examScheduleTable.add(teacher_ID); // course_Meeting_Days
//		     examScheduleTable.add(proctor_Sub); 	 // course_Start_Time
//		     examScheduleTable.add(call_Number); 		 // course_End_Time
//			
//			 // use DbConnect class post() method to insert into proper table
//			 // based on table name and relevant INSERT statement
//			 //System.out.println(courseTable.size());
//		  // Insert exam_room table data
//			 String insertStmt2 = "INSERT into EXAM_SCHEDULE (exam_Day, exam_Start_Time, exam_End_Time, department_ID, teacher_ID, proctor_Sub, course_Call_Number) VALUES (?,?,?,?,?,?,?)";
//		     
//			 String stmnt2 = insertStmt2;
//			 try {
//				DbConnect.post(examScheduleTable, stmnt2);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
//		 


    } // - end main

} // - end class ParseDU

	

