package au.edu.curtin.assignment.mathematics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherPage extends AppCompatActivity {

    private Button createStudent, viewStudent, viewTestHistory, back;
    @Override
    protected void onCreate(Bundle x)
    {
        super.onCreate(x);
        setContentView(R.layout.teacher_page);

        back = (Button) findViewById(R.id.backTeacherPage);
        createStudent = (Button) findViewById(R.id.registerStudentTeacher);
        viewStudent = (Button) findViewById(R.id.viewStudentTeacher);
        viewTestHistory = (Button) findViewById(R.id.viewTestHistoryTeacher);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logoff = new Intent(TeacherPage.this,MainActivity.class);
                startActivity(logoff);
            }
        });

        createStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent grab = new Intent(TeacherPage.this,CreateStudent.class);
                startActivity(grab);
            }
        });

        viewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewTestHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}
