package au.edu.curtin.assignment.mathematics;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.edu.curtin.assignment.mathematics.R;
import au.edu.curtin.assignment.mathematics.model.Databases;
import au.edu.curtin.assignment.mathematics.model.Student;

public class StudentViewMore extends AppCompatActivity {
    private Button back, changePicture, addEmailButton, addPhoneButton, saveInformationButton;
    private EditText firstName, lastName, email, phone;
    private RecyclerView emailRV, phoneRV;
    private ImageView viewMoreStudentImage;
    private List<String> emailList = new ArrayList<>();
    private List<String> phoneList = new ArrayList<>();
    private String strFirstName, strLastName, strID;
    private Bitmap studentImageBitmap;
    private Student student;

    private static final int REQUEST_PHOTO_FROM_CAMERA = 1;
    private static final int REQUEST_PHOTO_FROM_FILE = 2;
    private static final int REQUEST_PHOTO_FROM_PIXBAY = 3;

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("strID",strID);
        savedInstanceState.putStringArrayList("emailList", (ArrayList<String>) emailList);
        savedInstanceState.putStringArrayList("phoneList", (ArrayList<String>) phoneList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        strID = savedInstanceState.getString("strID");
        emailList = (List<String>) savedInstanceState.getStringArrayList("emailList");
        phoneList = (List<String>)savedInstanceState.getStringArrayList("phoneList");

        EMAILItemHolderAdapter itemHolder = new EMAILItemHolderAdapter(emailList);
        emailRV.setLayoutManager(new LinearLayoutManager(StudentViewMore.this));
        emailRV.setAdapter(itemHolder);

        PHONEADAPTER phoneadapter = new PHONEADAPTER(phoneList);
        phoneRV.setLayoutManager(new LinearLayoutManager(StudentViewMore.this));
        phoneRV.setAdapter(phoneadapter);


    }
    private void getStudentBitmap()
    {
        String imagePath = "data/data/au.edu.curtin.assignment.mathematics/app_studentImageDirectory/";
        try {
            File file = new File(imagePath,strID+".jpg");
            Bitmap image = BitmapFactory.decodeStream(new FileInputStream(file));
            studentImageBitmap = image;
            viewMoreStudentImage.setImageBitmap(image);
        }catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
    }
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
        phone = (EditText) findViewById(R.id.editTextViewMoreStudentPhone);

        emailRV = (RecyclerView) findViewById(R.id.RVViewMoreEmail);
        phoneRV = (RecyclerView) findViewById(R.id.RVPhoneViewMore);
        viewMoreStudentImage = (ImageView) findViewById(R.id.imageViewStudentViewMore);

        //get the data
        strID = getIntent().getExtras().getString("studentID");

        student = Databases.getInstance().findStudentWithID(strID);

        emailList = student.getEmailList();
        phoneList = student.getPhoneNumberList();

        EMAILItemHolderAdapter itemHolder = new EMAILItemHolderAdapter(emailList);
        emailRV.setLayoutManager(new LinearLayoutManager(StudentViewMore.this));
        emailRV.setAdapter(itemHolder);

        PHONEADAPTER phoneadapter = new PHONEADAPTER(phoneList);
        phoneRV.setLayoutManager(new LinearLayoutManager(StudentViewMore.this));
        phoneRV.setAdapter(phoneadapter);


        firstName.setText(student.getFirstName());
        lastName.setText(student.getLastName());
        getStudentBitmap();

