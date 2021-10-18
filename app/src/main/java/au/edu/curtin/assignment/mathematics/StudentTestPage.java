package au.edu.curtin.assignment.mathematics;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.FontRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import au.edu.curtin.assignment.mathematics.Util.DownloadUtils;


public class StudentTestPage extends AppCompatActivity {

    private String urlString = Uri.parse("https://140.238.205.172:8000/random/question").buildUpon().build().toString();
    private Button pass, finish,start;
    private TextView questionTextView, scoreTextView, title;
    boolean testStarted =false;

    private int score = 0;
    private CountDownTimer timer;
    private byte [] buffer = new byte[1024];
    private int bytesRead;
    private String question, result;
    private int TTS, nOptions;
    private List<String> listofOption = new ArrayList<>();

    private String studentAnswer = "";
    private String refID;

    //Test Answer UI Variable
    private Button option1,option2,option3,option4,submitEdit,submitMC, nextPageButton, prevPageButton;
    private EditText answersEditText, editTextMC;
    private List<Button> listofOptionalButton = new ArrayList<>();
    private List<Integer> nOptionEachPage = new ArrayList<>();
    private int currPage =0;
    private int maxPage;

    //RANDOM
    private String dateTestStarted;
    private String dateTestFinished;


    private String getQuestion()
    {
        String result = null;
        try {
            result = new DonwloadThread().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (nOptions==1)
        {
            try {
                result = new DonwloadThread().execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private void showQuestion()
    {
        if (getQuestion().equals("Success"))
        {
            questionTextView.setText(question);
            generateListnumberPages();
            Log.d("N Options", "showQuestion: "+ listofOption.size());
            Log.d("PAGE", "showQuestion: Pages:" +maxPage);
            generateAnswerLayout();

            timer = new CountDownTimer(TTS*1000,1000)
            {
                @Override
                public void onTick(long l) {
                    int seconds = (int) (l / 1000);
                    title.setText("Time Remaining: "+seconds+" Seconds");
                }

                @Override
                public void onFinish() {
                    showQuestion();
                }

            }.start();

        }
    }

    private void generateAnswerLayout()
    {
        if (listofOption.size()>1)
        {
            multipleChoiceSetUp();
            generatePage();
        }
        else
        {
            setUpWrittenAnswer();
        }
    }

    private void generateNavigationButton()
    {
        if (currPage==0)
        {
            prevPageButton.setVisibility(View.GONE);
            nextPageButton.setVisibility(View.VISIBLE);
            if (nOptionEachPage.size()-1==0)
            {
                nextPageButton.setVisibility(View.GONE);
            }
        }
        else if (currPage==nOptionEachPage.size()-1)
        {
            prevPageButton.setVisibility(View.VISIBLE);
            nextPageButton.setVisibility(View.GONE);
        }
        else
        {
            prevPageButton.setVisibility(View.VISIBLE);
            nextPageButton.setVisibility(View.VISIBLE);
        }
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

    private void generatePage()
    {
        setAllOptionalButtonGone();
        generateNavigationButton();
        for (int i=0; i<nOptionEachPage.get(currPage); i++)
        {
            listofOptionalButton.get(i).setVisibility(View.VISIBLE);
        }
        int sum =0;
        for (int i=0; i<currPage+1;i++)
        {
            sum += nOptionEachPage.get(i);
        }
        Log.d("121", "generatePage: "+sum);
        for (int i=4*currPage; i<sum; i++)
        {
            listofOptionalButton.get(i%4).setText(listofOption.get(i));
        }

    }

    private void setAllOptionalButtonGone()
    {
        option1.setVisibility(View.GONE);
        option2.setVisibility(View.GONE);
        option3.setVisibility(View.GONE);
        option4.setVisibility(View.GONE);
    }

    private void multipleChoiceSetUp()
    {
        answersEditText.setVisibility(View.GONE);
        submitEdit.setVisibility(View.GONE);


        editTextMC.setVisibility(View.VISIBLE);
        editTextMC.setClickable(false);
        editTextMC.setFocusable(false);
        nextPageButton.setVisibility(View.VISIBLE);
        prevPageButton.setVisibility(View.VISIBLE);
        submitMC.setVisibility(View.VISIBLE);
    }

    private void setUpWrittenAnswer()
    {
        answersEditText.setVisibility(View.VISIBLE);
        submitEdit.setVisibility(View.VISIBLE);

        option1.setVisibility(View.GONE);
        option2.setVisibility(View.GONE);
        option3.setVisibility(View.GONE);
        option4.setVisibility(View.GONE);
        nextPageButton.setVisibility(View.GONE);
        prevPageButton.setVisibility(View.GONE);
        submitMC.setVisibility(View.GONE);
        editTextMC.setVisibility(View.GONE);
    }

    private void generateListnumberPages()
    {
        //maximum number of pages is 3
        if (!nOptionEachPage.isEmpty())
        {
            nOptionEachPage.clear();
        }
        List<String> temp = new ArrayList<>();
        if (!listofOption.isEmpty())
        {
            for (int i=0; i<listofOption.size();i++)
            {
                temp.add(listofOption.get(i));
                if (temp.size()==4)
                {
                    maxPage+=1;
                    nOptionEachPage.add(4);
                    temp.clear();
                }
            }
            if (!temp.isEmpty())
            {
                maxPage+=1;
                nOptionEachPage.add(temp.size());
                temp.clear();
            }
        }
        Log.d("121", "generateListnumberPages: "+nOptionEachPage.toString());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_test_page);

        pass = (Button) findViewById(R.id.passQuestionButton);
        start = (Button)findViewById(R.id.startButtonStudentTest);
        finish = (Button) findViewById(R.id.finishButtonStudentTest);

        questionTextView = (TextView) findViewById(R.id.QuestionTextView);
        scoreTextView = (TextView)findViewById(R.id.studentScoreTextView);
        title = (TextView)findViewById(R.id.textView12121);

        scoreTextView.setText(String.valueOf(score));

        //initialse the answer UI
        option1 = (Button)findViewById(R.id.option1Button);
        option2 = (Button) findViewById(R.id.option2Button);
        option3 = (Button)findViewById(R.id.option3Button);
        option4 = (Button) findViewById(R.id.option4Button);

        answersEditText = (EditText)findViewById(R.id.editTextAnswer);

        submitEdit = (Button) findViewById(R.id.submitButtonEditText);
        submitMC = (Button)findViewById(R.id.submitButtonMultipleChoice);

        nextPageButton = (Button) findViewById(R.id.nextPageButton);
        prevPageButton = (Button) findViewById(R.id.prevPageButton);
        editTextMC = (EditText) findViewById(R.id.editTextForMC);

        listofOptionalButton.add(option1);
        listofOptionalButton.add(option2);
        listofOptionalButton.add(option3);
        listofOptionalButton.add(option4);

        setBeforeTheTest();

        refID = getIntent().getExtras().getString("refID");

        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currPage+=1;
                generatePage();

            }
        });

        prevPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currPage-=1;
                generatePage();
            }
        });

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentAnswer = (String) option1.getText();
                editTextMC.setText(studentAnswer);
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentAnswer = (String) option2.getText();
                editTextMC.setText(studentAnswer);
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentAnswer = (String)option3.getText();
                editTextMC.setText(studentAnswer);

            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentAnswer = (String)option4.getText();
                editTextMC.setText(studentAnswer);
            }
        });

        submitMC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (studentAnswer.equals(result))
                {
                    timer.cancel();
                    score = score+10;
                    scoreTextView.setText(String.valueOf(score));
                    showQuestion();
                }
                else if (!studentAnswer.equals(result))
                {
                    if (studentAnswer.equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"Please provide an Answer",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        timer.cancel();
                        score = score - 5;
                        if (score < 0) {
                            score = 0;
                        }
                        scoreTextView.setText(String.valueOf(score));
                        showQuestion();
                    }
                }
                editTextMC.setText("");
            }
        });

        submitEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String answers = getStringfromEditText(answersEditText);
                if (answers.equals(result))
                {
                    timer.cancel();
                    score= score+10;
                    scoreTextView.setText(String.valueOf(score));
                    showQuestion();
                }
                else if (!answers.equals(result))
                {
                    if (answers.equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"Please provide an Answer",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        timer.cancel();
                        score = score - 5;
                        if (score < 0) {
                            score = 0;
                        }
                        scoreTextView.setText(String.valueOf(score));
                        showQuestion();}
                }

                answersEditText.setText("");
            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testStarted = true;
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.now();
                dateTestStarted = dateTimeFormatter.format(localDateTime);

                setThingsAfterStart();
                showQuestion();

                Toast.makeText(getApplicationContext(), "Starting Test", Toast.LENGTH_SHORT).show();

            }
        });
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                showQuestion();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                testStarted =false;
                setBeforeTheTest();
                start.setVisibility(View.VISIBLE);

                DateTimeFormatter finish = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.now();
                dateTestFinished = finish.format(localDateTime);

                Toast.makeText(getApplicationContext(), "Test Finished", Toast.LENGTH_SHORT).show();

                Intent proceeed = new Intent(StudentTestPage.this,TestResults.class);
                proceeed.putExtra("refID",refID);
                proceeed.putExtra("DateStarted",dateTestStarted);
                proceeed.putExtra("DateFinished",dateTestFinished);
                proceeed.putExtra("finalScore",String.valueOf(score));
                startActivity(proceeed);

            }
        });
    }
    private void setThingsAfterStart()
    {
        start.setVisibility(View.GONE);
        finish.setVisibility(View.VISIBLE);
        pass.setVisibility(View.VISIBLE);
        questionTextView.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
    }

    private void setBeforeTheTest()
    {
        //INITIALLY
        finish.setVisibility(View.GONE); //because test hasnot started
        pass.setVisibility(View.GONE);
        questionTextView.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        option1.setVisibility(View.GONE);
        option2.setVisibility(View.GONE);
        option3.setVisibility(View.GONE);
        option4.setVisibility(View.GONE);
        nextPageButton.setVisibility(View.GONE);
        prevPageButton.setVisibility(View.GONE);
        submitMC.setVisibility(View.GONE);
        answersEditText.setVisibility(View.GONE);
        submitEdit.setVisibility(View.GONE);
        editTextMC.setVisibility(View.GONE);

    }
    private class DonwloadThread extends AsyncTask<Void,Void, String>
    {
        @Override
        protected String doInBackground(Void... urls)
        {
            try {
                URL url = new URL(urlString);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                DownloadUtils.addCertificate(StudentTestPage.this,connection);
                try
                {
                    try
                    {
                        if (connection.getResponseCode()!=HttpsURLConnection.HTTP_OK)
                        {
                            Log.d("121", "doInBackground: Cannot connect");
                        }
                        else
                        {
                            Log.d("121","Getting Question");
                            InputStream inputStream = connection.getInputStream();
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                            bytesRead = inputStream.read(buffer);
                            while(bytesRead >  0)
                            {
                                byteArrayOutputStream.write(buffer,0,bytesRead);
                                bytesRead = inputStream.read(buffer);

                            }
                            byteArrayOutputStream.close();

                            String JSONString = new String(byteArrayOutputStream.toByteArray());
                            try {
                                JSONObject jBase = new JSONObject(JSONString);
                                JSONArray jOptions = jBase.getJSONArray("options");
                                question = jBase.getString("question");
                                result = String.valueOf(jBase.getInt("result"));
                                TTS = jBase.getInt("timetosolve");
                                nOptions = jOptions.length();
                                Log.d("DOWNLOADER", "doInBackground: "+nOptions);
                                if (!listofOption.isEmpty())
                                {
                                    listofOption.clear();
                                }
                                for (int i=0; i<jOptions.length();i++)
                                {
                                    listofOption.add(String.valueOf(jOptions.getInt(i)));
                                    if (listofOption.size()==10)
                                    {
                                        break;
                                    }
                                }
                            }catch (JSONException ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
                finally
                {
                    connection.disconnect();
                }
                }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return "Success";
        }

    }
}
