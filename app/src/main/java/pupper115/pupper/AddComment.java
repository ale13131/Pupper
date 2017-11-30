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
 * Created by Josh on 11/29/2017.
 * This activity is for adding a comment to the dog profile and then returning to the dog profile
 * To leave, the user just needs to press the back arrow. If the user tries to enter a blank comment,
 * the app will display an error message and stay on the page
 */

public class AddComment extends AppCompatActivity
{
    Context context;
    String userName = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        Bundle extras = getIntent().getExtras();
        userName = extras.getString("userName");

        context = getApplication();
    }

    public void check(View v)
    {
        EditText cBox = (EditText) findViewById(R.id.comment);
        Log.d("HERE", cBox.getText().toString());

        if(cBox.getText().toString().isEmpty() == false)
        {
            String comment = userName + " says:  " + cBox.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("comment", comment);
            setResult(1, intent);
            this.finish();
        }
        else
        {
            CharSequence text = "You didn't add a comment!!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}
