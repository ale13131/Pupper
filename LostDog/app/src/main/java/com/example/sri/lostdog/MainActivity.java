import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import android.R;

public class DogLost extends AppCompatActivity {

    Button show;
    Dialog myDialog;
    Button open;
    Button close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_lost);

        show = (Button)findViewById(R.id.show);
        show.setOnClickListener(new View.onClickListener()){
            @Override
            public void onClick(View,view){
                myCustomAlertDialog();
            }
        });

    }
    public void myCustomAlertDialog(){
        myDialog = new Dialog(DogLost.this);
        myDialog.addContentView(R.layout.customdialogue);
        myDialog.requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        myDialog.setTitle("LostDog");

        open = (Button)myDialog.findViewById(R.id.open);
        close = (Button)myDialog.findViewById(R.id.close);
        open.setEnabled(true);
        close.setEnabled(true);

        open.setOnClickListener(new View.OnClickListener());{
            public void onClick(View,view){
                Toast.makeText(getApplicationContext(), "I am a lost dog!", Toast.LENGTH_LONG).show()
            }
        }
        close.setOnClickListener(new View.OnClickListener());{
            public void onClick(View,view){
                myDialog.cancel();
            }
        }
        myDialog.show();
    }
}