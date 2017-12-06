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
    // Amazon DB Client Object
    final AWSCredentialsProvider credentialsProvider = IdentityManager.getDefaultIdentityManager().getCredentialsProvider();
    AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);
    private Context context;
    DynamoDBMapper dynamoDBMapper;
    UserMapperRepo userMapRepo;
    TblUser user;
    private settingsTaskPull mPullTask = null;



    String userName = "";
    String password = "";
    private String userFN = "";

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = getApplication();

        Intent data = getIntent();
        userName = data.getStringExtra("userName");
        password = data.getStringExtra("password");
        userFN = data.getStringExtra("userFN");

        AWSConfiguration awsConfig = null;
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(awsConfig)
                .build();
        userMapRepo = new UserMapperRepo(dynamoDBClient);

        mUserFN = (EditText) findViewById(R.id.userFN);
        mUserLN = (EditText) findViewById(R.id.userLN);
        mUserEmail = (EditText) findViewById(R.id.userEmail);
        mAdoptingFlag = (CheckBox) findViewById(R.id.adoptingFlag);
        mChangePassword = (EditText) findViewById(R.id.changePassword);
        mConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        mCurrentPassword = (EditText) findViewById(R.id.currentPassword);
        mConfirmButton = (Button) findViewById(R.id.confirmButton);

        mChangePassword.addTextChangedListener(mTextWatcher);
        mConfirmPassword.addTextChangedListener(mTextWatcher);

        // Pull data from DB
        Log.d("username","" + userName);
        mPullTask = new SettingsActivity.settingsTaskPull();
        try {
            mPullTask.execute((Void) null).get();
        }
        catch(Exception e){
            Log.d("Exception thrown pulling db",e.getMessage());
        }

        // Set default values for user
        mUserFN.setText(user.getUserFN());
        mUserLN.setText(user.getUserLN());
        mUserEmail.setText(user.getUserEmail());
        mAdoptingFlag.setChecked(user.getIsAdopting());

        checkChangePasswordForEmpty();
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
            intent.putExtra("userFN", userFN);

            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void trySaveUserSettings(View v){
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

        //TODO:Fix password updating
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

    private boolean isPasswordValid(String password) {
        Boolean isGood = false;

        if(password.length() > 7)
            if(password.matches("[a-zA-Z ]*\\d+[a-zA-Z ]*\\d*"))
                isGood = true;

        return isGood;
    }
    // TODO: two async threads. 1 thread for pulling (do in oncreate.) once all data is pulled kill thread.
    // TODO: once user clicks confirm do push thread things

    public class settingsTaskPush extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog pDialog;
        private TblUser userS;
        settingsTaskPush(TblUser userT){
            userS = userT;
        }

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
                    Log.d("bgTask",user.getUserId() + "\n" + user.getUserFN());
                    return true;
                }else{
                    Log.d("doinBackground user load","didnt work fucking bitch");
                    return false;
                }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            pDialog.dismiss();
        }
    }
}