        if (studentImageBitmap == null)
        {
            Log.d("12863821", "onCreate:NULLLLLLLLLLLLLLLLLL");
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goback = new Intent(StudentViewMore.this, ViewStudent.class);
                startActivity(goback);
            }
        });

        saveInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strFirstName = getStringfromEditText(firstName);
                strLastName = getStringfromEditText(lastName);
                if (!strFirstName.equals("") || !strLastName.equals(""))
                {
                    Student editedStudent = new Student(strID,strFirstName,strLastName);
                    editedStudent.setEmailList(emailList);
                    editedStudent.setPhoneNumberList(phoneList);
                    editedStudent.setStudentImage(studentImageBitmap);

                    Databases.getInstance().editStudent(editedStudent,getApplicationContext());
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Invalid First/Last Name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        changePicture.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                PopupMenu listMenu = new PopupMenu(StudentViewMore.this,changePicture);
                listMenu.getMenuInflater().inflate(R.layout.menu_drop,listMenu.getMenu());
                listMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getTitle().equals("Take A Photo"))
                        {
                            Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            try {
                                startActivityForResult(openCamera,REQUEST_PHOTO_FROM_CAMERA);
                            }
                            catch (ActivityNotFoundException ex)
                            {

                            }
                        }
                        else if (menuItem.getTitle().equals("Import from file"))
                        {
                            Intent getFromFile = new Intent(Intent.ACTION_GET_CONTENT);
                            getFromFile.setType("image/*");
                            startActivityForResult(getFromFile,REQUEST_PHOTO_FROM_FILE);

                        }
                        else if (menuItem.getTitle().equals("Import from PixBay"))
                        {
                            Intent intent = new Intent(StudentViewMore.this,PixbayDownloaderForUpdate.class);
                            startActivityForResult(intent,REQUEST_PHOTO_FROM_PIXBAY);
                        }
                        return true;
                    }
                });
                listMenu.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_PHOTO_FROM_CAMERA && resultCode == RESULT_OK)
        {
            studentImageBitmap = (Bitmap) data.getExtras().get("data");
            viewMoreStudentImage.setImageBitmap(studentImageBitmap);
        }
        else if (requestCode == REQUEST_PHOTO_FROM_FILE && resultCode == RESULT_OK)
        {
            if (data!= null)
            {
                Uri uriImage = data.getData();
                try {
                    studentImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uriImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                viewMoreStudentImage.setImageURI(uriImage);
            }
        }
        else if (requestCode == REQUEST_PHOTO_FROM_PIXBAY && resultCode == RESULT_OK)
        {
            if (data!=null)
            {
                String imagePath = "data/data/au.edu.curtin.assignment.mathematics/app_pixbayImages/";
                try {
                    File file = new File(imagePath,"pixbay.png");
                    Bitmap image = BitmapFactory.decodeStream(new FileInputStream(file));
                    studentImageBitmap = image;
                    viewMoreStudentImage.setImageBitmap(image);
                }catch (FileNotFoundException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }

    private boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    private String[] SplitName(String DISPLAYNAME) {
        return DISPLAYNAME.split("\\s+");
    }

    private String getStringfromEditText(EditText text) {
        String res = "";
        if (!text.getText().toString().equals(null) || !text.getText().toString().equals("")) ;
        {
            res = text.getText().toString();
        }

        return res;
    }

    private class EMAILItemHolderAdapter extends RecyclerView.Adapter<EMAILItemHolder> {
        private List<String> itemList = new ArrayList<>();

        public EMAILItemHolderAdapter(List<String> data) {
            this.itemList = data;
        }

        @Override
        public EMAILItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
            View view = layoutInflater.inflate(R.layout.each_list_create, container, false);
            EMAILItemHolder itemHolder = new EMAILItemHolder(view);

            return itemHolder;
        }

        @Override
        public void onBindViewHolder(EMAILItemHolder itemHolder, @SuppressLint("RecyclerView") int position) {
            itemHolder.bind(this.itemList.get(position));
            itemHolder.removeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemList.remove(position);
                    notifyItemChanged(position);
                    notifyItemRangeChanged(position, itemList.size());
                    notifyDataSetChanged();
                }
            });

            addEmailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String emailSTR =  getStringfromEditText(email);
                    if (isValidEmail(emailSTR))
                    {
                        if (!itemList.contains(emailSTR))
                        {
                            itemList.add(emailSTR);
                            emailList=itemList;
                            notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return this.itemList.size();
        }
    }

    private class EMAILItemHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        Button removeItem;

        public EMAILItemHolder(View view) {
            super(view);

            itemName = (TextView) view.findViewById(R.id.ItemNameEachLayout);
            removeItem = (Button) view.findViewById(R.id.removeButtonxxx);

        }

        public void bind(String item) {
            itemName.setText(item);
        }
    }

    private class PHONEViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        Button removeItem;

        public PHONEViewHolder(View view) {
            super(view);

            itemName = (TextView) view.findViewById(R.id.ItemNameEachLayout);
            removeItem = (Button) view.findViewById(R.id.removeButtonxxx);

        }

        public void bind(String item) {
            itemName.setText(item);
        }
    }

    private class PHONEADAPTER extends RecyclerView.Adapter<PHONEViewHolder> {
        private List<String> data;

        public PHONEADAPTER(List<String> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public PHONEViewHolder onCreateViewHolder(@NonNull ViewGroup container, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
            View view = layoutInflater.inflate(R.layout.each_list_create, container, false);
            PHONEViewHolder itemHolder = new PHONEViewHolder(view);

            return itemHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PHONEViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.bind(this.data.get(position));
            holder.removeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    data.remove(position);
                    notifyItemChanged(position);
                    notifyItemRangeChanged(0, data.size());
                    notifyDataSetChanged();
                }
            });


            addPhoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phoneSTR = getStringfromEditText(phone);
                    if (phoneSTR.length()==10)
                    {
                        if (!data.contains(phoneSTR))
                        {
                            data.add(phoneSTR);
                            phoneList = data;
                            notifyDataSetChanged();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

    }
}
