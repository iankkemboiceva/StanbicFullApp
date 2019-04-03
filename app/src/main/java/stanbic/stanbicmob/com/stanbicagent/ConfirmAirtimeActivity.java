package stanbic.stanbicmob.com.stanbicagent;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.security.KeyStore;

import model.GetFee;
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

public class ConfirmAirtimeActivity extends BaseActivity implements View.OnClickListener {
    TextView reccustid, recamo, rectelco, step2, txtfee,acbal;
    Button btnsub;
    String txtcustid, amou, narra, ednamee, ednumbb, serviceid, billid,agbalance;
    ProgressDialog prgDialog, prgDialog2;
    String telcoop;
    EditText amon, edacc, pno, txtamount, txtnarr, edname, ednumber;
    EditText etpin;
    public static final String KEY_TOKEN = "token";
    SessionManagement session;

String finalrespfee;
    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_airtime);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)



        session = new SessionManagement(this);
        reccustid = (TextView) findViewById(R.id.textViewnb2);
        etpin = (EditText) findViewById(R.id.pin);
        acbal = (TextView) findViewById(R.id.txtacbal);
        recamo = (TextView) findViewById(R.id.textViewrrs);
        rectelco = (TextView)findViewById(R.id.textViewrr);
        txtfee = (TextView) findViewById(R.id.txtfee);

        prgDialog2 = new ProgressDialog(this);
        prgDialog2.setMessage("Loading....");
        prgDialog2.setCancelable(false);


        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Loading....");
        prgDialog.setCancelable(false);
        step2 = (TextView) findViewById(R.id.tv);
        step2.setOnClickListener(this);
        txtfee = (TextView) findViewById(R.id.txtfee);
        btnsub = (Button) findViewById(R.id.button2);
        btnsub.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {



            txtcustid = intent.getStringExtra("mobno");
            amou = intent.getStringExtra("amou");
            telcoop = intent.getStringExtra("telcoop");
            String newamo = amou.replace(",","");
            String txtamou = Utility.returnNumberFormat(newamo);
            if (txtamou.equals("0.00")) {
                txtamou = amou;
            }
            billid = intent.getStringExtra("billid");
            serviceid = intent.getStringExtra("serviceid");
            reccustid.setText(txtcustid);


            recamo.setText(ApplicationConstants.KEY_NAIRA + txtamou);
            rectelco.setText(telcoop);
            amou = Utility.convertProperNumber(amou);
            getFeeSec();

        }
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

    public void getFee() {
        if ((prgDialog2 != null)) {
            prgDialog2.show();
        }
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        String usid = Utility.gettUtilUserId(getApplicationContext());
        String agentid = Utility.gettUtilAgentId(getApplicationContext());
        Call<GetFee> call = apiService.getFee("1", usid, agentid, "MMO", amou);
        call.enqueue(new Callback<GetFee>() {
            @Override
            public void onResponse(Call<GetFee> call, Response<GetFee> response) {
                if (!(response.body() == null)) {
                    String responsemessage = response.body().getMessage();
                    String respfee = response.body().getFee();
                    SecurityLayer.Log("Response Message", responsemessage);
                    if (respfee == null || respfee.equals("")) {
                        txtfee.setText("N/A");
                    } else {
                        respfee = Utility.returnNumberFormat(respfee);
                        finalrespfee = ApplicationConstants.KEY_NAIRA + respfee;
                        txtfee.setText(ApplicationConstants.KEY_NAIRA + respfee);
                    }
                    if ((!(getApplicationContext() == null))&&(prgDialog2 != null) && prgDialog2.isShowing()) {
                        prgDialog2.dismiss();
                    }
                } else {
                    txtfee.setText("N/A");
                }
            }

            @Override
            public void onFailure(Call<GetFee> call, Throwable t) {
                // Log error here since request failed
                SecurityLayer.Log("Throwable error", t.toString());
                Toast.makeText(
                        getApplicationContext(),
                        "There was an error processing your request",
                        Toast.LENGTH_LONG).show();
                if (!(getApplicationContext() == null)&& (prgDialog2 != null) && prgDialog2.isShowing()) {
                    prgDialog2.dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.button2) {
            txtcustid = Utility.convertMobNumber(txtcustid);
            if (Utility.checkInternetConnection(getApplicationContext())) {
                String agpin  = etpin.getText().toString();
                if (Utility.isNotNull(txtcustid)) {
                    if (Utility.isNotNull(amou)) {
                        if (Utility.isNotNull(agpin)) {
                            /*double dbamo = Double.parseDouble(amou);
                            Double dbagbal = Double.parseDouble(agbalance);
                            if(dbamo <= dbagbal){*/
                            String encrypted = null;

encrypted = Utility.b64_sha256(agpin);



                            ApiInterface apiService =
                                    ApiClient.getClient().create(ApiInterface.class);

                            String usid = Utility.gettUtilUserId(getApplicationContext());
                            String agentid = Utility.gettUtilAgentId(getApplicationContext());
                            final String mobnoo = "0"+Utility.gettUtilMobno(getApplicationContext());
                            String emaill = Utility.gettUtilEmail(getApplicationContext());

                            String params = "1/"+usid+"/"+billid+"/"+serviceid+"/"+amou+"/01/"+txtcustid+"/"+emaill+"/"+txtcustid+"/NA";


                            Intent intent  = new Intent(ConfirmAirtimeActivity.this,TransactionProcessingActivity.class);




                            intent.putExtra("mobno", txtcustid);
                            intent.putExtra("amou", amou);
                            intent.putExtra("telcoop", telcoop);

                            intent.putExtra("res", billid);
                            intent.putExtra("billid", billid);
                            intent.putExtra("serviceid", serviceid);
                            intent.putExtra("txpin", encrypted);
                            intent.putExtra("serv", "AIRT");
                            intent.putExtra("params", params);


                            startActivity(intent);
                           /* Fragment fragment = new TransactingProcessing();

                            fragment.setArguments(b);
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            //  String tag = Integer.toString(title);
                            fragmentTransaction.replace(R.id.container_body, fragment, "Final Conf Airtime");
                            fragmentTransaction.addToBackStack("Final Conf");
                            ((FMobActivity) getApplicationContext())
                                    .setActionBarTitle("Final Conf Airtime");
                            fragmentTransaction.commit();*/
                            //   AirtimeResp(params);


                            ClearPin();
                           /* }  else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "The amount set is higher than your agent balance",
                                        Toast.LENGTH_LONG).show();
                            }*/
                        }

                        else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Please enter a valid value for Agent PIN",
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                    else {
                        Toast.makeText(
                                getApplicationContext(),
                                "Please enter a valid value for Amount",
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(
                            getApplicationContext(),
                            "Please enter a value for Customer ID",
                            Toast.LENGTH_LONG).show();
                }
            }
        }

        if (view.getId() == R.id.tv) {
           /* Fragment fragment = new AirtimeTransf();


            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment, "Airtime");
            fragmentTransaction.addToBackStack("Airtime");
            ((FMobActivity) getApplicationContext())
                    .setActionBarTitle("Airtime");
            fragmentTransaction.commit();*/

            finish();



            Intent intent  = new Intent(ConfirmAirtimeActivity.this,AirtimeTransfActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        // put your code here...

    }

    public void ClearPin() {
        etpin.setText("");
    }

    private void getFeeSec() {
        if ((prgDialog2 != null)) {
            prgDialog2.show();
        }
        String endpoint = "billpayment/billFee.action";


        String usid = Utility.gettUtilUserId(getApplicationContext());
        String agentid = Utility.gettUtilAgentId(getApplicationContext());

        String params = "1/" + usid + "/" + billid + "/" + amou;
        String urlparams = "";
        try {
            urlparams = SecurityLayer.genURLCBC(params, endpoint, getApplicationContext());
            //SecurityLayer.Log("cbcurl",url);
            SecurityLayer.Log("RefURL", urlparams);
            SecurityLayer.Log("refurl", urlparams);
            SecurityLayer.Log("params", params);
        } catch (Exception e) {
            SecurityLayer.Log("encryptionerror", e.toString());
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

                    agbalance = obj.optString("data");
                    JSONObject datas = obj.optJSONObject("data");
                    if(Utility.isNotNull(agbalance)) {
                        acbal.setText(agbalance+ApplicationConstants.KEY_NAIRA);
                    }


                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                        if (!(Utility.checkUserLocked(respcode))) {
                            if (!(response.body() == null)) {
                                if (respcode.equals("00")) {

                                    SecurityLayer.Log("Response Message", responsemessage);

                                    String respfee = datas.optString("fee");

//                                    SecurityLayer.Log("Respnse getResults",datas.toString());
                                    if (respfee == null || respfee.equals("")) {
                                        txtfee.setText("N/A");
                                    } else {
                                        respfee = Utility.returnNumberFormat(respfee);
                                        txtfee.setText(ApplicationConstants.KEY_NAIRA + respfee);
                                    }

                                } else if (respcode.equals("93")) {

                                    Toast.makeText(
                                            getApplicationContext(),
                                            responsemessage,
                                            Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                   /* Fragment fragment = new AirtimeTransf();
                                    String title = "Airtime";

                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    //  String tag = Integer.toString(title);
                                    fragmentTransaction.replace(R.id.container_body, fragment, title);
                                    fragmentTransaction.addToBackStack(title);
                                    ((FMobActivity) getApplicationContext())
                                            .setActionBarTitle(title);
                                    fragmentTransaction.commit();
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Please ensure amount set is below the set limit",
                                            Toast.LENGTH_LONG).show();*/

                                } else {
                                    btnsub.setVisibility(View.GONE);
                                    Toast.makeText(
                                            getApplicationContext(),
                                            responsemessage,
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                txtfee.setText("N/A");
                            }
                        }else{
                           LogOut();
                        }
                    }


                } catch (JSONException e) {
                    Utility.errornexttoken();
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    Utility.errornexttoken();
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // SecurityLayer.Log(e.toString());
                }
                try {
                    if ((!(getApplicationContext() == null))&&(prgDialog2 != null) && prgDialog2.isShowing()) {
                        prgDialog2.dismiss();
                    }
                } catch (final IllegalArgumentException e) {
                    // Handle or log or ignore
                } catch (final Exception e) {
                    // Handle or log or ignore
                } finally {
                    // prgDialog2 = null;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                Utility.errornexttoken();
                SecurityLayer.Log("Throwable error", t.toString());

                try {
                    if ((!(getApplicationContext() == null))&&(prgDialog2 != null) && prgDialog2.isShowing()) {
                        prgDialog2.dismiss();
                    }
                } catch (final IllegalArgumentException e) {
                    // Handle or log or ignore
                } catch (final Exception e) {
                    // Handle or log or ignore
                } finally {
                    //prgDialog2 = null;
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





    private void dismissProgressDialog() {
        if (prgDialog != null && prgDialog.isShowing()) {
            prgDialog.dismiss();
        }
        if(prgDialog2!=null && prgDialog2.isShowing()){

            prgDialog2.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }


    public  void LogOut(){
        session.logoutUser();

        // After logout redirect user to Loing Activity
        finish();
        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Staring Login Activity
        startActivity(i);
        Toast.makeText(
                getApplicationContext(),
                "You have been locked out of the app.Please call customer care for further details",
                Toast.LENGTH_LONG).show();
        // Toast.makeText(getApplicationContext(), "You have logged out successfully", Toast.LENGTH_LONG).show();

    }
}
