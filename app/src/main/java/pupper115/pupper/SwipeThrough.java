package pupper115.pupper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
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
    private String pictureFile = "";

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

                    startActivityForResult(intent, 2);

                    break;
                case R.id.navigation_notifications:
                    //Sould be the settings page
                    break;
            }
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_through);

        context = getApplication();
        transferUtility = Util.getTransferUtility(this);
        initData();

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    //ADDED by Josh until bottom
    public void getMoreInfo(View v)
    {
        //Here is where the dog info will appear over the actual picture
    }

    public void getNextDog(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.doggo);

        int range  = transferRecordMaps.size();
        Object [] array = transferRecordMaps.toArray();

        Random rand = new Random();

        int n = rand.nextInt(range);

        Object nextPicture = array[n];
        String pictureName = nextPicture.toString();
        pictureName = pictureName.substring(5, pictureName.length() - 1);

        new DownloadFileFromURL().execute("https://s3.amazonaws.com/pupper-user-info/" + pictureName);
        img.setImageBitmap(BitmapFactory.decodeFile(pictureFile));
        Log.d("Location", pictureFile);

        //observer.setTransferListener(new DownloadListener());
    }

    private ProgressDialog pDialog;

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");

            pDialog = new ProgressDialog(SwipeThrough.this);
            pDialog.setMessage("Loading... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                String root = Environment.getExternalStorageDirectory().toString();

                System.out.println("Downloading");
                URL url = new URL(f_url[0]);

                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file

                OutputStream output = new FileOutputStream(root + "/downloadedfile.jpg");
                pictureFile = root + "/downloadedfile.jpg";

                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    // writing data to file
                    output.write(data, 0, count);

                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }



        /**
         * After completing background task
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            System.out.println("Downloaded");

            pDialog.dismiss();
        }

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
