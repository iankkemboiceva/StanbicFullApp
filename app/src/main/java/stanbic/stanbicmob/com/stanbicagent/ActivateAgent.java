package stanbic.stanbicmob.com.stanbicagent;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import adapter.RegIDPojo;

import rest.ApiInterface;
import rest.ApiSecurityClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import security.SecurityLayer;


import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class ActivateAgent extends AppCompatActivity implements View.OnClickListener  {
Button btnnext;
TextView btnresp;
    EditText agentid,agentpin,phonenumber;
    private static final long INTERVAL = 1000 * 10;


    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 2000, FASTEST_INTERVAL = 2000; // = 5 seconds
String mlat;
    String mlongt;
    //Context applicationContext;
    SessionManagement session;
    String regId ;
    String encrypted;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public static final String AGMOB = "agmobno";
    ProgressDialog pDialog;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;

    private GoogleApiClient googleApiClient;

    private Location mLastLocation;


    private FusedLocationProviderClient mFusedLocationProviderClient;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted;

    protected LocationManager locationManager;
    protected LocationListener locationListener;

    boolean GpsStatus ;
    private static final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_agent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        agentid  = (EditText) findViewById(R.id.agentid);
        agentpin  = (EditText) findViewById(R.id.agentpin);
        phonenumber  = (EditText) findViewById(R.id.agentphon);
        btnnext = (Button) findViewById(R.id.button2);
        btnnext.setOnClickListener(this);
        btnresp = (TextView) findViewById(R.id.button5);
        btnresp.setOnClickListener(this);
        agentid.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        session = new SessionManagement(getApplicationContext());
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ....");
        // Set Cancelable as False

        pDialog.setCancelable(false);
        if (isGooglePlayServicesAvailable()) {
            mFusedLocationProviderClient = getFusedLocationProviderClient(this);
            //
          //  startLocationUpdates();
            /*createLocationRequest();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    //      .enableAutoManage(this, 34992, this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();





            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);*/
            //  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            getLocationPermission();

        }else{
            Toast.makeText(this,"Google Play services is not updated",Toast.LENGTH_LONG);
        }

       // testResp();
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }



    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            //   getDeviceLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }
    }

  /*  public void getDeviceLocation() {

            mFusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLastLocation = task.getResult();
                                mlat = Double.toString(mLastLocation.getLatitude());
                                mlongt = Double.toString(mLastLocation.getLongitude());

                                SecurityLayer.Log("Tag", "Success ");
                            } else {
                                SecurityLayer.Log("Tag", "Inside getLocation function. Error while getting location");

                            }
                        }
                    });

    }*/
    private void getDeviceLocation1() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {

                SecurityLayer.Log("getdeviceloc","started");
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        SecurityLayer.Log("getdeviceloc","oncomplete");
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            SecurityLayer.Log("getdeviceloc","tasksuccessful");
                            mLastKnownLocation = (Location) task.getResult();
                            if(!(mLastKnownLocation == null)) {
                                CheckGpsStatus() ;
                                SecurityLayer.Log("getdeviceloc","mlastknown");
                                SecurityLayer.Log("chkgpsstatus",Boolean.toString(GpsStatus));
                                if(GpsStatus == true) {
                               /*     SendLoc(String.valueOf(mLastKnownLocation.getLatitude()),
                                            String.valueOf(mLastKnownLocation.getLongitude()));*/

                               mlat = String.valueOf(mLastKnownLocation.getLatitude());
                                    mlongt = String.valueOf(mLastKnownLocation.getLongitude());


                                   SecurityLayer.Log("longitvalue",mlat+","+mlongt);
                                }else{
                                        Toast.makeText(getApplicationContext(),"Please enable location services", Toast.LENGTH_LONG).show();

                                }
                            }
                            else {
                                SecurityLayer.Log("getdeviceloc","Current location is null. Using defaults.");
                                Log.v("", "Current location is null. Using defaults.");
                                Log.v("", "Exception: %s", task.getException());

                            }
                        } else {
                            Log.v("", "Current location is null. Using defaults.");
                            Log.v("", "Exception: %s", task.getException());

                        }
                    }
                });
            }else {
                SecurityLayer.Log("", "Please enable location");
                //  Toast.makeText(getApplicationContext(),"Please enable location permissions", Toast.LENGTH_LONG).show();



                getLocationPermission();



            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void updateAndroidSecurityProvider(Activity callingActivity) {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            SecurityLayer.Log("SecurityException", "Google Play Services not available.");
        }
    }
