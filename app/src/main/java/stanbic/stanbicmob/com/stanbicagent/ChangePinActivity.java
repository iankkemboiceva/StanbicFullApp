package stanbic.stanbicmob.com.stanbicagent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import okhttp3.OkHttpClient;
import rest.ApiClient;
import rest.ApiInterface;
import rest.ApiSecurityClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import security.EncryptTransactionPin;
import security.SecurityLayer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChangePinActivity extends BaseActivity implements View.OnClickListener {
    ProgressDialog pDialog;
    EditText et,et2,oldpin;
    Button btnok;
    SessionManagement session;
    ProgressDialog prgDialog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        prgDialog2 = new ProgressDialog(this);
        prgDialog2.setMessage("Loading....");
        prgDialog2.setCancelable(false);


        session = new SessionManagement(this);
        oldpin = (EditText) findViewById(R.id.oldpin);
        et = (EditText) findViewById(R.id.pin);
        et2 = (EditText)findViewById(R.id.cpin);
        btnok = (Button) findViewById(R.id.button2);
        btnok.setOnClickListener(this);


        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Loading");
        pDialog.setCancelable(false);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {




        if (v.getId() == R.id.button2) {
            if (Utility.checkInternetConnection(this)) {
                String npin = et.getText().toString();
                String confnpin = et2.getText().toString();
                String oldpinn = oldpin.getText().toString();
                if (Utility.isNotNull(oldpinn)) {
                    if (Utility.isNotNull(npin)) {
                        if (confnpin.equals(npin)) {
                            if (!oldpinn.equals(npin)) {
                                if(npin.length() == 5 && oldpinn.length() == 5){

                                    //    pDialog.show();
                                    if(Utility.findweakPin(npin)){
                                        OkHttpClient client = new OkHttpClient();



                                        String encrypted1 = null;
                                        String encrypted2 = null;

                                        encrypted1 = Utility.b64_sha256(oldpinn);
                                        encrypted2 = Utility.b64_sha256(npin);
                                        ApiInterface apiService =
                                                ApiClient.getClient().create(ApiInterface.class);
                                        String usid = Utility.gettUtilUserId(getApplicationContext());
                                        String agentid = Utility.gettUtilAgentId(getApplicationContext());
                                        String mobnoo = Utility.gettUtilMobno(getApplicationContext());
                                        SecurityLayer.Log("Chg Pin URL", "1/" + usid + "/" + agentid + "/" + "0000/" + encrypted1 + "/" + encrypted2);
                                        String params = "1/"+ usid+"/"+agentid+"/"+mobnoo+"/"+ encrypted1+"/";






                                        String lgparams = "1" + "/"+usid+"/" + encrypted1  + "/"+mobnoo;
                                      //  invokeCheckRef(params);
                                        LogRetro(lgparams,params,npin);


                       /* Call<ChangePinModel> call = apiService.getChngPin("1", usid, agentid, "0000", encrypted1, encrypted2);
                        call.enqueue(new Callback<ChangePinModel>() {
                            @Override
                            public void onResponse(Call<ChangePinModel> call, Response<ChangePinModel> response) {

                                if (!(response.body() == null)) {
                                    String responsemessage = response.body().getMessage();
                                    String respcode = response.body().getRespCode();
                                    SecurityLayer.Log("Response Message", responsemessage);

                                    if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {


                                        if (respcode.equals("00")) {

                                            getActivity().finish();
                                            startActivity(new Intent(getActivity(), SignInActivity.class));
                                            Toast.makeText(
                                                    getActivity(),
                                                    "Pin change successful.Proceed to sign in with your new pin" ,
                                                    Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(
                                                    getActivity(),
                                                    "" + responsemessage,
                                                    Toast.LENGTH_LONG).show();
                                        }


                                    } else {

                                        Toast.makeText(
                                                getActivity(),
                                                "There was an error on your request",
                                                Toast.LENGTH_LONG).show();


                                    }
                                } else {

                                    Toast.makeText(
                                            getActivity(),
                                            "There was an error on your request",
                                            Toast.LENGTH_LONG).show();


                                }
                                pDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<ChangePinModel> call, Throwable t) {
                                // Log error here since request failed
                                SecurityLayer.Log("throwable error", t.toString());


                                Toast.makeText(
                                        getActivity(),
                                        "There was an error on your request",
                                        Toast.LENGTH_LONG).show();


                                pDialog.dismiss();
                            }
                        });*/

                                    }
                                    else {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "You have set a weak PIN for New Pin Value.Please ensure you have selected a strong PIN",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                                else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Please ensure the Confirm New Pin and New Pin values are 5 digit length",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Please ensure Current Pin and New Pin values are not the same",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Please ensure the Confirm New Pin and New Pin values are  the same",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "Please enter a valid value for New pin",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Please enter a valid value for Current pin",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void changepinsec(String params) {
        if(!(prgDialog2 == null) ) {
            prgDialog2.show();
        }
        Log.v("In chpinsec","Innn");
        String endpoint= "login/changepin.action";


        String usid = Utility.gettUtilUserId(getApplicationContext());
        String agentid = Utility.gettUtilAgentId(getApplicationContext());


        String urlparams = "";
        try {
            urlparams = SecurityLayer.genURLCBC(params,endpoint,getApplicationContext());
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
                    //obj = Utility.onresp(obj,getApplicationContext());
                    obj = SecurityLayer.decryptTransaction(obj, getApplicationContext());
                    SecurityLayer.Log("decrypted_response", obj.toString());

                    String respcode = obj.optString("responseCode");
                    String responsemessage = obj.optString("message");
                    String respfee = obj.optString("fee");


                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                        if (!(Utility.checkUserLocked(respcode))) {
                            if (!(response.body() == null)) {
                                if (respcode.equals("00")) {
                                    finish();
                                    Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Pin change successful.Proceed to sign in with your new pin" ,
                                            Toast.LENGTH_LONG).show();

                                } else if (respcode.equals("93")) {



                                   /* Fragment fragment = new CashDepoTrans();
                                    String title = "Cash Transfer";

                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    //  String tag = Integer.toString(title);
                                    fragmentTransaction.replace(R.id.container_body, fragment, title);
                                    fragmentTransaction.addToBackStack(title);
                                    ((FMobActivity) getApplicationContext())
                                            .setActionBarTitle(title);
                                    fragmentTransaction.commit();*/
                                    onBackPressed();
                                    Toast.makeText(
                                            getApplicationContext(),
                                            responsemessage,
                                            Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            responsemessage,
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        responsemessage,
                                        Toast.LENGTH_LONG).show();
                            }
                        }else{
                            LogOut();
                        }
                    }



                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    if(!(getApplicationContext() == null)) {
                        Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                        SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());
                    }
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    if(!(getApplicationContext() == null)) {
                        SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());
                    }
                    // SecurityLayer.Log(e.toString());
                }
                if(!(prgDialog2 == null) && prgDialog2.isShowing() && getApplicationContext() != null) {
                    prgDialog2.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                SecurityLayer.Log("Throwable error",t.toString());

                if(!(prgDialog2 == null) && prgDialog2.isShowing() && getApplicationContext() != null) {
                    prgDialog2.dismiss();
                }
                if(!(getApplicationContext() == null)) {
                    Toast.makeText(
                            getApplicationContext(),
                            "There was an error processing your request",
                            Toast.LENGTH_LONG).show();
                    SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());

                }
            }
        });

    }

    public void SetForceOutDialog(String msg, final String title, final Context c) {
        if (!(c == null)) {
            new MaterialDialog.Builder(this)
                    .title(title)
                    .content(msg)

                    .negativeText("CONTINUE")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {

                            dialog.dismiss();
                            finish();
                            session.logoutUser();

                            // After logout redirect user to Loing Activity
                            Intent i = new Intent(c, SignInActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            // Staring Login Activity
                            startActivity(i);

                        }
                    })
                    .show();
        }
    }

    private void invokeCheckRef(final String params) {
        final ProgressDialog pro = new ProgressDialog(this);
        pro.setMessage("Loading...");
        pro.setTitle("");
        pro.setCancelable(false);
        pro.show();
        final AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(35000);

        String endpoint= "login/changepin.action";

        String url = "";
        try {
            url = ApplicationConstants.NET_URL+ SecurityLayer.genURLCBC(params,endpoint,this);
            //SecurityLayer.Log("cbcurl",url);
            SecurityLayer.Log("RefURL",url);
            SecurityLayer.Log("refurl", url);
            SecurityLayer.Log("params", params);
        } catch (Exception e) {
            SecurityLayer.Log("encryptionerror",e.toString());
        }



        client.post(url, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                pro.dismiss();
                try {
                    // JSON Object
                    SecurityLayer.Log("response..:", response);
                    JSONObject obj = new JSONObject(response);
                    //obj = Utility.onresp(obj,getActivity());
                    obj = SecurityLayer.decryptTransaction(obj, getApplicationContext());
                    SecurityLayer.Log("decrypted_response", obj.toString());

                    String respcode = obj.optString("responseCode");
                    String responsemessage = obj.optString("message");


                    //  JSONObject datas = obj.optJSONObject("data");
                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if (Utility.isNotNull(respcode) && Utility.isNotNull(responsemessage)) {
                        SecurityLayer.Log("Response Message", responsemessage);

                        if (respcode.equals("00")) {

                            finish();
                            Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Pin change successful.Proceed to sign in with your new pin" ,
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
                    Toast.makeText(getApplicationContext(), getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // SecurityLayer.Log(e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {

                // Hide Progress Dialog
                pro.dismiss();
                SecurityLayer.Log("error:", error.toString());
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "We are unable to process your request at the moment. Please try again later", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "We are unable to process your request at the moment. Please try again later", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), getText(R.string.conn_error), Toast.LENGTH_LONG).show();

                }
            }
        });
    }




    private void LogRetro(final String params,final String chgpinparams,final  String npin) {

        if(!(prgDialog2 == null) ) {
            prgDialog2.show();
        }
        String endpoint= "login/login.action/";

        String urlparams = "";
        try {
            urlparams = SecurityLayer.generalLogin(params,"23322",getApplicationContext(),endpoint);
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
                    obj = SecurityLayer.decryptGeneralLogin(obj, getApplicationContext());
                    SecurityLayer.Log("decrypted_response", obj.toString());

                    String respcode = obj.optString("responseCode");
                    String responsemessage = obj.optString("message");




                    JSONObject datas = obj.optJSONObject("data");

                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if (Utility.isNotNull(respcode) && Utility.isNotNull(responsemessage)) {
                        SecurityLayer.Log("Response Message", responsemessage);

                        if (respcode.equals("00")) {
                            prgDialog2.dismiss();
                            if (!(datas == null)) {
                                session.setString(SessionManagement.CHKLOGIN,"Y");

                                String agentid = datas.optString("agent");
                                String userid = datas.optString("userId");
                                String username = datas.optString("userName");
                                String email = datas.optString("email");
                                String lastl = datas.optString("lastLoggedIn");
                                String mobno = datas.optString("mobileNo");
                                String accno = datas.optString("acountNumber");
                                String pubkey = datas.optString("publicKey");
                                session.SetAgentID(agentid);
                                session.SetUserID(userid);
                                session.putCustName(username);
                                session.putEmail(email);
                                session.putLastl(lastl);

                                session.putAccountno(accno);

                                session.setString("Base64image","N");
                                session.setString(SessionManagement.KEY_SETBANKS,"N");
                                session.setString(SessionManagement.KEY_SETBILLERS,"N");
                                session.setString(SessionManagement.KEY_SETWALLETS,"N");
                                session.setString(SessionManagement.KEY_SETAIRTIME,"N");

                                session.setString(SessionManagement.PUBLICKEY,pubkey);
                                String encryptednewpin = Utility.b64_sha256(npin);
                                Log.v("Public Key",pubkey);
                                Log.v("Encrypted new pin",encryptednewpin);
                                boolean checknewast = session.checkAst();
                              /*  if (checknewast == false) {

                                    finish();
                                    Intent i = new Intent(getApplicationContext(), FMobActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    // Staring Login Activity
                                    startActivity(i);

                                } else {
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), AdActivity.class));

                                }*/
String finalparams = chgpinparams+encryptednewpin;
                                Log.v("Final params",finalparams);
                                changepinsec(finalparams);
                            }
                        }
                        else {

                            Toast.makeText(
                                    getApplicationContext(),
                                    responsemessage,
                                    Toast.LENGTH_LONG).show();

prgDialog2.dismiss();
                        }

                    }
                    else {

                        Toast.makeText(
                                getApplicationContext(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();

prgDialog2.dismiss();
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
                prgDialog2.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                SecurityLayer.Log("Throwable error",t.toString());
                Toast.makeText(
                        getApplicationContext(),
                        "There was an error processing your request",
                        Toast.LENGTH_LONG).show();

                prgDialog2.dismiss();

            }
        });

    }
}
