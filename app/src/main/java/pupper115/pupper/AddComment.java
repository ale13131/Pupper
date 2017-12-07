package pupper115.pupper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    // Context is used for passing user info to other screens as well
    //  as displaying messages in the app
    Context context;
    // userFN is the string that stores the user first name. It is
    //  set to the user's first name from the dog profile
    String userFN = "";

    // onCreate is called when the screen is created
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Displays the user interface that was created for this page
        setContentView(R.layout.activity_add_comment);

        // Bundle gets the intent
        Bundle extras = getIntent().getExtras();

        // Set the user first name to the one passed
        userFN = extras.getString("userFN");

        // Initialize context
        context = getApplication();
    }

    // This is to display the help icon on the top right
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create the menu, adds the help icon to the bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // This is the listener for the menu, in case we extend it
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Finds the item clicked, if multiple items there
        int id = item.getItemId();

        // If the one clicked is the help button
        if (id == R.id.action_help) {
            // Create the intent for the help page
            Intent intent = new Intent(context, AppInfo.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Put the user's name in so the help page can display it
            intent.putExtra("userName", userFN);

            // Start the help page
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // This function is to verify that the user actually wrote a comment
    public void check(View v)
    {
        // Finds the comment the user wrote
        EditText cBox = findViewById(R.id.comment);

        // Checks if the string is empty, therefor comment
        if(cBox.getText().toString().trim().isEmpty()) {
            // Display an error message and not continue
            CharSequence text = "You didn't add a comment!!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        // Else, there is a comment
        else{
            // Create the comment to return to the dog profile
            String comment = userFN + " says:  " + cBox.getText().toString();
            Intent intent = new Intent();
            // Put the comment in the intent to return
            intent.putExtra("comment", comment);
            setResult(1, intent);
            // Finish the activity
            this.finish();
        }
    }
}
