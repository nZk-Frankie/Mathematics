package au.edu.curtin.assignment.mathematics;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class PixbayDownloaderForUpdate extends AppCompatActivity {

    private EditText search;
    private Button load,finish,back;
    private byte [] buffer = new byte[1024];
    private Bitmap images = null;

    private String URLString;
    private List<String> listofPICURL = new ArrayList<>();
    private List<Bitmap> pictureList = new ArrayList<>();

    private GridView imageGridViewer;

    @Override
    protected void onSaveInstanceState(Bundle x)
    {
        super.onSaveInstanceState(x);
    }

    @Override
    protected void onRestoreInstanceState(Bundle x)
    {
        super.onRestoreInstanceState(x);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pixbay_layout);
        search = (EditText) findViewById(R.id.inputSearch);
        load = (Button) findViewById(R.id.loadImage);
        back = (Button)findViewById(R.id.backButtonfromPixbay);
        imageGridViewer = (GridView)findViewById(R.id.imageGridView);


        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URLString = Uri.parse("https://pixabay.com/api/").buildUpon()
                        .appendQueryParameter("key","23742722-8569659c0f963cb56b77133a8")
                        .appendQueryParameter("q",getText(search))
                        .appendQueryParameter("per_page","30").build().toString();
                new ImageDownloader().execute();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goback = new Intent(PixbayDownloaderForUpdate.this,StudentViewMore.class);
                setResult(Activity.RESULT_CANCELED,goback);
                finish();
            }
        });
    }
    private void saveBitmap(Bitmap image,String photoName)
    {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File pixbayDirectory = contextWrapper.getDir("pixbayImages", Context.MODE_PRIVATE);

        File pixbayPath = new File(pixbayDirectory,photoName);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(pixbayPath);

            image.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
        }catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }finally {
            //close the file output strema
            try {
                fileOutputStream.close();
            }catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

    }

    private boolean checkImageExist(ImageView image)
    {
        Drawable draw = image.getDrawable();
        boolean imageExist = (draw!=null);
        if (imageExist && (draw instanceof BitmapDrawable))
        {
            imageExist = ((BitmapDrawable) draw).getBitmap() != null;

        }

        return imageExist;
    }

    private String getText(EditText editText)
    {
        String res = "";
        if (editText.getText().toString().equals(null))
        {
            res = "";
        }
        else
        {
            res = editText.getText().toString();
        }
        return res;
    }

    private class ImageDownloader extends AsyncTask<Void, Void, Void>
    {

        /**
         * @param voids
         * @deprecated
         */
        @Override
        protected Void doInBackground(Void... voids) {

            if (!listofPICURL.isEmpty() && !pictureList.isEmpty())
            {
                listofPICURL.clear();
                pictureList.clear();
            }
            try {
                URL url = new URL(URLString);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                try
                {
                    try
                    {
                        if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK)
                        {
                            throw new IOException();
                        }
                        else
                        {
                            String data = searchRemoteAPI(connection);
                            if(data != null)
                            {
                                getImageLargeURL(data);
                                if (!listofPICURL.isEmpty())
                                {
                                    getImageFromURL(listofPICURL);
                                    if (!pictureList.isEmpty())
                                    {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Loading: "+pictureList.size()+" images", Toast.LENGTH_SHORT).show();
                                                ImageAdapter adapter = new ImageAdapter(pictureList);
                                                imageGridViewer.setAdapter(adapter);

                                                imageGridViewer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                        saveBitmap(pictureList.get(i),"pixbay.png");

                                                        Intent goback = new Intent(PixbayDownloaderForUpdate.this,StudentViewMore.class);
                                                        setResult(Activity.RESULT_OK,goback);
                                                        finish();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }finally {
                    connection.disconnect();
                }
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return null;
        }
        private byte[] getByteArrayFromInputStream(InputStream inputStream) throws IOException {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[4096];
            int progress = 0;
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
                progress = progress+nRead;
            }

            return buffer.toByteArray();
        }

        private void getImageFromURL(List<String> pictureURL)
        {
            for (int i=0; i<pictureURL.size();i++) {
                String url = pictureURL.get(i);
                Bitmap image = null;
                String imageURLString = Uri.parse(url).buildUpon().toString();
                try {
                    URL imageURL = new URL(imageURLString);
                    HttpsURLConnection connection = (HttpsURLConnection) imageURL.openConnection();
                    try {
                        if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {

                        } else {
                            try {
                                InputStream inputStream = connection.getInputStream();
                                byte[] Bytedata = getByteArrayFromInputStream(inputStream);
                                image = BitmapFactory.decodeByteArray(Bytedata, 0, Bytedata.length);
                                pictureList.add(image);
                            } catch (IOException x) {
                                x.printStackTrace();
                            }
                        }
                    } finally {
                        connection.disconnect();
                    }
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (IOException p) {
                    p.printStackTrace();
                }
            }
        }

        private String searchRemoteAPI(HttpsURLConnection con)
        {
            String data = null;
            try
            {
                InputStream inputStream = con.getInputStream();
                byte [] byteDat = IOUtils.toByteArray(inputStream);
                data = new String(byteDat, StandardCharsets.UTF_8);
            }
            catch (IOException e){
                e.printStackTrace();
            }

            return data;
        }

        private void getImageLargeURL(String data)
        {
            String imageURL = null;
            try
            {
                JSONObject jBase = new JSONObject(data);
                JSONArray jHits = jBase.getJSONArray("hits");
                for (int i=0; i<jHits.length();i++)
                {
                    JSONObject jHitsItem = jHits.getJSONObject(i);
                    listofPICURL.add(jHitsItem.getString("largeImageURL"));
                }
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    private class ImageAdapter extends BaseAdapter {

        List<Bitmap> imageList;
        public ImageAdapter(List<Bitmap> image)
        {
            this.imageList = image;
        }


        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public Object getItem(int i) {
            return imageList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null)
            {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pixbay_each,viewGroup,false);
            }

            ImageView imageView = (ImageView) view.findViewById(R.id.imageEachLayout);
            imageView.setImageBitmap(this.imageList.get(i));

            return view;
        }
    }
}
