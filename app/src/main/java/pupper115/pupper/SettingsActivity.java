package pupper115.pupper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pupper115.pupper.dbmapper.repos.UserMapperRepo;
import pupper115.pupper.dbmapper.tables.TblUser;

/**
 * This was created by Aaron
 *
 * This is the user settings page. The user is able to change their email, first name, last name,
 * and password here. They must confirm their current password before changing anything. To exit
 * this page, the user presses their back arrow
 */

public class SettingsActivity extends AppCompatActivity {
    // Instantiate AWS DB Client
    // Create the following objects to communicate with Amazon NoSQL DB.
    final AWSCredentialsProvider credentialsProvider = IdentityManager.getDefaultIdentityManager().getCredentialsProvider();
    AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);
    DynamoDBMapper dynamoDBMapper;
    UserMapperRepo userMapRepo;
    
    // Android Context
    private Context context;
    
    
    private settingsTaskPull mPullTask = null;

    String userName = "";
    String password = "";
    private String userFN = "";
    TblUser user;
    
    // UI references to settings pages :'D
    private CheckBox mAdoptingFlag;
    private EditText mUserEmail;
    private EditText mUserFN;
    private EditText mUserLN;
    private EditText mConfirmPassword;
    private EditText mCurrentPassword;
    private EditText mChangePassword;
    private Button mConfirmButton;

    // Textwatcher to watch for password fields
    // Taken from slackoverflow@petey
    // Used to watch Confirm/Change password fields
    // Watchs the Change Password field. If text is entered, allow user to enter into the change password field
    // Else prevent user from entering into field
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkChangePasswordForEmpty();
        }
    };

    void checkChangePasswordForEmpty(){
        String changepassword = mChangePassword.getText().toString();

        if(changepassword.isEmpty()){
            mConfirmPassword.setEnabled(false);
        }else{
            mConfirmPassword.setEnabled(true);
        }
    }

    
    
    // On creation of activity (when settings tab is active)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = getApplication();
        
        // Grabs data from pevious activity
        // We want to grab the users password/username
        // Pass along user's FN to CRUD calls to DB
        Intent data = getIntent();
        userName = data.getStringExtra("userName");
        password = data.getStringExtra("password");
        userFN = data.getStringExtra("userFN");
        
        // Configure AWS and use UserMapperRepo as adapter for CRUD connections to NoSQL.
        AWSConfiguration awsConfig = null;
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(awsConfig)
                .build();
        userMapRepo = new UserMapperRepo(dynamoDBClient);
        
        // Grab UI fields for editing/displaying
        mUserFN = (EditText) findViewById(R.id.userFN);
        mUserLN = (EditText) findViewById(R.id.userLN);
        mUserEmail = (EditText) findViewById(R.id.userEmail);
        mAdoptingFlag = (CheckBox) findViewById(R.id.adoptingFlag);
        mChangePassword = (EditText) findViewById(R.id.changePassword);
        mConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        mCurrentPassword = (EditText) findViewById(R.id.currentPassword);
        mConfirmButton = (Button) findViewById(R.id.confirmButton);
        
        // Using the Text listener we can watch the user's input in the changepassword field
        mChangePassword.addTextChangedListener(mTextWatcher);
        mConfirmPassword.addTextChangedListener(mTextWatcher);

        // Pull users data from the DB (calls an async thread to be run in the background that pulls the users info
        Log.d("username","" + userName);
        mPullTask = new SettingsActivity.settingsTaskPull();
        try {
            mPullTask.execute((Void) null).get();
        }
        catch(Exception e){
            Log.d("Exception thrown pulling db",e.getMessage());
        }

        // Displays the users current information on creation of the activity
        mUserFN.setText(user.getUserFN());
        mUserLN.setText(user.getUserLN());
        mUserEmail.setText(user.getUserEmail());
        mAdoptingFlag.setChecked(user.getIsAdopting());
        checkChangePasswordForEmpty();
    }
    
    
    // Josh's implementation
    // Info button displays on the header and allows access to
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
            intent.putExtra("userFN", userFN);

            startActivity(intent);
            return true;
        }

        if (id == R.id.action_lost) {
            Intent intent = new Intent(getApplication(), AppInfo.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            intent.putExtra("userName", userName);
            intent.putExtra("userFN", userFN);

            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    // On user clicking Confirm Settings button attempts to save user's information
    public void trySaveUserSettings(View v){
        // Need to convert text fields to strings to be saved on DB
        userFN = mUserFN.getText().toString();
        String userLN = mUserLN.getText().toString();
        String userEmail = mUserEmail.getText().toString();
        Boolean adoptingFlag = mAdoptingFlag.isChecked();
        String changePassword = mChangePassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();
        String currentPassword = mCurrentPassword.getText().toString();

        Boolean pass = true;
        Boolean changedFlag = false;
        View focusView = null;
        
        // Checks user's FN/LN fields to see if empty
        // If empty don't push anything to DB and prompt user.
        if (userFN.isEmpty()){
            mUserFN.setError("First name field is empty");
            focusView = mUserFN;
            pass = false;
        }

        if (userLN.isEmpty()){
            mUserLN.setError("Last name field is empty");
            focusView = mUserFN;
            pass = false;
        }
        
        // Checks user's email to see if valid and non empty
        // Acts as FN/LN fields
        if (TextUtils.isEmpty(userEmail)){
            mUserEmail.setError("Email field is empty");
            focusView = mUserEmail;
            pass = false;
        }

        else if (!isEmailValid(userEmail)){
            mUserEmail.setError("This email is not allowed");
            focusView = mUserEmail;
            pass = false;
        }
        
        // Checks if user inputted anything into changepassword
        // If user has entered text, check if both changepassword and confirmpasswords are VALID and the same.
        if(!TextUtils.isEmpty(changePassword)){
            if(!isPasswordValid(changePassword)){
                mChangePassword.setError("Password must be 8 characters and have at " +
                        "least one of each: 1 character and 1 number");
                focusView = mChangePassword;
                pass = false;
            }else {
                if(TextUtils.isEmpty(confirmPassword)){
                    mConfirmPassword.setError("Please confirm your changed password");
                    focusView = mConfirmPassword;
                    pass = false;
                }else if (changePassword.compareTo(confirmPassword) != 0){
                    mConfirmPassword.setError("Passwords does not match");
                    focusView = mConfirmPassword;
                    pass = false;
                }else{
                    pass = true;
                    changedFlag = true;
                }
            }
        }
        
        // To allow user to save information, user must input their current password
        if(currentPassword.isEmpty()){
            mCurrentPassword.setError("Please input your password to save your data!");
            focusView = mCurrentPassword;
            pass = false;
        }

        else if (!currentPassword.equals(password)){
            mCurrentPassword.setError("Current password is incorrect!");
            focusView = mCurrentPassword;
            pass = false;
        }
        
        // If any of the above failed, then focus view to error and prompt user
        // Else attempt to push the data to the NoSQL DB
        if(pass == false){
                focusView.requestFocus();
        }
        else {
                user.setUserFN(userFN);
                user.setUserLN(userLN);
                user.setUserEmail(userEmail);
                if (changedFlag) {
                    Log.d("password",confirmPassword);
                    user.setUserPassword(confirmPassword);
                }
                user.setIsAdopting(adoptingFlag);
                settingsTaskPush mUserSaveTask = new SettingsActivity.settingsTaskPush(user);
                mUserSaveTask.execute((Void) null);
        }
    }
    
    // Helper functions
    // Checks if email is valid within our language
    // Input: <email>
    // Output: true | false
    private boolean isEmailValid(String email) {
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(email);
        boolean b = m.find();

        if(email.isEmpty())
            return false;
        else if(b)
            return false;

        return true;
    }
    
    // Checks if password is valid.
    // Input: <password>
    // Output: true | false
    private boolean isPasswordValid(String password) {
        Boolean isGood = false;

        if(password.length() > 7)
            if(password.matches("[a-zA-Z ]*\\d+[a-zA-Z ]*\\d*"))
                isGood = true;

        return isGood;
    }
    
    // Asynchronous Task where we try to push data to the database.
    // If completed go back to main swipe activity
    public class settingsTaskPush extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog pDialog;
        private TblUser userS;
        settingsTaskPush(TblUser userT){
            userS = userT;
        }
        
        // onPreExecute pushes a dialog to prevent any action until attempt to push is completed.
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(SettingsActivity.this);
            pDialog.setMessage("Saving your settings...woof");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            userMapRepo.save(userS);
            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            pDialog.dismiss();

                Intent intent = new Intent(context, SwipeThrough.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(2, intent);
                finish();

        }
    }
    
    // Asynchronous Task where we try to pull data from the database.
    // Prevents user from doing any action until attempt to pull is done
    private class settingsTaskPull extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SettingsActivity.this);
            pDialog.setMessage("Loading... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params){

                user = userMapRepo.getUser(userName);
                if (user != null){
                    Log.d("bgTaskPull",user.getUserId() + "\n" + user.getUserFN());
                    return true;
                }else{
                    Log.d("bgTaskPull","Blew up. FIX!!!!");
                    return false;
                }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            pDialog.dismiss();
        }
    }
}
