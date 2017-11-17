package pupper115.pupper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

import pupper115.pupper.dbmapper.repos.UserMapperRepo;

public class SettingsActivity extends AppCompatActivity {
    // Amazon DB Client Object
    DynamoDBMapper dynamoDBMapper;
    UserMapperRepo userMapRepo;
    String userId = "";

    // UI references to settings pages :'D
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
