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
    Context context;
    String userFN = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        Bundle extras = getIntent().getExtras();
        userFN = extras.getString("userFN");

        context = getApplication();
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
            Intent intent = new Intent(context, AppInfo.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            intent.putExtra("userName", userFN);

            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void check(View v)
    {
        EditText cBox = (EditText) findViewById(R.id.comment);
        Log.d("HERE", cBox.getText().toString());

        if(cBox.getText().toString().trim().isEmpty()) {
            CharSequence text = "You didn't add a comment!!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }else{
            String comment = userFN + " says:  " + cBox.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("comment", comment);
            setResult(1, intent);
            this.finish();
        }
    }
}
