package au.edu.curtin.assignment.mathematics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherLanding extends AppCompatActivity {

    Button back, login;
    EditText in1,in2,in3,in4;
    String inS1, inS2, inS3, inS4, inputTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_landing);

        back = (Button) findViewById(R.id.backButton);
        login = (Button) findViewById(R.id.loginTeacherButton);

        in1 = (EditText) findViewById(R.id.input1);
        in2 = (EditText) findViewById(R.id.input2);
        in3 = (EditText) findViewById(R.id.input3);
        in4 = (EditText) findViewById(R.id.input4);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goback = new Intent(TeacherLanding.this,MainActivity.class);
                startActivity(goback);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get input
                inS1 = getStringfromEditText(in1);
                inS2 = getStringfromEditText(in2);
                inS3 = getStringfromEditText(in3);
                inS4 = getStringfromEditText(in4);

                inputTotal = inS1+inS2+inS3+inS4;
                if (inputTotal.equals("2212"))
                {
                    Intent progress = new Intent(TeacherLanding.this,TeacherPage.class);
                    startActivity(progress);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Wrong Passcode", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

