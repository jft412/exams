package cis498;

public class Professor {
    int professorId;
    String fullName;
    String department;
    String availability;
    Room[][] timeSlots;

    public Professor(String fullNameIn, String departmentIn, String availabilityIn) {
        fullName = fullNameIn;
        department = departmentIn;
        setAvailability(availabilityIn);
        timeSlots = new Room[5][5];
    }
    
    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorIdIn) {
        professorId = professorIdIn;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullNameIn) {
        fullName = fullNameIn;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String departmentIn) {
        department = departmentIn;
    }

    private void setAvailability(String availabilityIn) {
        if(availabilityIn.equals("ALL")){
            availability = "11111";
        }else{
            availability = availabilityIn.substring(availabilityIn.length() - 5);
        }
    }
    
    public boolean professorFree(int day, int timeslot){
        if(day<5 && day>=0 && timeslot>=0 && timeslot <5){
            return availability.charAt(day)=='1' && 
                getScheduledRoom(day, timeslot)==null && 
                (timeslot>0 ? getScheduledRoom(day, timeslot-1)==null : true) && 
                (timeslot<4 ? getScheduledRoom(day, timeslot+1)==null : true);
        }else{
            System.out.println("Invalid day/timeslot provided: "+ day + ", " + timeslot);
            return false;
        }
    }
    
    public void scheduleRoom(Room classroom, int day, int timeslot){
        if(classroom!=null && day>=0 && day<5 && timeslot>=0 && timeslot<5){
            timeSlots[day][timeslot] = classroom;
        }
    }
    
    public Room getScheduledRoom(int day, int timeslot){
        return timeSlots[day][timeslot];
    }
    
    @Override
    public String toString() {
        return "Prof number=" + professorId +" | Prof Name=" + fullName;
    }
}