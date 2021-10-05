package au.edu.curtin.assignment.mathematics.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Student {
    static String ID;
    String firstName;
    String lastName;
    List<String> phoneNumberList = new ArrayList<>();
    List<String> emailList = new ArrayList<>();
    Bitmap studentImage;


    public Student(String firstName, String lastName)
    {
        this.ID = randomDigit();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private String randomDigit()
    {
        String digit;
        SecureRandom random = new SecureRandom();
        int x = random.nextInt(10000);

        digit = String.format("%05d",x);
        return digit;
    }


    public void setEmailList(List<String> emailList) {
        this.emailList = emailList;
    }

    public static void setID(String ID) {
        Student.ID = ID;
    }

    public void setStudentImage(Bitmap studentImage) {
        this.studentImage = studentImage;
    }

    public void setPhoneNumberList(List<String> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }

    public Bitmap getStudentImage() {
        return studentImage;
    }

    public static String getID() {
        return ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getPhoneNumberList() {
        return phoneNumberList;
    }

    public List<String> getEmailList() {
        return emailList;
    }
}
