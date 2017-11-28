package pupper115.pupper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

import java.io.File;

/**
 * Created by Josh on 11/8/2017.
 */

public class CreateDogProfile extends AppCompatActivity {
    private EditText name = null;
    private EditText age = null;
    private EditText bio = null;

    private String dogName = "";
    private String dogAge = "";
    private String dogBio = "";

    private Bitmap dogPicture = null;
    private String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dog_profile);

        EditText focus = (EditText) findViewById(R.id.name);
        focus.requestFocus();
    }

    public void registerDog()
    {
        Intent data = getIntent();
        String username = data.getStringExtra("userName");

        String bucketName = "http://s3.amazonaws.com/pupper-user-info/";
        String keyName = username;

        CheckBox checked = (CheckBox) findViewById(R.id.checkBox);
        Boolean isUpForAdoption = checked.isChecked();

        File dogFile = new File(filePath);

        AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
        try {
            Log.d("Pre:", "Entering");
            s3client.putObject(new PutObjectRequest(bucketName, keyName, dogFile));
            Log.d("Pre:", "Finished");
        } catch (AmazonServiceException e) {
            Log.d("Result:", "CRASHED HERE");
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }

    }

    public void checkResponse(View v)
    {
        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        bio = (EditText) findViewById(R.id.bio);

        dogName = name.getText().toString();
        dogAge = age.getText().toString();
        dogBio = bio.getText().toString();

        if(dogName.isEmpty() || dogAge.isEmpty() || dogBio.isEmpty())
        {
            Context context = getApplicationContext();
            CharSequence text = "A field has been left empty!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();

            dogPicture = BitmapFactory.decodeFile(filePath);

            registerDog();
        }
    }
}
