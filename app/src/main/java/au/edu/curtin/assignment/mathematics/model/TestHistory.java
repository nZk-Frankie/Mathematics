package au.edu.curtin.assignment.mathematics.model;

public class TestHistory {
    String referenceKey;
    String studentName;
    int finalScore;
    String Date;
    String Time;

    public TestHistory(String ref, int finalScore, String date, String time)
    {
        this.referenceKey = ref;
        this.finalScore = finalScore;
        this.studentName = findStudentName();
        this.Date= date;
        this.Time = time;
    }

    private String findStudentName()
    {
        return Databases.getInstance().findStudentWithID(referenceKey).getFirstName()+" "+Databases.getInstance().findStudentWithID(referenceKey).getLastName();
    }

    public String getReferenceKey() {
        return referenceKey;
    }

    public void setReferenceKey(String referenceKey) {
        this.referenceKey = referenceKey;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String toString()
    {
        String result = "Test Date: " + this.getDate() + "\nStudent Name: "+this.studentName+"\nTest Length: "+this.Time+"\nFinal Score: "+this.finalScore+"\n";

        return result;
    }
}
