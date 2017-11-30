package pupper115.pupper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Josh on 11/29/2017.
 */

public class AddComment extends AppCompatActivity
{
    private String comment = "";
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
        comment = cBox.getText().toString();

        if(comment.isEmpty() == false)
        {
            comment = userName + " says:  " + comment;
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
