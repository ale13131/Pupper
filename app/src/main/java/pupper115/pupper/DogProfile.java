package pupper115.pupper;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DogProfile extends AppCompatActivity {
    Drawable dogImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_profile);
        Bundle extras = getIntent().getExtras();
        String dogImage = extras.getString("dogImage");
        setImage(dogImage);
        //TextView name= (TextView) findViewById(R.id.textViewDogName);
        //TextView info= (TextView) findViewById(R.id.textViewDogInfo);


    }

    private void setImage( String imageName){
        ImageView img = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this).load("https://s3.amazonaws.com/pupper-user-info/" + imageName).noFade()
                .resize(1200, 1800).centerInside().into(img);
    }
}