private void checkPlayServices(){
    GoogleApiAvailability api = GoogleApiAvailability.getInstance();
    int code = api.isGooglePlayServicesAvailable(getApplicationContext());
    if (code == ConnectionResult.SUCCESS) {
        // Do Your Stuff Here
        registerInBackground();
    } else {
        Toast.makeText(
                getApplicationContext(),
                "Please ensure you have installed Google Play Services"
                     , Toast.LENGTH_LONG).show();
        registerInBackground();
    }
}

    public void receivedSMS(String message)
    {
        try
        {

            phonenumber.setText(message);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void registerInBackground() {
        pDialog.show();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
             //  getDeviceLocation();
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            /*    if (!TextUtils.isEmpty(regId)) {*/



                    String ip = Utility.getIP(getApplicationContext());
                    String mac = Utility.getMacAddress(getApplicationContext());
                    String serial = Utility.getSerial();
                    String version = Utility.getDevVersion();
                    String devtype = Utility.getDevModel();
                    String imei = Utility.getDevImei(getApplicationContext());
                    if (Utility.checkInternetConnection(getApplicationContext())){
                        if (Utility.isNotNull(imei) && Utility.isNotNull(serial)) {
                            if(regId == null){
                                regId = "JKKS";
                            }
                     //    if(mlat != null && mlongt != null){

                            SecurityLayer.Log("Longt and Langit", mlat+","+mlongt);
                             //   final   String agid = agentid.getText().toString();
                             String agid = Utility.gettUtilUserId(getApplicationContext());
                             String agpin = agentpin.getText().toString();
                             String phnnumb = phonenumber.getText().toString();
                             phnnumb = Utility.CheckNumberZero(phnnumb);

                             encrypted = Utility.b64_sha256(agpin);
                             SecurityLayer.Log("Encrypted Pin", encrypted);


                             String params = "1/" + agid + "/" + phnnumb + "/" + encrypted + "/"+mlat+"/"+mlongt+"/" + mac + "/" + ip + "/" + imei + "/" + serial + "/" + version + "/" + devtype + "/" + regId;

                             RetroDevReg(params);

                         /*   }else{
                             Toast.makeText(
                                     getApplicationContext(),
                                     "Please ensure you have set location services correctly",
                                     Toast.LENGTH_LONG).show();

                             pDialog.hide();
                         }*/

                        }
                    } else {

                        Toast.makeText(
                                getApplicationContext(),
                                "Please ensure this device has an IMEI number",
                                Toast.LENGTH_LONG).show();



                    }

            }
        }.execute(null, null, null);
    }

    public void CheckGpsStatus(){

        locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    public static void locationChecker(GoogleApiClient mGoogleApiClient, final Activity activity) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    activity, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button2){


            String agpin = agentpin.getText().toString();
            String phnnumb = phonenumber.getText().toString();

                if (Utility.isNotNull(agpin)) {
                    if (Utility.isNotNull(phnnumb)) {
                        if (Utility.checkInternetConnection(getApplicationContext())) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                insertDummyContactWrapper();
                            } else {
                                // Pre-Marshmallow
                             //   registerInBackground();
                                checkPlayServices();
                            }



                        }

                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "Please enter a valid value for OTP",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Please enter a valid value for activation key",
                            Toast.LENGTH_LONG).show();
                }


        }


        if(v.getId() == R.id.button5){
            String agid = Utility.gettUtilUserId(getApplicationContext());
            String params = "1/"+agid;
            pDialog.show();
            GenerateOTPParams(params);



        }
    }

    private void GenerateOTPParams(String params) {



        String endpoint= "otp/generateotp.action/";

        String urlparams = "";
        try {
            urlparams = SecurityLayer.firstLogin(params,endpoint,getApplicationContext());
            //SecurityLayer.Log("cbcurl",url);
            SecurityLayer.Log("RefURL",urlparams);
            SecurityLayer.Log("refurl", urlparams);
            SecurityLayer.Log("params", params);
        } catch (Exception e) {
            SecurityLayer.Log("encryptionerror",e.toString());
        }





        ApiInterface apiService =
                ApiSecurityClient.getClient(getApplicationContext()).create(ApiInterface.class);


        Call<String> call = apiService.setGenericRequestRaw(urlparams);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    // JSON Object
                    SecurityLayer.Log("response..:", response.body());


                    JSONObject obj = new JSONObject(response.body());
                 /*   JSONObject jsdatarsp = obj.optJSONObject("data");
                    SecurityLayer.Log("JSdata resp", jsdatarsp.toString());
                    //obj = Utility.onresp(obj,getActivity()); */
                    obj = SecurityLayer.decryptFirstTimeLogin(obj, getApplicationContext());
                    SecurityLayer.Log("decrypted_response", obj.toString());

                    String respcode = obj.optString("responseCode");
                    String responsemessage = obj.optString("message");



                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if (Utility.isNotNull(respcode) && Utility.isNotNull(responsemessage)) {
                        SecurityLayer.Log("Response Message", responsemessage);

                        if (respcode.equals("00")) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "OTP has been successfully resent",
                                    Toast.LENGTH_LONG).show();

                        }
                        else {

                            Toast.makeText(
                                    getApplicationContext(),
                                    responsemessage,
                                    Toast.LENGTH_LONG).show();


                        }

                    }
                    else {

                        Toast.makeText(
                                getApplicationContext(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();


                    }

                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // SecurityLayer.Log(e.toString());
                }
                if ((pDialog != null) && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                SecurityLayer.Log("Throwable error",t.toString());
                Toast.makeText(
                        getApplicationContext(),
                        "There was an error processing your request",
                        Toast.LENGTH_LONG).show();
                if ((pDialog != null) && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                //SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr));
            }
        });

    }


    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();


        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("Read Phone State");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }

            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

      //  registerInBackground();
        checkPlayServices();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);


            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ActivateAgent.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        mLocationPermissionGranted = false;
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    checkPlayServices();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(
                            getApplicationContext(),
                            "Please note we need to allow this permission to activate the app",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }



                case PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionGranted = true;
                    }
                
            }

        }
    }


    public void ClearFields(){

        agentid.setText("");
        agentpin.setText("");
        phonenumber.setText("");
    }

    private void RetroDevReg(String params) {

        SecurityLayer.Log("","Inside Retro Dev Reg");

        String endpoint= "reg/devReg.action/";

        String urlparams = "";
        try {
            urlparams = SecurityLayer.firstLogin(params,endpoint,getApplicationContext());
            //SecurityLayer.Log("cbcurl",url);
            SecurityLayer.Log("RefURL",urlparams);
            SecurityLayer.Log("refurl", urlparams);
            SecurityLayer.Log("params", params);
        } catch (Exception e) {
            SecurityLayer.Log("encryptionerror",e.toString());
        }





        ApiInterface apiService =
                ApiSecurityClient.getClient(getApplicationContext()).create(ApiInterface.class);


        Call<String> call = apiService.setGenericRequestRaw(urlparams);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    // JSON Object
                    SecurityLayer.Log("response..:", response.body());


                    JSONObject obj = new JSONObject(response.body());
                 /*   JSONObject jsdatarsp = obj.optJSONObject("data");
                    SecurityLayer.Log("JSdata resp", jsdatarsp.toString());
                    //obj = Utility.onresp(obj,getActivity()); */
                    obj = SecurityLayer.decryptFirstTimeLogin(obj, getApplicationContext());
                    SecurityLayer.Log("decrypted_response", obj.toString());

                    String respcode = obj.optString("responseCode");
                    String responsemessage = obj.optString("message");



                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if (Utility.isNotNull(respcode) && Utility.isNotNull(responsemessage)) {
                        SecurityLayer.Log("Response Message", responsemessage);

                        if (respcode.equals("00")) {
                            JSONObject datas = obj.optJSONObject("data");
                            String agent = datas.optString("agent");
                            if (!(datas == null)) {
                                final   String agid = agentid.getText().toString();
                                final   String mobno = phonenumber.getText().toString();
                                String status = datas.optString("status");
                                //    session.SetUserID(agid);
                                session.SetAgentID(agent);
                                session.setString(AGMOB,mobno);
                                if(status.equals("F")) {
                                    finish();
                                    Intent mIntent = new Intent(getApplicationContext(), ForceChangePin.class);
                                    mIntent.putExtra("pinna", encrypted);
                                    startActivity(mIntent);
                                }else{
                                    session.setString(SessionManagement.SESS_REG,"Y");

                                    finish();
                                    Intent mIntent = new Intent(getApplicationContext(), SignInActivity.class);
                                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mIntent);
                                }
                            }
                        }
                        else {

                            Toast.makeText(
                                    getApplicationContext(),
                                    responsemessage,
                                    Toast.LENGTH_LONG).show();


                        }

                    }
                    else {

                        Toast.makeText(
                                getApplicationContext(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();


                    }

                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // SecurityLayer.Log(e.toString());
                }
pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                SecurityLayer.Log("Throwable error",t.toString());
                Toast.makeText(
                        getApplicationContext(),
                        "There was an error processing your request",
                        Toast.LENGTH_LONG).show();
pDialog.dismiss();


            }
        });

    }




   /* // Trigger new location updates at interval
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }*/




    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

}
