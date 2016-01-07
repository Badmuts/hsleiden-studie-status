package nl.antonsteenvoorden.ikpmd.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import com.activeandroid.ActiveAndroid;
import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import nl.antonsteenvoorden.ikpmd.App;
import nl.antonsteenvoorden.ikpmd.R;
import nl.antonsteenvoorden.ikpmd.model.Module;

public class SplashScreen extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    public static final String PREFS_NAME = "LaunchPreferences";
    SharedPreferences settings;

    @Bind(R.id.splashScreenWelcome) TextView welkom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        settings = getSharedPreferences(PREFS_NAME, 0);
        welkom = (TextView) findViewById(R.id.splashScreenWelcome);

        if (settings.getBoolean("first_run", true)) {
            welkom.setText("");
        } else {
            welkom.setText("Welkom terug " + String.valueOf(settings.getString("name", ""))+ " !");
        }

        handleAfterSplash();
    }

    private void handleAfterSplash() {

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {

                // This method will be executed once the timer is over
                if (settings.getBoolean("first_run", true)) {
                    try {
                        ((App) getApplication()).getModuleService().findAll(successListener(), errorListener(SplashScreen.this));
                    } catch(Exception e) {
                        Log.d("TRY CATCH BLOCK", e.toString());
                        Snackbar snackbar = Snackbar
                                .make((RelativeLayout) findViewById(R.id.splashScreenLayout),
                                        "Bij de eerste keer opstarten is een internet verbinding nodig!",
                                        Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                    //the app is being launched for first time, do something
                    Log.d("Comments", "First time, opening get to know you screen");

                    Intent i = new Intent(SplashScreen.this, WelcomeActivity.class);
                    startActivity(i);
                    // record the fact that the app has been started at least once
                     settings.edit().putBoolean("first_run", false).commit();
                } else {
                    Log.d("Comments", "Opening main activity");
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                }
                // close this activity
                finish();
            }

        }, SPLASH_TIME_OUT);
    }

    private Response.Listener<List<Module>> successListener() {
        return new Response.Listener<List<Module>>() {
            @Override
            public void onResponse(List<Module> modules) {
                ActiveAndroid.beginTransaction();
                try {
                    for (Module module: modules) {
                        module.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
            }
        };
    }

    private Response.ErrorListener errorListener(final SplashScreen splashScreen) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(splashScreen.getCurrentFocus(), "Kan modules niet ophalen", Snackbar.LENGTH_LONG).show();
                Log.e("Volley error", error.getMessage());
            }
        };
    }
}
