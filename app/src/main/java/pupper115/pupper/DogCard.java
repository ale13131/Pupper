package pupper115.pupper;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.*;
import com.mindorks.placeholderview.annotations.swipe.*;

/**
 * Created by Chris Yeh on 11/27/2017.
 * Unused in main program
 * Class created to hold and display dog info in cards to be swiped on
 */

@Layout(R.layout.activity_dog_card_view)
public class DogCard {

    // variable declaration
    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameAgeTxt)
    private TextView nameAgeTxt;

    @View(R.id.locationNameTxt)
    private TextView locationNameTxt;

    private DogProfile mProfile;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;

    // constructor
    public DogCard(Context context, DogProfile profile, SwipePlaceHolderView swipeView) {
        mContext = context;
        mProfile = profile;
        mSwipeView = swipeView;
    }

    // retrieve dog info
    @Resolve
    private void onResolved(){
        Glide.with(mContext).load(mProfile.getImageUrl()).into(profileImageView);
        nameAgeTxt.setText(mProfile.getName() + ", " + mProfile.getAge());
        locationNameTxt.setText(mProfile.getLocation());
    }

    // Swipe events
    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
        mSwipeView.addView(this);
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
    }

    // swipe event animations
    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }
}
