package cis498;

@SuppressWarnings("rawtypes")
public class Section implements Comparable {
 //course section attributes
 int callNumber; //12941
 Professor instructor;
 Room classroom;
 String department; //Ex: CIS
 int courseNumber; //Ex: 498
 String offeredDays;
 String bucket;
 int startTime;
 int endTime;
 int enrolled;
 boolean computerFinal;
 
 //final exam attributes
 Professor examProctor;
 Room examRoom;

    public Section(int callNumber, Professor instructor, Room classroom, String department, int courseNumber, 
                    String offeredDays, int startTime, int endTime, int enrolled, boolean computerFinal) {
        this.callNumber = callNumber;
        this.instructor = instructor;
        this.classroom = classroom;
        this.department = department;
        this.courseNumber = courseNumber;
        this.offeredDays = offeredDays;
        this.startTime = startTime;
        this.endTime = endTime;
        this.enrolled = enrolled;
        this.computerFinal = computerFinal;
    }
    
    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucketIn) {
        bucket = bucketIn;
    }

    public int getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(int callNumber) {
        this.callNumber = callNumber;
    }

    public Professor getInstructor() {
        return instructor;
    }

    public void setInstructor(Professor instructor) {
        this.instructor = instructor;
    }

    public Room getClassroom() {
        return classroom;
    }

    public void setClassroom(Room classroom) {
        this.classroom = classroom;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getOfferedDays() {
        return offeredDays;
    }

    public void setOfferedDays(String offeredDays) {
        this.offeredDays = offeredDays;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(int enrolled) {
        this.enrolled = enrolled;
    }

    public boolean isComputerFinal() {
        return computerFinal;
    }

    public void setComputerFinal(boolean computerFinal) {
        this.computerFinal = computerFinal;
    }

    public Professor getExamProctor() {
        return examProctor;
    }

    public void setExamProctor(Professor examProctor) {
        this.examProctor = examProctor;
    }
    
    public Room getExamRoom() {
        return examRoom;
    }

    public void setExamRoom(Room examRoom) {
        this.examRoom = examRoom;
    }

    @Override
	public int compareTo(Object o) {
            int compareStart=((Section)o).getStartTime();
            /* For Ascending order*/
            return this.startTime-compareStart;

            /* For Descending order do like this */
            //return compareage-this.studentage;
	}

    @Override
    public String toString() {
        return "Course Time=" + startTime + "-" + endTime +  "| Call Number=" + callNumber+"| Number enrolled="+enrolled+"| Days="+offeredDays+"| Bucket="+bucket;
    }  
    
    
}