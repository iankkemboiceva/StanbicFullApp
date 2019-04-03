package stanbic.stanbicmob.com.stanbicagent;

import android.support.v7.app.ActionBarActivity;

import security.SecurityLayer;

/**
 * Created by deeru on 15-07-2015.
 */
public class ControlActivity extends ActionBarActivity {
    private static final String TAG=ControlActivity.class.getName();

    /**
     * Gets reference to global Application
     * @return must always be type of ControlApplication! See AndroidManifest.xml
     */
    public ControlApplication getApp()
    {
        return (ControlApplication )this.getApplication();
    }

    @Override
    public void onUserInteraction()
    {
        super.onUserInteraction();
        getApp().touch();
        SecurityLayer.Log(TAG, "User interaction to " + this.toString());
    }
}
