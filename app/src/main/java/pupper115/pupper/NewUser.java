package pupper115.pupper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Josh on 10/26/2017.
 */

public class NewUser extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        EditText focus = (EditText) findViewById(R.id.first);
        focus.requestFocus();
    }

    public void registerUser(View v) {
        Intent data = getIntent();
        String eMail = "";
        String password = "";
        if (data != null)
            eMail = data.getStringExtra("email");
        password = data.getStringExtra("password");

        EditText first = (EditText) findViewById(R.id.first);
        EditText last = (EditText) findViewById(R.id.last);
        EditText username = (EditText) findViewById(R.id.username);

        String fName = first.getText().toString();
        String lName = last.getText().toString();
        String uName = username.getText().toString();

        if (fName.isEmpty() || lName.isEmpty() || uName.isEmpty()) {
            Context context = getApplicationContext();
            CharSequence text = "There's an empty field!!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else {
            Log.d("Result", "Email: " + eMail + " password: " + password + " first: " + fName + " last: " + lName + " user: " + uName);

            //
        }
    }
}
