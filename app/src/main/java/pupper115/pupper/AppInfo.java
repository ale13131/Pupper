package pupper115.pupper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Josh on 12/1/2017.
 *
 * This is a screen that explains the basic information and control of the app. It is first called
 * when a new user is registered or when a user clicks on help
 */

public class AppInfo extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String userName;
    private Spinner infoSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_info);

        Intent data = getIntent();
        userName = data.getStringExtra("userName");

        infoSelect = (Spinner) findViewById(R.id.activitySelect);
        infoSelect.setOnItemSelectedListener(this);

        displayHowTo();
    }

    public void displayHowTo()
    {
        TextView displayMessage = (TextView) findViewById(R.id.displayMessage);
        String info = "Hello " + userName + ", here is how to use the best dog app, Pupper.\n";
        info = info + "\nPlease select a feature from the menu below to see more information on it";
        displayMessage.setText(info);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("Selected", Integer.toString(i));
        TextView infoMessage = (TextView) findViewById(R.id.displayHelpMessage);
        String infoDisplay;

        switch (i)
        {
            case 0:
                //Help Screen Info
                infoDisplay = "The Help Screen is this screen. This displays relevant information ";
                infoDisplay += "about the other parts of the app so you can understand it better. ";
                infoDisplay += "To find out more information about another part, select it from the";
                infoDisplay += " drop-down menu. To exit this screen, just press the back arrow ";
                infoDisplay += "on your navigation plane at the bottom of the screen";

                infoMessage.setText(infoDisplay);

                break;
            case 1:
                //Main Screen Info
                infoDisplay = "The Main Screen where the dog pictures are displayed! To access more";
                infoDisplay += " information on the dog, either swipe up or down, or select the ";
                infoDisplay += "More Info button at the bottom of the screen. To see the next dog, ";
                infoDisplay += "either swipe right to left or select the next dog button. To see ";
                infoDisplay += "the previous dogs, swipe left to right. To exit the app, press the ";
                infoDisplay += "back button and a confirmation message will be displayed";

                infoMessage.setText(infoDisplay);

                break;
            case 2:
                //Create a Dog Screen Info
                infoDisplay = "The Create a Dog Screen is where you would create a dog profile for";
                infoDisplay += " other users to see. You click on it and it will bring you to the ";
                infoDisplay += "page where you enter the info for your dog. If a field is not filled";
                infoDisplay += " when you select the button, a message will say that a field is ";
                infoDisplay += "empty. If all fields are filled, when you click the button it will ";
                infoDisplay += "have you select the picture from your phone, then return to the ";
                infoDisplay += "main screen. To exit, press the back button";

                infoMessage.setText(infoDisplay);
                break;
            case 3:
                //Settings Screen Info
                infoDisplay = "The Settings Screen where you can view your information and edit";
                infoDisplay += " your name, email or password. To confirm the change, you must ";
                infoDisplay += "enter your current password and then press the confirm settings ";
                infoDisplay += "button and it will return to the main screen. To exit the page ";
                infoDisplay += "press the back button to return to the main screen without ";
                infoDisplay += "changing any information";

                infoMessage.setText(infoDisplay);

                break;
            case 4:
                //Dog Profile Screen Info
                infoDisplay = "The Dog Profile Screen is where the information about the dog is";
                infoDisplay += " displayed. The number of likes and any comments about the dog ";
                infoDisplay += "will also be displayed. To like the dog, press the like button, ";
                infoDisplay += "to comment on the dog, press the comment button and you'll be brought";
                infoDisplay += " to the comment page to create the comment. To email the user about ";
                infoDisplay += "the dog, press the email button and email them. To exit this page, ";
                infoDisplay += "press the back button to return to the main screen";

                infoMessage.setText(infoDisplay);

                break;
            case 5:
                //Comment Screen Info
                infoDisplay = "The Comment Screen is where you create the comment for the specific";
                infoDisplay += " dog. You enter what you want to say about the dog and click on the";
                infoDisplay += " button. If there is nothing entered, it will display a message";
                infoDisplay += " and stay on the screen, else it will return to the dog profile and";
                infoDisplay += " you will see your comment at the bottom. To exit without commenting, ";
                infoDisplay += "simply press the back button on your navigation pane";

                infoMessage.setText(infoDisplay);

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Help Screen should always be selected at first, this will never be called
    }
}
