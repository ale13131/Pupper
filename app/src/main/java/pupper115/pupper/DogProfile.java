package pupper115.pupper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import com.squareup.picasso.Picasso;

import java.util.StringTokenizer;

import pupper115.pupper.dbmapper.repos.DogMapperRepo;
import pupper115.pupper.dbmapper.tables.TblDog;

/**
 * Created by Joseph
 * The original layout and info displayed was created by Joseph. Josh then took the base and added
 * the info pulled from the server and the like and comment functionality. This page displays more
 * information on the dog, number of likes, any comments as well as being able to do the two latter.
 * If you click on like, it will like the dog, increment the number of likes and save you as a liker
 * of the dog so you don't click it a ton to up the likes. When you click add comment, it takes you
 * the add comment page and you then add the comment which is displayed on the page when finished.
 * To exit this page, the user simply press on their back arrow
 */

public class DogProfile extends AppCompatActivity {
    private Context context;
    DynamoDBMapper dynamoDBMapper;
    DogMapperRepo dogMapRepo;
    TblDog dog;
    private dogTaskPull mPullTask = null;
    String dogImage = "";
    String bio = "";
    String userName = "";
    private DogRegisterTask mAuthTask = null;

    final AWSCredentialsProvider credentialsProvider = IdentityManager.getDefaultIdentityManager().getCredentialsProvider();
    AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_profile);
        Bundle extras = getIntent().getExtras();
        dogImage = extras.getString("dogImage");
        userName = extras.getString("userName");

        context = getApplication();
        AWSConfiguration awsConfig = null;
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(awsConfig)
                .build();
        dogMapRepo = new DogMapperRepo(dynamoDBClient);

        mPullTask = new DogProfile.dogTaskPull();
        try {
            mPullTask.execute((Void) null).get();
        }
        catch(Exception e){
            Log.d("Exception pulling db",e.getMessage());
        }

        setImage(dogImage);
        setData(dogImage);
    }

    private void setImage( String imageName){
        ImageView img = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this).load("https://s3.amazonaws.com/pupper-user-info/" + imageName).noFade()
                .resize(1200, 1800).centerInside().into(img);
    }

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("url")
    @Expose
    private String imageUrl;

    @SerializedName("age")
    @Expose
    private Integer age;

    @SerializedName("location")
    @Expose
    private String location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    //Added by Josh 11/20
    private void setData(String image)
    {
        Log.d("Image", image);
        bio = "The current owner of ";
        bio = bio + dog.getDogName() + " is " + dog.getOwnerId() + ". ";
        bio = bio + dog.getDogName() + " is currently ";
        //Pull from dog table if the dog is up for adoption
        if(dog.getIsOwned() == false)
            bio = bio + "up for adoption! Contact " + dog.getOwnerId() + " for details";
        else
            bio = bio + "not up to be adopted. Sorry";

        bio = bio + ". Here is a quick bio of " + dog.getDogName() + " from " + dog.getOwnerId() + ": \r\n";
        //Pull bio about dog and add it to the string
        bio = bio + dog.getDogBio();

        String comments = dog.getComments().replaceAll("null", "");
        if(comments.contains("null"))
        {
            comments.replaceAll("\0", "");
            Log.d("HERE", comments);
        }
        if(comments.contains("null") == false)
        {
            bio = bio + "\n ------------Comments------------";
            bio = bio + comments;
        }

        TextView name = (TextView) findViewById(R.id.textViewDogName);
        TextView info = (TextView) findViewById(R.id.textViewDogInfo);

        Button likes = (Button) findViewById(R.id.like);
        Double num = dog.getLikes();
        String likedBy = dog.getLikedBy();
        likes.setText("Likes: " + num.intValue());
        if(likedBy.contains(userName))
            likes.setClickable(false);

        name.setText(dog.getDogName());
        info.setText(bio);
    }

    public void likeDog(View v)
    {
        Button likes = (Button) findViewById(R.id.like);
        Double num = dog.getLikes();
        dog.likeDog();
        ++num;
        likes.setText("Likes: " + num.intValue());
        likes.setClickable(false);
        dog.setLikedBy(userName);
        mAuthTask = new DogRegisterTask(true, dog);
        mAuthTask.execute((Void) null);
    }

    public void addComment(View v)
    {
        //Create an activity to write a comment
        Intent intent = new Intent(context, AddComment.class);
        intent.putExtra("userName", userName);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(data != null) {
            switch (requestCode) {
                case 1:
                    String comment = data.getStringExtra("comment");
                    if (comment != null) {
                        dog.setComments("\n" + comment);
                        bio = bio + " \n" + comment;
                    }
                    mAuthTask = new DogRegisterTask(true, dog);
                    mAuthTask.execute((Void) null);

                    TextView info = (TextView) findViewById(R.id.textViewDogInfo);
                    info.setText(bio);
                    break;
            }
        }
    }

    private class dogTaskPull extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DogProfile.this);
            pDialog.setMessage("Loading... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params){
            StringTokenizer tokens = new StringTokenizer(dogImage, ".");
            String first = tokens.nextToken(); //Dog owner ID
            Log.d("ID:", dogImage + "     " + first);

            dog = dogMapRepo.getDog(dogImage, first);
            if (dog != null){
                Log.d("Results", "WORKED!!!!");
                return true;
            }else{
                Log.d("Results", dogImage);
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            pDialog.dismiss();
        }
    }

    public class DogRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private Boolean isRight = false;
        private TblDog dog = null;

        DogRegisterTask(Boolean isAllowed, TblDog t) {
            isRight = isAllowed;
            dog = t;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            if(isRight) {
                dynamoDBMapper.save(dog);
                return true;
            }
            else
                return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

}
