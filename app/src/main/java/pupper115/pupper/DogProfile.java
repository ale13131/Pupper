package pupper115.pupper;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
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
        ImageView img = findViewById(R.id.imageViewDogPhoto);
        Picasso.with(this).load("https://s3.amazonaws.com/pupper-user-info/" + imageName).noFade()
                .resize(1200, 1800).centerInside().into(img);
    }

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("url")
    @Expose
    private String imageUrl;

    @SerializedName("age")
    @Expose
    private Integer age;

    @SerializedName("location")
    @Expose
    private String location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
