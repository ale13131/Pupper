package pupper115.pupper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;


import pupper115.pupper.dbmapper.repos.UserMapperRepo;
import pupper115.pupper.dbmapper.tables.TblUser;

/**
 * Created by Josh on 10/26/2017.
 * This page is where the user enters their information and attempts to register. If there is an
 * empty field, the user cannot register until they fill it out. To exit this page, the user simply
 * presses their back arrow and they are returned to the login page
 */

public class NewUser extends AppCompatActivity {
    UserMapperRepo userMapRepo;
    DynamoDBMapper dynamoDBMapper;
    final AWSCredentialsProvider credentialsProvider = IdentityManager.getDefaultIdentityManager().getCredentialsProvider();
    AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);
    private UserRegisterTask mAuthTask = null;

    private Context context;
    private String userName = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        context = getApplication();

        AWSConfiguration awsConfig = null;
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(awsConfig)
                .build();
        userMapRepo = new UserMapperRepo(dynamoDBClient);

        EditText focus = findViewById(R.id.editTextFirstName);
        focus.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_help) {
            Intent intent = new Intent(getApplication(), AppInfo.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            intent.putExtra("userName", userName);

            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void registerUser(View v) {
        Intent data = getIntent();
        if (data != null)
            userName = data.getStringExtra("userName");
        password = data.getStringExtra("password");

        EditText first = findViewById(R.id.editTextFirstName);
        EditText last = findViewById(R.id.editTextLastName);
        EditText eMail = findViewById(R.id.editTextEmail);

        String fName = first.getText().toString();
        String lName = last.getText().toString();
        String email = eMail.getText().toString();

        CheckBox checked = findViewById(R.id.checkBox);
        Boolean isAdopting = checked.isChecked();

        if (fName.isEmpty() || lName.isEmpty() || email.isEmpty()) {
            Context context = getApplicationContext();
            CharSequence text = "There's an empty field!!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (!email.contains("@")) {
            Context context = getApplicationContext();
            CharSequence text = "Invalid eMail entered!!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            Log.d("Result", "User: " + userName + " password: " +
                    password + " first: " + fName + " last: " + lName + " email: " + email + " adopting: " + isAdopting);

            TblUser newUser = new TblUser();
            newUser.setUserId(userName);
            newUser.setUserPassword(password);
            newUser.setUserFN(fName);
            newUser.setUserLN(lName);
            newUser.setUserEmail(email);
            newUser.setIsAdopting(isAdopting);

            mAuthTask = new NewUser.UserRegisterTask(true, newUser);
            mAuthTask.execute((Void) null);
        }
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private Boolean isRight = false;
        private TblUser user = null;

        UserRegisterTask(Boolean isAllowed, TblUser t) {
            isRight = isAllowed;
            user = t;
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
                dynamoDBMapper.save(user);
                return true;
            }
            else
                return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                //Login was successful, go to main screen after the info page is displayed
                Intent resultIntent = new Intent();

                Intent displayInfo = new Intent(context, AppInfo.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                displayInfo.putExtra("userName", userName);
                displayInfo.putExtra("password", password);
                startActivity(displayInfo);

                setResult(2, resultIntent);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}