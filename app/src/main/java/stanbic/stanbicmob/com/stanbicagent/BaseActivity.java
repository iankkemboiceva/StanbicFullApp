package stanbic.stanbicmob.com.stanbicagent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by ian on 7/23/2018.
 */

public class BaseActivity extends AppCompatActivity {
    public static final long TIMEOUT_IN_MILLI = 1000*60*3;
    public static final String PREF_FILE = "App_Pref";
    public static final String KEY_SP_LAST_INTERACTION_TIME = "KEY_SP_LAST_INTERACTION_TIME";
    SessionManagement session;

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        if (isValidLogin())
            getSharedPreference().edit().putLong(KEY_SP_LAST_INTERACTION_TIME, System.currentTimeMillis()).apply();
        else gohome();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isValidLogin())
            getSharedPreference().edit().putLong(KEY_SP_LAST_INTERACTION_TIME, System.currentTimeMillis()).apply();
        else gohome();
    }

    public SharedPreferences getSharedPreference() {
        return getSharedPreferences(PREF_FILE, MODE_PRIVATE);
    }

    public boolean isValidLogin() {
        long last_edit_time = getSharedPreference().getLong(KEY_SP_LAST_INTERACTION_TIME, 0);
        return last_edit_time == 0 || System.currentTimeMillis() - last_edit_time < TIMEOUT_IN_MILLI;
    }

    public void gohome() {
        Intent intent = new Intent(this, FMobActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
     //   Toast.makeText(this, "User logout due to inactivity", Toast.LENGTH_SHORT).show();
        getSharedPreference().edit().remove(KEY_SP_LAST_INTERACTION_TIME).apply(); // make shared preference null.
    }

    public  void LogOut(){
        session = new SessionManagement(this);
        session.logoutUser();

        // After logout redirect user to Loing Activity
        finish();
        Intent i = new Intent(getApplicationContext(), SignInActivity.class);

        // Staring Login Activity
        startActivity(i);
        Toast.makeText(
                getApplicationContext(),
                "You have been locked out of the app.Please call customer care for further details",
                Toast.LENGTH_LONG).show();
        // Toast.makeText(getApplicationContext(), "You have logged out successfully", Toast.LENGTH_LONG).show();

    }
}