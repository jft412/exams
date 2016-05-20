package cis498;

@SuppressWarnings("rawtypes")
public class Room implements Comparable {
    String buildingCode;
    String roomNumber;
    boolean computerized;
    String weekend;
    int capacity;
    String maintenanceDay;
    int maintenanceStartTime;  
    int maintenanceEndTime;
    Section[][] timeSlots;     // array of all possible exam timeslots, which will store a scheduled exam object

    public Room(String buildingCodeIn, String roomNumberIn, boolean computerizedIn, int capacityIn, String maintenanceDayIn, 
                int maintenanceStartTimeIn, int maintenanceEndTimeIn, boolean weekendOk) {
        
        buildingCode = buildingCodeIn;
        roomNumber = roomNumberIn;
        computerized = computerizedIn;
        capacity = capacityIn;
        maintenanceDay = maintenanceDayIn;
        maintenanceStartTime = maintenanceStartTimeIn;
        maintenanceEndTime = maintenanceEndTimeIn;
       
        if(weekendOk){
            //room is available on Saturday, so we'll need 5 days x 5 timeslots
            timeSlots= new Section[4][5];
            weekend = "Y";
        }else{
            //room is not available on Saturday, so we'll only need 4 days x 5 timeslots
            timeSlots= new Section[4][5];
            weekend = "N";
        }
    }

    public int getScheduledDay(Room examRoom, int courseID){
    	int day = 0;
    	for(int i=0; i<examRoom.timeSlots.length; i++){
    		for(int j=0; j<5; j++){
    			if(examRoom.timeSlots[i][j] == null){
    				continue;
    			} else {
    				if(examRoom.timeSlots[i][j].getCallNumber() == courseID)
    				day=i;
    			}
    		}
    	}    	
		return day;
    }
    
    
    public String getScheduledTime(Room examRoom, int courseID){
    	int time = 0;
    	for(int i=0; i<examRoom.timeSlots.length; i++){    		
    		for(int j=0; j<5; j++){    			
    			if(examRoom.timeSlots[i][j] == null){
    				continue;
    			} else {
    				if(examRoom.timeSlots[i][j].getCallNumber() == courseID)
    				time=j;
    			}    		
    		}    	
    	}    	
		return Integer.toString(time);
    }
    
    
    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCodeIn) {
        buildingCode = buildingCodeIn;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumberIn) {
        roomNumber = roomNumberIn;
    }

    public boolean isComputerized() {
        return computerized;
    }
    
    public String weekendAvail() {
        return weekend;
    }

    public void setComputerized(boolean computerizedIn) {
        computerized = computerizedIn;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacityIn) {
        capacity = capacityIn;
    }

    public String getMaintenanceDay() {
        return maintenanceDay;
    }

    public void setMaintenanceDay(String maintenanceDayIn) {
        maintenanceDay = maintenanceDayIn;
    }

    public int getMaintenanceStartTime() {
        return maintenanceStartTime;
    }

    public void setMaintenanceStartTime(int maintenanceStartTimeIn) {
        maintenanceStartTime = maintenanceStartTimeIn;
    }

    public int getMaintenanceEndTime() {
        return maintenanceEndTime;
    }

    public void setMaintenanceEndTime(int maintenanceEndTimeIn) {
        maintenanceEndTime = maintenanceEndTimeIn;
    }   
    
    String getTimeslotDay(int timeslot){
        switch (timeslot) {
            case 0: return "Tuesday";
            case 1: return "Wednesday";
            case 2: return "Thursday";
            case 3: return "Friday";
            case 4: return "Saturday";
            default: return "Not found";
        }
    }
    int getTimeslotStart(int timeslot){
        switch (timeslot) {
            case 0: return 800;
            case 1: return 1000;
            case 2: return 1200;
            case 3: return 1400;
            case 4: return 1600;
            default: return -1;
        }
    }
    String getTimeslotStartSt(String timeslot){
        switch (timeslot) {
            case "0": return "800";
            case "1": return "1000";
            case "2": return "1200";
            case "3": return "1400";
            case "4": return "1600";
            default: return "-1";
        }
    }
    
    int getTimeslotEnd(int timeslot){
        switch (timeslot) {
            case 0: return 930;
            case 1: return 1130;
            case 2: return 1330;
            case 3: return 1530;
            case 4: return 1730;
            default: return -1;
        }
    }
    String getTimeslotEndSt(String timeslot){
        switch (timeslot) {
            case "0": return "930";
            case "1": return "1130";
            case "2": return "1330";
            case "3": return "1530";
            case "4": return "1730";
            default: return "-1";
        }
    }
    
    public void scheduleExam(Section courseSection, int day, int timeslot){
        if(day>=0 && day<5 && timeslot>=0 && timeslot<5){
            timeSlots[day][timeslot] = courseSection;
        }
    }
    
    public Section getScheduledSection(int day, int timeslot){
        if(day>=0 && day<5 && timeslot>=0 && timeslot<5){
            return timeSlots[day][timeslot];
        }else{
            return null;
        }
    }
    
    final String[] days = {"Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    
    public boolean roomFree(int day, int timeslot){
        return getScheduledSection(day, timeslot)==null && 
                (!days[day].equals(getMaintenanceDay()) || 
                (getTimeslotStart(timeslot)>getMaintenanceEndTime() || 
                getTimeslotEnd(timeslot)<getMaintenanceStartTime()));
    }
    
    public int compareTo(Object o) {
        int compareCapacity=((Room)o).getCapacity();
        /* For Ascending order*/
        //return this.capacity-compareCapacity;

        /* For Descending order do like this */
        return compareCapacity-this.capacity;
	}
    
    public String spaces(int n){
        String s = "";
        int i=0;
        for(i=0;i<(25-n);i++){
            s+=" ";
        }
        return s;
    }
    
    public String printSchedule() {
        String out;
        boolean isEmpty = true;
        checkEmpty:
        for(int i=0;i<timeSlots.length;i++){
                for(int j=0;j<timeSlots[i].length;j++){
                    if(timeSlots[i][j]!=null){
                        isEmpty=false;
                        break checkEmpty;
                }
            }
        }
        if(isEmpty){
            out = "No exams scheduled in room "+buildingCode + roomNumber+".";
        }else{
            out = buildingCode + roomNumber + " - Capacity: " + capacity + " - " + (computerized ? "(computerized) ":"") + "Scheduled call numbers:\n";
            for(int i=0;i<timeSlots.length;i++){
                out = out + days[i].substring(0,4) + "\t";
                for(int j=0;j<timeSlots[i].length;j++){
                    out = out + (timeSlots[i][j]==null ? "[___]" : timeSlots[i][j].getCallNumber()) + 
                            spaces((timeSlots[i][j]==null ? "[___]" : String.valueOf(timeSlots[i][j].getCallNumber())).length());
                }
                out = out + "\n\t";
                for(int j=0;j<timeSlots[i].length;j++){
                    if(timeSlots[i][j]!=null){
                        if(timeSlots[i][j].getExamProctor()==null){
                            out=out+"NULL                     ";
                        }else{
                        out = out + timeSlots[i][j].getExamProctor().getFullName() + 
                              spaces(timeSlots[i][j].getExamProctor().getFullName().length());
                        }
                    }else{
                        out = out + " ___ " + "                    ";
                    }
                }
                out = out + "\n";
            }
        }
        return out;
    }
     
	
    @Override
    public String toString() {
        return "Room number=" + roomNumber +" | Building=" + buildingCode +  " | Capacity=" + capacity + " | Saturday=" + weekend;
    }    
}