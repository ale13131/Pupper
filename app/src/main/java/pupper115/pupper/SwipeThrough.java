package pupper115.pupper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import pupper115.pupper.s3bucket.Constants;
import pupper115.pupper.s3bucket.Util;

public class SwipeThrough extends AppCompatActivity {

    private TextView mTextMessage;
    private Context context;

    //Added by Josh for use in the dog displaying
    // The S3 client used for getting the list of objects in the bucket
    private AmazonS3Client s3;
    private SimpleAdapter simpleAdapter;
    private ArrayList<HashMap<String, Object>> transferRecordMaps;
    private TransferUtility transferUtility;
    private String userName = "";
    private String password = "";
    private int counter = 0;
    private String lastPicture = "init";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //Should be the main page
                    break;
                case R.id.navigation_dashboard:
                    //Should be the upload page

                    Intent intent = new Intent(context, CreateDogProfile.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("userName", userName);
                    intent.putExtra("password", password);

                    startActivity(intent);

                    break;
                case R.id.navigation_notifications:
                    //Sould be the settings page
                    Intent intent2 = new Intent(context, SettingsActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent2.putExtra("userName", userName);
                    intent2.putExtra("password", password);

                    startActivity(intent2);

                    break;
            }
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_through);

        Intent data = getIntent();
        userName = data.getStringExtra("userName");
        password = data.getStringExtra("password");

        context = getApplication();
        transferUtility = Util.getTransferUtility(this);
        initData();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    //ADDED by Josh until bottom
    public void getMoreInfo(View v)
    {
        if(counter > 0) {
            ImageView img = (ImageView) findViewById(R.id.doggo1);
            Intent intent = new Intent(context, DogProfile.class);

            intent.putExtra("dogImage", lastPicture);
            intent.putExtra("userName", userName);
            startActivity(intent);
        }
        else
        {
            Context context = getApplicationContext();
            CharSequence text = "This is the placeholder dog!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public void getNextDog(View v) {
        Context context = getApplicationContext();
        CharSequence text = "Loading the good doggo...";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        ImageView img = (ImageView) findViewById(R.id.doggo1);

        int range  = transferRecordMaps.size();
        Object [] array = transferRecordMaps.toArray();

        Random rand = new Random();

        int n = rand.nextInt(range);

        Object nextPicture = array[n];

        String pictureName = nextPicture.toString();
        pictureName = pictureName.substring(5, pictureName.length() - 1);

        while(lastPicture.equals(pictureName))
        {
            if(n == 0)
                ++n;
            else if(n == (1 - range))
                --n;
            else
                ++n;
            nextPicture = array[n];

            pictureName = nextPicture.toString();
            pictureName = pictureName.substring(5, pictureName.length() - 1);
        }
        lastPicture = pictureName;

        Picasso.with(this).load("https://s3.amazonaws.com/pupper-user-info/" + pictureName).noFade()
                .resize(1200, 1800).centerInside().into(img);

        ++counter;
    }

    private void initData() {
        // Gets the default S3 client.
        s3 = Util.getS3Client(SwipeThrough.this);
        transferRecordMaps = new ArrayList<HashMap<String, Object>>();
        TransferListener listener = new DownloadListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the file list.
        new GetFileListTask().execute();
    }

    //To get the dog pictures at the first instance of going to the page
    private class GetFileListTask extends AsyncTask<Void, Void, Void> {
        // The list of objects we find in the S3 bucket
        private List<S3ObjectSummary> s3ObjList;
        // A dialog to let the user know we are retrieving the files
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(SwipeThrough.this,
                    "Refreshing the doggos",
                    "Please Wait");
        }

        @Override
        protected Void doInBackground(Void... inputs) {
            // Queries files in the bucket from S3.
            s3ObjList = s3.listObjects(Constants.BUCKET_NAME).getObjectSummaries();
            transferRecordMaps.clear();
            for (S3ObjectSummary summary : s3ObjList) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("key", summary.getKey());
                transferRecordMaps.add(map);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            //simpleAdapter.notifyDataSetChanged();
        }
    }

    private class DownloadListener implements TransferListener {
        // Simply updates the list when notified.
        @Override
        public void onError(int id, Exception e) {
            Log.e("DownloadListener", "onError: " + id, e);
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d("DownloadListener", String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));
        }

        @Override
        public void onStateChanged(int id, TransferState state) {
            Log.d("DownloadListener", "onStateChanged: " + id + ", " + state);
        }
    }
}
