package au.edu.curtin.assignment.mathematics.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Student{
    public String ID;
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

    public Student (String ID, String firstName, String LastName, String Image)
    {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = LastName;
        this.studentImage = loadImage(Image);

    }

    private Bitmap loadImage(String imageName)
    {
        Bitmap studentImage=null;
        String ImagePath = "data/data/au.edu.curtin.assignment.mathematics/app_studentImageDirectory/";
        try{
            File imageFile = new File(ImagePath,imageName);
            studentImage = BitmapFactory.decodeStream(new FileInputStream(imageFile));
        }catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return studentImage;
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

    public void setStudentImage(Bitmap studentImage) {
        this.studentImage = studentImage;
    }

    public void setPhoneNumberList(List<String> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }

    public Bitmap getStudentImage() {
        return studentImage;
    }

    public String getID() {
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
