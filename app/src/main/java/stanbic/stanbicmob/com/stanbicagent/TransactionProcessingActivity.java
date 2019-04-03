package stanbic.stanbicmob.com.stanbicagent;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vipul.hp_hp.library.Layout_to_Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;

import rest.ApiInterface;
import rest.ApiSecurityClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import security.SecurityLayer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TransactionProcessingActivity extends BaseActivity implements View.OnClickListener {
    Button btnsub;
    String recanno, amou ,narra, ednamee,ednumbb,txtname,strfee,stragcms,bankname,bankcode,txpin,newparams,serv ;
    String txtcustid,serviceid,billid,txtfee,strtref,strlabel,strbillnm,fullname,telcoop,marketnm;

    ProgressDialog prgDialog2;
    RelativeLayout rlsendname,rlsendno;
    EditText etpin;
    private FirebaseAnalytics mFirebaseAnalytics;
    String   txtrfc,txref;
    Layout_to_Image layout_to_image;  //Create Object of Layout_to_Image Class
    TextView txstatus,txdesc;
    LinearLayout relativeLayout;   //Define Any Layout
    Button shareImage,repissue;
    Bitmap bitmap;                  //Bitmap for holding Image of layout

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    Button btnconfirm;
    ProgressDialog pro ;
    String finpin;
    SessionManagement session;
    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {

            //SecurityLayer.Log(TAG, "Pin complete: " + pin);
            finpin = pin;
        }

        @Override
        public void onEmpty() {
            SecurityLayer.Log("Pin Empty", "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            //	SecurityLayer.Log(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_processing);

        prgDialog2 = new ProgressDialog(this);
        prgDialog2.setMessage("Loading....");
        prgDialog2.setCancelable(false);
        rlsendname = (RelativeLayout)findViewById(R.id.rlsendnam);
        rlsendno = (RelativeLayout) findViewById(R.id.rlsendnum);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)


        txstatus = (TextView) findViewById(R.id.txstatus);
        txdesc = (TextView) findViewById(R.id.txdesc);
        btnsub = (Button)findViewById(R.id.button2);
        btnsub.setOnClickListener(this);
        session = new SessionManagement(this);
        relativeLayout=(LinearLayout)findViewById(R.id.receipt);
        Intent intent = getIntent();
        if (intent != null) {
            serv = intent.getStringExtra("serv");
            if(serv.equals("CASHDEPO")) {
                recanno = intent.getStringExtra("recanno");
                amou = intent.getStringExtra("amou");
                narra = intent.getStringExtra("narra");
                ednamee = intent.getStringExtra("ednamee");
                ednumbb = intent.getStringExtra("ednumbb");
                txtname = intent.getStringExtra("txtname");
                txtrfc = intent.getStringExtra("refcode");
                String params  = intent.getStringExtra("params");
                stragcms = Utility.returnNumberFormat(intent.getStringExtra("agcmsn"));
                String trantype = intent.getStringExtra("trantype");
                strfee = intent.getStringExtra("fee");
                txpin = intent.getStringExtra("txpin");
                newparams = params;
                Log.v("Params",newparams+"/"+txpin);
                IntraDepoBankResp(newparams+"/"+txpin);

            }
            if(serv.equals("CASHTRAN")) {
                recanno = intent.getStringExtra("recanno");
                amou = intent.getStringExtra("amou");
                narra = intent.getStringExtra("narra");
                ednamee = intent.getStringExtra("ednamee");
                ednumbb = intent.getStringExtra("ednumbb");
                txtname = intent.getStringExtra("txtname");
                txtrfc = intent.getStringExtra("refcode");
                String params  = intent.getStringExtra("params");
                stragcms = Utility.returnNumberFormat(intent.getStringExtra("agcmsn"));
                String trantype = intent.getStringExtra("trantype");
                strfee = intent.getStringExtra("fee");
                txpin = intent.getStringExtra("txpin");
                newparams = params;
                Log.v("Params",newparams+"/"+txpin);
                IntraTranBankResp(newparams+"/"+txpin);

            }
            if(serv.equals("WDRAW")) {
                recanno = intent.getStringExtra("recanno");
                amou = intent.getStringExtra("amou");
                strfee = intent.getStringExtra("fee");
                txtname = intent.getStringExtra("txtname");
                txref = intent.getStringExtra("txref");
                txtrfc = intent.getStringExtra("refcode");

                String params  = intent.getStringExtra("params");
                stragcms = Utility.returnNumberFormat(intent.getStringExtra("agcmsn"));

                strfee = intent.getStringExtra("fee");

                txpin = intent.getStringExtra("txpin");
                newparams = params;
                Log.v("Params",newparams+"/"+txpin);



                WithdrawResp(newparams+"/"+txpin);

            }
            if(serv.equals("AIRT")) {

                strfee = intent.getStringExtra("fee");



                stragcms = Utility.returnNumberFormat(intent.getStringExtra("agcmsn"));

                strfee = intent.getStringExtra("fee");

                txtcustid = intent.getStringExtra("mobno");
                amou = intent.getStringExtra("amou");
                telcoop = intent.getStringExtra("telcoop");
                String params  = intent.getStringExtra("params");
                String txtamou = Utility.returnNumberFormat(amou);
                if(txtamou.equals("0.00")){
                    amou = txtamou;
                }

                billid = intent.getStringExtra("billid");
                serviceid = intent.getStringExtra("serviceid");
                txpin = intent.getStringExtra("txpin");
                newparams = params;
                Log.v("Params",newparams+"/"+txpin);

                AirtimeResp(newparams+"/"+txpin);

            }
            if(serv.equals("SENDOTB")) {
                recanno = intent.getStringExtra("recanno");
                amou = intent.getStringExtra("amou");
                narra = intent.getStringExtra("narra");
                ednamee = intent.getStringExtra("ednamee");
                ednumbb = intent.getStringExtra("ednumbb");
                txtname = intent.getStringExtra("txtname");
                bankname = intent.getStringExtra("bankname");
                bankcode = intent.getStringExtra("bankcode");
                strfee = intent.getStringExtra("fee");
                txtrfc = intent.getStringExtra("refcode");
                String params  = intent.getStringExtra("params");
                txpin = intent.getStringExtra("txpin");
                newparams = params;
                Log.v("Params",newparams+"/"+txpin);


                InterBankResp(newparams+"/"+txpin);

            }
            if(serv.equals("CABLETV")) {
                txtcustid = intent.getStringExtra("custid");
                amou = intent.getStringExtra("amou");
                narra = intent.getStringExtra("narra");
                ednamee = intent.getStringExtra("ednamee");
                ednumbb = intent.getStringExtra("ednumbb");
                strlabel = intent.getStringExtra("label");
                billid = intent.getStringExtra("billid");
                strbillnm = intent.getStringExtra("billname");
                serviceid = intent.getStringExtra("serviceid");
                strfee = intent.getStringExtra("fee");
                strtref = intent.getStringExtra("tref");
                fullname = intent.getStringExtra("fullname");
                String params  = intent.getStringExtra("params");
                txpin = intent.getStringExtra("txpin");
                if(Utility.checkStateCollect(serviceid)) {

                }
                newparams = params;
                Log.v("Params",newparams+"/"+txpin);




                PayBillResp(newparams+"/"+txpin);

            }
        }


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void IntraTranBankResp(String params) {
        prgDialog2.show();
        String endpoint= "transfer/intrabank.action";


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


                    SecurityLayer.Log("Intra Bank Resp", response.body());
                    SecurityLayer.Log("response..:", response.body());
                    JSONObject obj = new JSONObject(response.body());
                    //obj = Utility.onresp(obj,getApplicationContext());
                    obj = SecurityLayer.decryptTransaction(obj, getApplicationContext());
                    SecurityLayer.Log("decrypted_response", obj.toString());




                    JSONObject datas = obj.optJSONObject("data");
                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if(!(response.body() == null)) {
                        String respcode = obj.optString("responseCode");
                        String responsemessage = obj.optString("message");
                        String totfee = obj.optString("fee");
                        String agcmsn = obj.optString("commission");
                        String refcodee = "";

                        SecurityLayer.Log("Response Message", responsemessage);
                        SecurityLayer.Log("Response Code", respcode);
                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if (!(Utility.checkUserLocked(respcode))) {
                                if(respcode.equals("00")){

                                    String datetimee = "";
                                    if(!(datas == null)){

                                        datetimee = datas.optString("datetime");
                                        refcodee = datas.optString("ReferenceId");
                                    }
                                    Bundle b  = new Bundle();




                                    Intent intent  = new Intent(TransactionProcessingActivity.this,FinalConfDepoActivity.class);

                                   intent.putExtra("recanno",recanno);
                                    intent.putExtra("amou",amou);
                                    intent.putExtra("narra",narra);
                                    //    String refcodee = datas.optString("referenceCode");
                                    intent.putExtra("refcode",refcodee);
                                    intent.putExtra("ednamee",ednamee);
                                    intent.putExtra("ednumbb",ednumbb);
                                    intent.putExtra("txtname",txtname);
                                    intent.putExtra("datetime",datetimee);
                                    intent.putExtra("trantype","T");
                                    intent.putExtra("agcmsn",agcmsn);
                                    intent.putExtra("fee",totfee);
                                    startActivity(intent);
                                }
                                else if(respcode.equals("002")){
                                    Toast.makeText(
                                            getApplicationContext(), responsemessage,
                                            Toast.LENGTH_LONG).show();


                                    setAlertDialog();
                                }
                                else {
                                    new MaterialDialog.Builder(TransactionProcessingActivity.this)
                                            .title("Error")
                                            .content(responsemessage)

                                            .negativeText("Dismiss")
                                            .callback(new MaterialDialog.ButtonCallback()  {
                                                @Override
                                                public void onPositive(MaterialDialog dialog) {
                                                    dialog.dismiss();
                                                }
                                                @Override
                                                public void onNegative(MaterialDialog dialog) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                    txstatus.setText("TRANSACTION FAILURE");
                                    txdesc.setText(responsemessage);
                                    Bundle params = new Bundle();
                                    params.putString("transfer_error", responsemessage);
                                    params.putString("response_code", respcode);


                                    Answers.getInstance().logCustom(new CustomEvent("cash_deposit error code")

                                            .putCustomAttribute("deposit_error",responsemessage)
                                            .putCustomAttribute("response_code",respcode)
                                    );

                                /*    Toast.makeText(
                                            getApplicationContext(),
                                            "" + responsemessage,
                                            Toast.LENGTH_LONG).show();*/
                                }
                            } else {
                               /* getApplicationContext().finish();
                                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                Toast.makeText(
                                        getApplicationContext(),
                                        "You have been locked out of the app.Please call customer care for further details",
                                        Toast.LENGTH_LONG).show();*/

                               LogOut();
                            }
                        } else {

                            Toast.makeText(
                                    getApplicationContext(),
                                    "There was an error on your request",
                                    Toast.LENGTH_LONG).show();

                            txstatus.setText("TRANSACTION FAILURE");
                            txdesc.setText("There was an error on your request");
                        }
                    } else {

                        Toast.makeText(
                                getApplicationContext(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();

                        txstatus.setText("TRANSACTION FAILURE");
                        txdesc.setText("There was an error on your request");


                    }
                    // prgDialog2.dismiss();




                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                   SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());
                    // SecurityLayer.Log(e.toString());
                }
                if(!(prgDialog2 == null) && prgDialog2.isShowing() && getApplicationContext() != null) {
                    prgDialog2.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                SecurityLayer.Log("throwable error",t.toString());


                Toast.makeText(
                        getApplicationContext(),
                        "There was an error on your request",
                        Toast.LENGTH_LONG).show();


                if(!(prgDialog2 == null) && prgDialog2.isShowing() && getApplicationContext() != null) {
                    prgDialog2.dismiss();
                }
                SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void IntraDepoBankResp(String params) {
        prgDialog2.show();
        String endpoint = "transfer/intrabank.action";


        String usid = Utility.gettUtilUserId(getApplicationContext());
        String agentid = Utility.gettUtilAgentId(getApplicationContext());


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


                    SecurityLayer.Log("Intra Bank Resp", response.body());
                    SecurityLayer.Log("response..:", response.body());
                    JSONObject obj = new JSONObject(response.body());
                    //obj = Utility.onresp(obj,getApplicationContext());
                    obj = SecurityLayer.decryptTransaction(obj, getApplicationContext());
                    SecurityLayer.Log("decrypted_response", obj.toString());


                    JSONObject datas = obj.optJSONObject("data");
                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if (!(response.body() == null)) {
                        String respcode = obj.optString("responseCode");
                        String responsemessage = obj.optString("message");
                        String agcmsn = obj.optString("fee");
                        String refcodee = obj.optString("commission");
                        SecurityLayer.Log("Response Message", responsemessage);

                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if (!(Utility.checkUserLocked(respcode))) {
                                if (respcode.equals("00")) {
                                    String totfee = "0.00";
                                    String datetimee = "";
                                    if (!(datas == null)) {
                                        totfee = datas.optString("fee");
                                        datetimee = datas.optString("dateTime");
                                    }



                                    Intent intent  = new Intent(TransactionProcessingActivity.this,FinalConfDepoActivity.class);

                                    intent.putExtra("recanno",recanno);
                                    intent.putExtra("amou",amou);
                                    intent.putExtra("narra",narra);
                                    //    String refcodee = datas.optString("referenceCode");
                                    intent.putExtra("refcode",refcodee);
                                    intent.putExtra("ednamee",ednamee);
                                    intent.putExtra("ednumbb",ednumbb);
                                    intent.putExtra("txtname",txtname);
                                    intent.putExtra("datetime",datetimee);
                                    intent.putExtra("trantype","D");
                                    intent.putExtra("agcmsn",agcmsn);
                                    intent.putExtra("fee",totfee);
                                    startActivity(intent);

                                 /*   Bundle b = new Bundle();
                                    b.putString("recanno", recanno);
                                    b.putString("amou", amou);
                                    //      String refcodee = datas.optString("referenceCode");
                                    b.putString("refcode", refcodee);
                                    b.putString("narra", narra);
                                    b.putString("ednamee", ednamee);
                                    b.putString("ednumbb", ednumbb);
                                    b.putString("txtname", txtname);
                                    b.putString("agcmsn", agcmsn);
                                    b.putString("fee", totfee);
                                    b.putString("trantype", "D");
                                    Fragment fragment = new FinalConfDepoTrans();

                                    fragment.setArguments(b);
                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    //  String tag = Integer.toString(title);
                                    fragmentTransaction.replace(R.id.container_body, fragment, "Confirm Transfer");
                                    fragmentTransaction.addToBackStack("Confirm Transfer");
                                    ((FMobActivity) getApplicationContext())
                                            .setActionBarTitle("Confirm Transfer");
                                    fragmentTransaction.commit();*/
                                }

                                else if(respcode.equals("002")){
                                    Toast.makeText(
                                            getApplicationContext(), responsemessage,
                                            Toast.LENGTH_LONG).show();


                                    setAlertDialog();

                                    //   ((FMobActivity)getApplicationContext()).showWrongPinDialog(serv);
                                }else {
                                    new MaterialDialog.Builder(TransactionProcessingActivity.this)
                                            .title("Error")
                                            .content(responsemessage)

                                            .negativeText("Dismiss")
                                            .callback(new MaterialDialog.ButtonCallback() {
                                                @Override
                                                public void onPositive(MaterialDialog dialog) {
                                                    dialog.dismiss();
                                                }

                                                @Override
                                                public void onNegative(MaterialDialog dialog) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();

                                    txstatus.setText("TRANSACTION FAILURE");
                                    txdesc.setText(responsemessage);
                                   /* Bundle params = new Bundle();
                                    params.putString("deposit_error", responsemessage);
                                    params.putString("response_code", respcode);
                                    mFirebaseAnalytics.logEvent("cash_deposit", params);

                                    Answers.getInstance().logCustom(new CustomEvent("cash_deposit error code")

                                            .putCustomAttribute("deposit_error", responsemessage)
                                            .putCustomAttribute("response_code", respcode)
                                    );*/

                                 /*   Toast.makeText(
                                            getApplicationContext(),
                                            "" + responsemessage,
                                            Toast.LENGTH_LONG).show();*/
                                }
                            } else {
                              /*  getApplicationContext().finish();
                                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                Toast.makeText(
                                        getApplicationContext(),
                                        "You have been locked out of the app.Please call customer care for further details",
                                        Toast.LENGTH_LONG).show();*/

                               LogOut();
                            }
                        } else {

                            Toast.makeText(
                                    getApplicationContext(),
                                    "There was an error on your request",
                                    Toast.LENGTH_LONG).show();
                            txstatus.setText("TRANSACTION FAILURE");
                            txdesc.setText("There was an error on your request");

                        }
                    } else {

                        Toast.makeText(
                                getApplicationContext(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();


                    }
                    // prgDialog2.dismiss();


                }

                catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
              //      ((FMobActivity) getApplicationContext()).SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());
                    // SecurityLayer.Log(e.toString());
                }
                if ((!(getApplicationContext() == null)) && !(prgDialog2 == null) && prgDialog2.isShowing()) {
                    prgDialog2.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                // Log error here since request failed

                if (t instanceof SocketTimeoutException) {
                    //   message = "Socket Time out. Please try again.";

                    setDialog("Your request has been received.Please wait shortly for feedback");
                }
                SecurityLayer.Log("throwable error", t.toString());


                Toast.makeText(
                        getApplicationContext(),
                        "There was an error on your request",
                        Toast.LENGTH_LONG).show();


                if ((!(getApplicationContext() == null)) && !(prgDialog2 == null) && prgDialog2.isShowing()) {
                    prgDialog2.dismiss();
                }
         //       ((FMobActivity) getApplicationContext()).SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());
            }
        });
    }


    public void setDialog(String message){
        new MaterialDialog.Builder(TransactionProcessingActivity.this)
                .title("Error")
                .content(message)

                .negativeText("Dismiss")
                .callback(new MaterialDialog.ButtonCallback()  {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), FMobActivity.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), FMobActivity.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .show();
    }
    public void setBillDialog(String message){
        new MaterialDialog.Builder(TransactionProcessingActivity.this)
                .title("Notice")
                .content(message)

                .negativeText("Dismiss")
                .callback(new MaterialDialog.ButtonCallback()  {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), FMobActivity.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), FMobActivity.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .show();
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button2) {
            finish();
            Intent intent = new Intent(getApplicationContext(), FMobActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }



    public void setAlertDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.signindialogfrag, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText mEditTexta = (EditText) alertLayout.findViewById(R.id.txt_your_name);

        pro = new ProgressDialog(getApplicationContext());
        pro.setMessage("Loading...");
        pro.setTitle("");
        pro.setCancelable(false);

        mPinLockView = (PinLockView) alertLayout.findViewById(R.id.pin_lock_view);
        mPinLockView.setPinLockListener(mPinLockListener);
        mIndicatorDots = (IndicatorDots) alertLayout.findViewById(R.id.indicator_dots);

        mPinLockView.attachIndicatorDots(mIndicatorDots);


        mPinLockView.setPinLength(5);
        mPinLockView.setTextColor(getResources().getColor(R.color.fab_material_blue_grey_900));
        alert.setTitle("Incorrect PIN set.Please enter valid PIN");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent intent = new Intent(getApplicationContext(), FMobActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        alert.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                if(Utility.isNotNull(finpin)) {
                    String encrypted = Utility.getencpin(finpin);
                    SecurityLayer.Log("Enc Pin",encrypted);


                    String finalparams = newparams+"/"+encrypted;
                    dialog.dismiss();
                    if(serv.equals("CASHDEPO")) {

                        IntraDepoBankResp(finalparams);

                    }
                    if(serv.equals("CASHTRAN")) {

                        IntraTranBankResp(finalparams);

                    }
                    if(serv.equals("WDRAW")) {


                        WithdrawResp(finalparams);

                    }
                    if(serv.equals("AIRT")) {


                        AirtimeResp(finalparams);

                    }
                    if(serv.equals("SENDOTB")) {

                        InterBankResp(finalparams);

                    }

                    if(serv.equals("CABLETV")) {


                        PayBillResp(finalparams);

                    }


                }else{
                    dialog.dismiss();
                    Toast.makeText(
                            getApplicationContext(),
                            "Please enter a valid value for Attendant PIN",
                            Toast.LENGTH_LONG).show();
setAlertDialog();

                }
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }




    private void InterBankResp(String params) {
        prgDialog2.show();
        String endpoint= "transfer/interbank.action";


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


                    SecurityLayer.Log("Inter Bank Resp", response.body());
                    SecurityLayer.Log("response..:", response.body());
                    JSONObject obj = new JSONObject(response.body());
                    //obj = Utility.onresp(obj,getApplicationContext());
                    obj = SecurityLayer.decryptTransaction(obj, getApplicationContext());
                    SecurityLayer.Log("decrypted_response", obj.toString());




                    JSONObject datas = obj.optJSONObject("data");
                    //session.setString(SecurityLayer.KEY_APP_ID,appid);
                    if(!(response.body() == null)) {
                        String respcode = obj.optString("responseCode");
                        String responsemessage = obj.optString("message");
                        String agcmsn = obj.optString("fee");
                        String refcodee = obj.optString("commission");
                        SecurityLayer.Log("Response Message", responsemessage);


                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if (!(Utility.checkUserLocked(respcode))) {
                                if (respcode.equals("00")) {
                                    String totfee = "0.00";
                                    String datetimee = "";
                                    if(!(datas == null)){
                                        totfee = datas.optString("fee");
                                        datetimee = datas.optString("dateTime");
                                    }



                                    Intent intent  = new Intent(TransactionProcessingActivity.this,FinalConfSendOTBActivity.class);


                                    intent.putExtra("recanno", recanno);
                                    intent.putExtra("amou", amou);
                                    intent.putExtra("narra", narra);
                                    intent.putExtra("ednamee", ednamee);
                                    intent.putExtra("ednumbb", ednumbb);
                                    intent.putExtra("txtname", txtname);
                                    intent.putExtra("datetime", datetimee);
                                    intent.putExtra("bankname", bankname);
                                    intent.putExtra("bankcode", bankcode);
                                    //     String refcodee = datas.optString("referenceCode");
                                    intent.putExtra("refcode",refcodee);
                                    intent.putExtra("agcmsn",agcmsn);
                                    intent.putExtra("fee",totfee);
                                  startActivity(intent);
                                }   else if(respcode.equals("002")){
                                    Toast.makeText(
                                            getApplicationContext(), responsemessage,
                                            Toast.LENGTH_LONG).show();


                                    setAlertDialog();
                                }else {
                                    setDialog(responsemessage);
                                    txstatus.setText("TRANSACTION FAILURE");
                                    txdesc.setText(responsemessage);

                                    Answers.getInstance().logCustom(new CustomEvent("interbank error code")

                                            .putCustomAttribute("error_msg",responsemessage)
                                            .putCustomAttribute("response_code",respcode)
                                    );
                                }
                            } else {
                                /*getApplicationContext().finish();
                                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                Toast.makeText(
                                        getApplicationContext(),
                                        "You have been locked out of the app.Please call customer care for further details",
                                        Toast.LENGTH_LONG).show();*/
                               LogOut();
                            }
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "There was an error on your request",
                                    Toast.LENGTH_LONG).show();
                            txstatus.setText("TRANSACTION FAILURE");
                            txdesc.setText("There was an error on your request");

                        }
                    }else{
                        Toast.makeText(
                                getApplicationContext(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();
                        txstatus.setText("TRANSACTION FAILURE");
                        txdesc.setText("There was an error on your request");
                    }

                    // prgDialog2.dismiss();




                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                   SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());
                    // SecurityLayer.Log(e.toString());
                }
                if((!(getApplicationContext() == null)) && !(prgDialog2 == null) && prgDialog2.isShowing()) {
                    prgDialog2.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                SecurityLayer.Log("throwable error",t.toString());


                Toast.makeText(
                        getApplicationContext(),
                        "There was an error on your request",
                        Toast.LENGTH_LONG).show();


                if((!(getApplicationContext() == null)) && !(prgDialog2 == null) && prgDialog2.isShowing()) {
                    prgDialog2.dismiss();
                }
               SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());
            }
        });

    }





    private void WithdrawResp(String params) {
        prgDialog2.show();
        String endpoint= "withdrawal/cashbyaccountconfirm.action";


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


                    SecurityLayer.Log("Intra Bank Resp", response.body());
                    SecurityLayer.Log("response..:", response.body());
                    JSONObject obj = new JSONObject(response.body());
                    //obj = Utility.onresp(obj,getApplicationContext());
                    obj = SecurityLayer.decryptTransaction(obj, getApplicationContext());
                    SecurityLayer.Log("decrypted_response", obj.toString());




                    JSONObject datas = obj.optJSONObject("data");
                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if(!(response.body() == null)) {
                        String respcode = obj.optString("responseCode");
                        String responsemessage = obj.optString("message");
                        String agcmsn = obj.optString("fee");
                        String refcodee = obj.optString("commission");
                        SecurityLayer.Log("Response Message", responsemessage);

                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if (!(Utility.checkUserLocked(respcode))) {
                                SecurityLayer.Log("Response Message", responsemessage);
                               /* Toast.makeText(
                                        getApplicationContext(),
                                        "" + responsemessage,
                                        Toast.LENGTH_LONG).show();*/
                                if (respcode.equals("00")) {
                                    String totfee = "0.00";
                                    String datetimee = "";
                                    if (!(datas == null)) {
                                        totfee = datas.optString("fee");
                                        datetimee = datas.optString("dateTime");
                                    }
                                    Bundle b = new Bundle();
                                    Intent intent  = new Intent(TransactionProcessingActivity.this,FinalConfWithdrawActivity.class);


                                    intent.putExtra("recanno", recanno);

                                    intent.putExtra("amou", amou);

                                    intent.putExtra("datetime", datetimee);
                                    intent.putExtra("txtname", txtname);
                                    intent.putExtra("txref", txref);
                                    intent.putExtra("agcmsn", agcmsn);
                                    //    String refcodee = datas.optString("referenceCode");
                                    intent.putExtra("refcode",refcodee);
                                    intent.putExtra("fee", totfee);


                                    startActivity(intent);


                                    /*Fragment fragment = new FinalConfWithdraw();

                                    fragment.setArguments(b);
                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    //  String tag = Integer.toString(title);
                                    fragmentTransaction.replace(R.id.container_body, fragment, "Final Confirm Withdrawal");
                                    fragmentTransaction.addToBackStack("Final Confirm Withdrawal");
                                    ((FMobActivity) getApplicationContext())
                                            .setActionBarTitle("Final Confirm Withdrawal");
                                    fragmentTransaction.commit();*/
                                }

                                else if(respcode.equals("002")){
                                    Toast.makeText(
                                            getApplicationContext(), responsemessage,
                                            Toast.LENGTH_LONG).show();



                                    setAlertDialog();
                                }else{
                                    setDialog(responsemessage);
                                    txstatus.setText("TRANSACTION FAILURE");
                                    txdesc.setText(responsemessage);

                                    Answers.getInstance().logCustom(new CustomEvent("withdrawal error code")

                                            .putCustomAttribute("error_msg",responsemessage)
                                            .putCustomAttribute("response_code",respcode)
                                    );
                                }
                            } else {
                               /* getApplicationContext().finish();
                                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                Toast.makeText(
                                        getApplicationContext(),
                                        "You have been locked out of the app.Please call customer care for further details",
                                        Toast.LENGTH_LONG).show();*/

                               LogOut();

                            }
                        } else {

                            Toast.makeText(
                                    getApplicationContext(),
                                    "There was an error on your request",
                                    Toast.LENGTH_LONG).show();
                            txstatus.setText("TRANSACTION FAILURE");
                            txdesc.setText("There was an error on your request");


                        }
                    } else {

                        Toast.makeText(
                                getApplicationContext(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();
                        txstatus.setText("TRANSACTION FAILURE");
                        txdesc.setText("There was an error on your request");


                    }
                    // prgDialog2.dismiss();




                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                   SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());
                    // SecurityLayer.Log(e.toString());
                }
                if ((prgDialog2 != null) && prgDialog2.isShowing() && !(getApplicationContext() == null)) {
                    prgDialog2.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                // Log error here since request failed
                SecurityLayer.Log("throwable error",t.toString());


                Toast.makeText(
                        getApplicationContext(),
                        "There was an error on your request",
                        Toast.LENGTH_LONG).show();


                if ((prgDialog2 != null) && prgDialog2.isShowing() && !(getApplicationContext() == null)) {
                    prgDialog2.dismiss();
                }
                SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());
            }
        });

    }



    public void SetForceOutDialog(String msg, final String title, final Context c) {
        if (!(c == null)) {
            new MaterialDialog.Builder(TransactionProcessingActivity.this)
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



    private void AirtimeResp(String params) {

        prgDialog2.show();

        String endpoint = "billpayment/mobileRecharge.action";


        String usid = Utility.gettUtilUserId(getApplicationContext());
        String agentid = Utility.gettUtilAgentId(getApplicationContext());



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


                    SecurityLayer.Log("Intra Bank Resp", response.body());
                    SecurityLayer.Log("response..:", response.body());
                    JSONObject obj = new JSONObject(response.body());
                    //obj = Utility.onresp(obj,getApplicationContext());
                    obj = SecurityLayer.decryptTransaction(obj, getApplicationContext());
                    SecurityLayer.Log("decrypted_response", obj.toString());


                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if (!(response.body() == null)) {
                        String respcode = obj.optString("responseCode");
                        String responsemessage = obj.optString("message");
                        String agcmsn = obj.optString("fee");
                        String refcodee = obj.optString("commission");
                        JSONObject datas = obj.optJSONObject("data");
                        SecurityLayer.Log("Response Message", responsemessage);

                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if (!(Utility.checkUserLocked(respcode))) {
                                SecurityLayer.Log("Response Message", responsemessage);
                               /* Toast.makeText(
                                        getApplicationContext(),
                                        "" + responsemessage,
                                        Toast.LENGTH_LONG).show();*/
                                if (respcode.equals("00")) {

//                                    SecurityLayer.Log("Respnse getResults",datas.toString());
                                    if (!(datas == null)) {

                                        String totfee = "0.00";
String datetimee = "";
                                        String ttf = datas.optString("fee");
                                        datetimee = datas.optString("dateTime");
                                        if (ttf == null || ttf.equals("")) {

                                        } else {
                                            totfee = ttf;
                                        }

                                        String tref = datas.optString("refNumber");

                                        Intent intent  = new Intent(TransactionProcessingActivity.this,FinalConfAirtimeActivity.class);




                                        intent.putExtra("mobno", txtcustid);
                                        intent.putExtra("amou", amou);
                                        intent.putExtra("telcoop", telcoop);
                                        //  String refcodee = datas.optString("refNumber");
                                        intent.putExtra("refcode", refcodee);
                                        intent.putExtra("billid", billid);
                                        intent.putExtra("datetime", datetimee);
                                        intent.putExtra("serviceid", serviceid);
                                        intent.putExtra("agcmsn", agcmsn);
                                        intent.putExtra("fee", totfee);
                                        intent.putExtra("tref", tref);


                                        //    String refcodee = datas.optString("referenceCode");
                                        intent.putExtra("refcode",refcodee);
                                        intent.putExtra("fee", totfee);


                                        startActivity(intent);
                                       /* Bundle b = new Bundle();
                                        b.putString("mobno", txtcustid);
                                        b.putString("amou", amou);
                                        b.putString("telcoop", telcoop);
                                        //  String refcodee = datas.optString("refNumber");
                                        b.putString("refcode", refcodee);
                                        b.putString("billid", billid);
                                        b.putString("serviceid", serviceid);
                                        b.putString("agcmsn", agcmsn);
                                        b.putString("fee", totfee);
                                        b.putString("tref", tref);
                                        Fragment fragment = new FinalConfAirtime();

                                        fragment.setArguments(b);
                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        //  String tag = Integer.toString(title);
                                        fragmentTransaction.replace(R.id.container_body, fragment, "Final Conf Airtime");
                                        fragmentTransaction.addToBackStack("Final Conf");
                                        ((FMobActivity) getApplicationContext())
                                                .setActionBarTitle("Final Conf Airtime");
                                        fragmentTransaction.commit();*/
                                    }
                                }    else if(respcode.equals("002")){
                                    Toast.makeText(
                                            getApplicationContext(), responsemessage,
                                            Toast.LENGTH_LONG).show();

                                    setAlertDialog();
                                } else {
                                    setDialog(responsemessage);
                                    txstatus.setText("TRANSACTION FAILURE");
                                    txdesc.setText(responsemessage);
                                }
                            } else {
                               /* getApplicationContext().finish();
                                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                Toast.makeText(
                                        getApplicationContext(),
                                        "You have been locked out of the app.Please call customer care for further details",
                                        Toast.LENGTH_LONG).show();*/
                               LogOut();
                            }
                        } else {

                            Toast.makeText(
                                    getApplicationContext(),
                                    "There was an error on your request",
                                    Toast.LENGTH_LONG).show();

                            txstatus.setText("TRANSACTION FAILURE");
                            txdesc.setText("There was an error on your request");
                        }
                    } else {

                        Toast.makeText(
                                getApplicationContext(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();
                        txstatus.setText("TRANSACTION FAILURE");
                        txdesc.setText("There was an error on your request");

                    }
                    // prgDialog2.dismiss();


                } catch (JSONException e) {

                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    txstatus.setText("TRANSACTION FAILURE");
                    txdesc.setText("There was an error on your request");
                    SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {

                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    txstatus.setText("TRANSACTION FAILURE");
                    txdesc.setText("There was an error on your request");
                   SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());
                    // SecurityLayer.Log(e.toString());
                }
                if ((prgDialog2 != null) && prgDialog2.isShowing()) {
                    prgDialog2.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                // Log error here since request failed
                SecurityLayer.Log("throwable error", t.toString());

                if (t instanceof SocketTimeoutException) {

                    SecurityLayer.Log("socket timeout error", t.toString());
                    txstatus.setText("TRANSACTION FAILURE");
                    txdesc.setText("There was an error on your request");
                }
                Toast.makeText(
                        getApplicationContext(),
                        "There was an error on your request",
                        Toast.LENGTH_LONG).show();

                txstatus.setText("TRANSACTION FAILURE");
                txdesc.setText("There was an error on your request");
                if ((prgDialog2 != null) && prgDialog2.isShowing()) {
                    prgDialog2.dismiss();
                }
                SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());
            }
        });

    }




    private void PayBillResp(String params) {
        prgDialog2.show();
        String endpoint = "billpayment/dobillpayment.action";


        String usid = Utility.gettUtilUserId(getApplicationContext());
        String agentid = Utility.gettUtilAgentId(getApplicationContext());


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


                    SecurityLayer.Log("Cable TV Resp", response.body());
                    SecurityLayer.Log("response..:", response.body());
                    JSONObject obj = new JSONObject(response.body());
                    //obj = Utility.onresp(obj,getApplicationContext());
                    obj = SecurityLayer.decryptTransaction(obj, getApplicationContext());
                    SecurityLayer.Log("decrypted_response", obj.toString());


                    JSONObject datas = obj.optJSONObject("data");
                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if (!(response.body() == null)) {
                        String respcode = obj.optString("responseCode");
                        String responsemessage = obj.optString("message");
                        String agcmsn = obj.optString("fee");
                        String refcodee = obj.optString("commission");
                        SecurityLayer.Log("Response Message", responsemessage);

                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if (!(Utility.checkUserLocked(respcode))) {
                                SecurityLayer.Log("Response Message", responsemessage);

                                if (respcode.equals("00")) {

//                                    SecurityLayer.Log("Respnse getResults",datas.toString());
                                    String totfee = "0.00";
                                    String tref = "N/A";
                                    if (!(datas == null)) {

                                        String datetimee = "";
                                        String ttf = datas.optString("fee");
                                        datetimee = datas.optString("dateTime");
                                        if (ttf == null || ttf.equals("")) {

                                        } else {
                                            totfee = ttf;
                                        }


                                        tref = datas.optString("refNumber");
                                    /*Bundle b = new Bundle();
                                    b.putString("custid", txtcustid);
                                    b.putString("amou", amou);
                                    b.putString("narra", narra);
                                    b.putString("billname", strbillnm);
                                    b.putString("ednamee", ednamee);
                                    b.putString("ednumbb", ednumbb);
                                    b.putString("billid", billid);
                                    b.putString("serviceid", serviceid);
                                    b.putString("label", strlabel);
                                    b.putString("fullname", fullname);
                                    //  String refcodee = datas.optString("refNumber");
                                    b.putString("refcode", refcodee);
                                    b.putString("tref", tref);
                                    b.putString("agcmsn", agcmsn);
                                    b.putString("fee", totfee);
                                    Fragment fragment = new FinalConfirmCableTV();

                                    fragment.setArguments(b);
                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    //  String tag = Integer.toString(title);
                                    fragmentTransaction.replace(R.id.container_body, fragment, "Final Conf Cable");
                                    fragmentTransaction.addToBackStack("Final Conf Cable");
                                    ((FMobActivity) getApplicationContext())
                                            .setActionBarTitle("Final Conf Cable");
                                    fragmentTransaction.commit();*/

                                        Intent intent = new Intent(TransactionProcessingActivity.this, FinalConfirmCableTVActivity.class);


                                        intent.putExtra("custid", txtcustid);
                                        intent.putExtra("amou", amou);
                                        intent.putExtra("narra", narra);
                                        intent.putExtra("billname", strbillnm);
                                        intent.putExtra("ednamee", ednamee);
                                        intent.putExtra("ednumbb", ednumbb);
                                        intent.putExtra("billid", billid);
                                        intent.putExtra("serviceid", serviceid);
                                        intent.putExtra("label", strlabel);
                                        intent.putExtra("fullname", fullname);
                                        intent.putExtra("datetime", datetimee);
                                        //  String refcodee = datas.optString("refNumber");
                                        intent.putExtra("refcode", refcodee);
                                        intent.putExtra("tref", tref);
                                        intent.putExtra("agcmsn", agcmsn);
                                        intent.putExtra("fee", strfee);


                                        startActivity(intent);
                                    }
                                }   else if(respcode.equals("002")){
                                    Toast.makeText(
                                            getApplicationContext(), responsemessage,
                                            Toast.LENGTH_LONG).show();

                                    setAlertDialog();
                                }else {
                                    if(Utility.checkStateCollect(serviceid)){
                                        setBillDialog(responsemessage);
                                    }else{
                                        setDialog(responsemessage);
                                    }

                                    txstatus.setText("TRANSACTION FAILURE");
                                    txdesc.setText(responsemessage);


                                    Answers.getInstance().logCustom(new CustomEvent("paybill error code")

                                            .putCustomAttribute("error_msg",responsemessage)
                                            .putCustomAttribute("response_code",respcode)
                                    );
                                }
                            } else {
                                /*getApplicationContext().finish();
                                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                Toast.makeText(
                                        getApplicationContext(),
                                        "You have been locked out of the app.Please call customer care for further details",
                                        Toast.LENGTH_LONG).show();*/

                              LogOut();
                            }
                        } else {

                            Toast.makeText(
                                    getApplicationContext(),
                                    "There was an error on your request",
                                    Toast.LENGTH_LONG).show();
                            txstatus.setText("TRANSACTION FAILURE");
                            txdesc.setText("There was an error on your request");


                        }
                    } else {

                        Toast.makeText(
                                getApplicationContext(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();
                        txstatus.setText("TRANSACTION FAILURE");
                        txdesc.setText("There was an error on your request");


                    }
                    // prgDialog2.dismiss();


                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());

                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    // SecurityLayer.Log(e.toString());

                    txstatus.setText("TRANSACTION FAILURE");
                    txdesc.setText("There was an error on your request.Please retry");
                    SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());

                } catch (Exception e) {

                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());
                    // SecurityLayer.Log(e.toString());
                }
                if ((prgDialog2 != null) && prgDialog2.isShowing() && !(getApplicationContext() == null)) {
                    prgDialog2.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                // Log error here since request failed
                SecurityLayer.Log("throwable error", t.toString());
                if (t instanceof SocketTimeoutException) {

                    SecurityLayer.Log("socket timeout error", t.toString());
                }


                Toast.makeText(
                        getApplicationContext(),
                        "There was an error on your request",
                        Toast.LENGTH_LONG).show();

                if ((prgDialog2 != null) && prgDialog2.isShowing() && !(getApplicationContext() == null)) {
                    prgDialog2.dismiss();
                }
               SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());
            }
        });

    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        if(prgDialog2!=null && prgDialog2.isShowing()){

            prgDialog2.dismiss();
        }
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
