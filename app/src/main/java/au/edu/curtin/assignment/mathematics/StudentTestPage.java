package au.edu.curtin.assignment.mathematics;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import au.edu.curtin.assignment.mathematics.Util.DownloadUtils;


public class StudentTestPage extends AppCompatActivity {

    private String urlString = Uri.parse("https://140.238.205.172:8000/random/question").buildUpon().build().toString();
    private Button pass, finish,start;
    private ViewPager2 studentAnswerViewPager;
    private TextView questionTextView, scoreTextView, title;
    boolean testStarted =false, timerfinished = true;

    private CountDownTimer timer;
    private byte [] buffer = new byte[1024];
    private int bytesRead;
    private String question, result;
    private int TTS, nOptions;
    private List<String> listofOption = new ArrayList<>();


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
            Log.d("888", "onClick: "+TTS);

            timer = new CountDownTimer(10*1000,1000)
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_test_page);



        pass = (Button) findViewById(R.id.passQuestionButton);
        start = (Button)findViewById(R.id.startButtonStudentTest);
        finish = (Button) findViewById(R.id.finishButtonStudentTest);


        studentAnswerViewPager = (ViewPager2)findViewById(R.id.studentAnswerSectionVP2);
        questionTextView = (TextView) findViewById(R.id.QuestionTextView);
        scoreTextView = (TextView)findViewById(R.id.studentScoreTextView);
        title = (TextView)findViewById(R.id.textView12121);

        scoreTextView.setText("0");

        FragmentManager fm = getSupportFragmentManager();

        //INITIALLY
        finish.setVisibility(View.GONE); //because test hasnot started
        pass.setVisibility(View.GONE);
        questionTextView.setVisibility(View.GONE);
        title.setVisibility(View.GONE);




        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start.setVisibility(View.GONE);
                testStarted = true;

                //then
                finish.setVisibility(View.VISIBLE);
                pass.setVisibility(View.VISIBLE);
                questionTextView.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);

                showQuestion();




                Toast.makeText(getApplicationContext(), "Starting Test", Toast.LENGTH_SHORT).show();

            }
        });
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testStarted =false;
                finish.setVisibility(View.GONE);
                pass.setVisibility(View.GONE);
                questionTextView.setVisibility(View.GONE);
                start.setVisibility(View.VISIBLE);
                title.setVisibility(View.GONE);

                timer.cancel();

                Toast.makeText(getApplicationContext(), "Test Finished", Toast.LENGTH_SHORT).show();

            }
        });
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
                                for (int i=0; i<jOptions.length();i++)
                                {
                                    listofOption.add(String.valueOf(jOptions.getInt(i)));
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


    private class AnswerSectionFragment extends Fragment{

        List<String> options;
        public AnswerSectionFragment()
        {

        }
        public AnswerSectionFragment(List<String> options)
        {
            this.options = options;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle x)
        {
            return (ViewGroup)inflater.inflate(R.layout.answer_fragment_layout,ui,false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
        {

        }
    }

    private class AnswerSectionFragmentAdapter extends FragmentStateAdapter{
        private List<String> answerChoice = new ArrayList<>();
        private List<List<String>> listofListofQuestions = new ArrayList<>();


        public AnswerSectionFragmentAdapter(@NonNull FragmentActivity fragmentActivity, List<String> fullAnswerChoices)
        {
            super(fragmentActivity);
            this.answerChoice = fullAnswerChoices;
            generate();
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return new AnswerSectionFragment(listofListofQuestions.get(position));
        }

        private void generate()
        {
            int i = 0;
            List<String> temp = new ArrayList<>();
            while (i!= answerChoice.size())
            {
                temp.add(answerChoice.get(i));
                if (i%4==0)
                {
                    listofListofQuestions.add(temp);
                    temp.clear();
                }
                i++;
            }
        }

        @Override
        public int getItemCount() {
            return listofListofQuestions.size();
        }
    }
}
