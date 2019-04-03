package stanbic.stanbicmob.com.stanbicagent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import security.SecurityLayer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Splash extends Activity {
    SessionManagement session;
	private static int SPLASH_TIME_OUT = 3000;
    ProgressBar prgDialog;
 // String NET_URL = "https://mbanking.nationalbank.co.ke:8443";
  String NET_URL = "http://196.32.226.78:8010";

    TextView gm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsplash);
        session = new SessionManagement(this);
//Test Github commit
        gm = (TextView) findViewById(R.id.gm);

        session.putURL(NET_URL);
        if(!(Utility.isEmulator())) {
            if(!(Utility.isRooted())) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        Intent i = null;
                        //      session.logoutUser();


                        String chkreg = session.getString(SessionManagement.SESS_REG);

                        if ((chkreg == null) || chkreg.equals("")) {

                                i = new Intent(Splash.this, ActivateAgentBefore.class);
                                session.clearallPref();

                                startActivity(i);
                                finish();


                        } else {
                            boolean isloggedin = session.isLoggedIn();
                            if (isloggedin){
                                i = new Intent(Splash.this, FMobActivity.class);
                            }else{
                                i = new Intent(Splash.this, SignInActivity.class);
                            }
                            startActivity(i);
                            finish();
                        }




                        //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }


                }, SPLASH_TIME_OUT);
           }else{
                Toast.makeText(
                        getApplicationContext(),
                        "You have currently rooted your device hence cant access this app"
                        , Toast.LENGTH_LONG).show();
                finish();
            }
       }else{
            Toast.makeText(
                    getApplicationContext(),
                    "You are currently using a mobile Emulator which is not acceptable."
                    , Toast.LENGTH_LONG).show();
            finish();
        }

	}

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
