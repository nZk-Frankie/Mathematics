package au.edu.curtin.assignment.mathematics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import au.edu.curtin.assignment.mathematics.model.Databases;

public class TestResults extends AppCompatActivity {

    private TextView date,score,time;
    private Button finish;
    private String refID,strScore,strDate,strFinsihed;

    private String timeStarted, timeFinished;
    private String hourStarted,minuteStarted, secondStarted, hourFinished,minuteFinished, secondFinished;
    private int hourDiff, minuteDiff, secondDiff;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_results);

        finish = (Button) findViewById(R.id.finishButton);
        date = (TextView) findViewById(R.id.DateTextView);
        score = (TextView) findViewById(R.id.finalResultTextView);
        time =(TextView) findViewById(R.id.timeTextView);

        refID = getIntent().getExtras().getString("refID");
        strScore = getIntent().getExtras().getString("finalScore");
        strDate = getIntent().getExtras().getString("DateStarted");
        strFinsihed = getIntent().getExtras().getString("DateFinished");

        timeStarted = strDate.split("\\s+")[1];
        timeFinished = strFinsihed.split("\\s+")[1];

        hourStarted = timeStarted.split(":")[0];
        minuteStarted = timeStarted.split(":")[1];
        secondStarted= timeStarted.split(":")[2];

        hourFinished = timeFinished.split(":")[0];
        minuteFinished = timeFinished.split(":")[1];
        secondFinished = timeFinished.split(":")[2];
        Log.d("!21", "onCreate: "+secondStarted);
        Log.d("91921",""+secondFinished);
        Log.d("989", "onCreate: "+refID);

        hourDiff = Integer.parseInt(hourFinished) - Integer.parseInt(hourStarted);
        if (hourDiff < 0)
        {
            hourDiff = 24 + hourDiff;
        }
        minuteDiff = Integer.parseInt(minuteFinished) - Integer.parseInt(minuteStarted);
        if (minuteDiff <0 )
        {
            minuteDiff = 60 + minuteDiff;
        }
        secondDiff = Integer.parseInt(secondFinished) - Integer.parseInt(secondStarted);
        if (secondDiff < 0){
            secondDiff = 60 + secondDiff;
        }

        String timexxxxxx = hourDiff+" Hours, "+minuteDiff+" Minute, and "+secondDiff+" Seconds";
        date.setText(strDate);
        score.setText(strScore);
        time.setText(timexxxxxx);


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Databases.getInstance().addTestHistorytoDB(refID,strScore,strDate,timexxxxxx);
                Intent goback = new Intent(TestResults.this,StudentLanding.class);
                startActivity(goback);

                Toast.makeText(getApplicationContext(),"Saving Test Results", Toast.LENGTH_SHORT).show();

            }
        });






    }
}
