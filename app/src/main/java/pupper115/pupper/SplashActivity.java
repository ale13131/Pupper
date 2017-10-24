package pupper115.pupper;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.StartupAuthResultHandler;
import com.amazonaws.mobile.auth.core.StartupAuthResult;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;

//Login
import com.amazonaws.mobile.auth.core.DefaultSignInResultHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.IdentityProvider;
import com.amazonaws.mobile.auth.core.StartupAuthErrorDetails;
import com.amazonaws.mobile.auth.core.StartupAuthResult;
import com.amazonaws.mobile.auth.core.StartupAuthResultHandler;
import com.amazonaws.mobile.auth.core.signin.AuthException;
import com.amazonaws.mobile.auth.ui.AuthUIConfiguration;
import com.amazonaws.mobile.auth.ui.SignInActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Context appContext = getApplicationContext();
        AWSConfiguration awsConfig = new AWSConfiguration(appContext);
        //For showing login page
        /*IdentityManager identityManager = new IdentityManager(appContext, awsConfig);
        IdentityManager.setDefaultIdentityManager(identityManager);
        identityManager.doStartupAuth(this, new StartupAuthResultHandler() {
            @Override
            public void onComplete(StartupAuthResult startupAuthResult) {
                // User identity is ready as unauthenticated user or previously signed-in user.
            }
        });*/

        // Go to the main activity
        final Intent intent = new Intent(this, LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
        this.finish();

        //Login
        final IdentityManager identityManager =
                IdentityManager.getDefaultIdentityManager();

        identityManager.doStartupAuth(this,
                new StartupAuthResultHandler() {
                    @Override
                    public void onComplete(final StartupAuthResult authResults) {
                        if (authResults.isUserSignedIn()) {
                            final IdentityProvider provider =
                                    identityManager.getCurrentIdentityProvider();

                            // If the user was  signed in previously with a provider,
                            // indicate that to them with a toast.
                            Toast.makeText(
                                    SplashActivity.this, String.format("Signed in with %s",
                                            provider.getDisplayName()), Toast.LENGTH_LONG).show();
                            goMain(SplashActivity.this);
                            return;

                        } else {
                            // Either the user has never signed in with a provider before
                            // or refresh failed with a previously signed in provider.

                            // Optionally, you may want to check if refresh
                            // failed for the previously signed in provider.

                            final StartupAuthErrorDetails errors =
                                    authResults.getErrorDetails();

                            if (errors.didErrorOccurRefreshingProvider()) {
                                final AuthException providerAuthException =
                                        errors.getProviderRefreshException();

                                // Credentials for previously signed-in provider could not be refreshed
                                // The identity provider name is available here using:
                                //     providerAuthException.getProvider().getDisplayName()

                            }

                            doSignIn(IdentityManager.getDefaultIdentityManager());
                            return;
                        }


                    }
                }, 2000);
    }

    private void doSignIn(final IdentityManager identityManager) {

        identityManager.setUpToAuthenticate(
                SplashActivity.this, new DefaultSignInResultHandler() {

                    @Override
                    public void onSuccess(Activity activity, IdentityProvider identityProvider) {
                        if (identityProvider != null) {

                            // Sign-in succeeded
                            // The identity provider name is available here using:
                            //     identityProvider.getDisplayName()

                        }

                        // On Success of SignIn go to your startup activity
                        activity.startActivity(new Intent(activity, MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }

                    @Override
                    public boolean onCancel(Activity activity) {
                        // Return false to prevent the user from dismissing
                        // the sign in screen by pressing back button.
                        // Return true to allow this.

                        return false;
                    }
                });

        AuthUIConfiguration config =
                new AuthUIConfiguration.Builder()
                        .userPools(true)
                        // .signInButton(FacebookButton.class)
                        // .signInButton(GoogleButton.class)
                        .build();

        Context context = SplashActivity.this;
        SignInActivity.startSignInActivity(context, config);
        SplashActivity.this.finish();
    }

    /** Go to the main activity. */
    private void goMain(final Activity callingActivity) {
        callingActivity.startActivity(new Intent(callingActivity, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        callingActivity.finish();
    }
}