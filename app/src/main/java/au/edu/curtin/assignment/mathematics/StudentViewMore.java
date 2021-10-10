package au.edu.curtin.assignment.mathematics;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.List;

import au.edu.curtin.assignment.mathematics.R;
import au.edu.curtin.assignment.mathematics.model.Databases;
import au.edu.curtin.assignment.mathematics.model.Student;

public class StudentViewMore extends AppCompatActivity {
    private Button back, changePicture, addEmailButton, addPhoneButton, saveInformationButton;
    private EditText firstName,lastName,email,phone;
    private RecyclerView emailRV,phoneRV;
    private ImageView viewMoreStudentImage;
    private List<String> emailList = new ArrayList<>();
    private List<String> phoneList = new ArrayList<>();
    private String strFirstName,strLastName,strID;
    private Bitmap btmpStudentImage;
    private Student student;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.more_detail_student);

        back = (Button) findViewById(R.id.backFromStudentViewMore);
        changePicture = (Button) findViewById(R.id.changePictureButton);
        addEmailButton = (Button) findViewById(R.id.addEmailButtonViewMoreStudent);
        addPhoneButton = (Button) findViewById(R.id.addPhoneViewMore);
        saveInformationButton = (Button) findViewById(R.id.saveInfoStudentViewMore);

        firstName = (EditText) findViewById(R.id.editTextViewMoreStudentFirstName);
        lastName = (EditText) findViewById(R.id.editTextViewMoreStudentLastName);
        email = (EditText) findViewById(R.id.editTextViewMoreStudentEmail);
        phone =(EditText) findViewById(R.id.editTextViewMoreStudentPhone);

        emailRV = (RecyclerView) findViewById(R.id.RVViewMoreEmail);
        phoneRV = (RecyclerView) findViewById(R.id.RVPhoneViewMore);
        viewMoreStudentImage = (ImageView) findViewById(R.id.imageViewStudentViewMore);

        //get the data
        strID = getIntent().getExtras().getString("studentID");
        Log.d("1212", "onCreate: "+strID);
        student = Databases.getInstance().findStudentWithID(strID);

        firstName.setText(student.getFirstName());
        lastName.setText(student.getLastName());
        viewMoreStudentImage.setImageBitmap(student.getStudentImage());


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goback = new Intent(StudentViewMore.this,ViewStudent.class);
                startActivity(goback);
            }
        });

    }
    private boolean isValidEmail(String email)
    {
        return EmailValidator.getInstance().isValid(email);
    }

    private String [] SplitName(String DISPLAYNAME)
    {
        return DISPLAYNAME.split("\\s+");
    }

    private String getStringfromEditText(EditText text)
    {
        String res="";
        if (!text.getText().toString().equals(null) || !text.getText().toString().equals(""));
        {
            res = text.getText().toString();
        }

        return res;
    }

}
