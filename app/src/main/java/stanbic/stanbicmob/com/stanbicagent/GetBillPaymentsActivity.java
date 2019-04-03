package stanbic.stanbicmob.com.stanbicagent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.BillMenuParcelable;
import adapter.BillerPayMenuAdapt;
import model.GetBillPayData;
import rest.ApiClient;
import rest.ApiInterface;
import rest.ApiSecurityClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import security.SecurityLayer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GetBillPaymentsActivity extends BaseActivity implements View.OnClickListener {
    GridView gridView;
    List<GetBillPayData> planetsList = new ArrayList<GetBillPayData>();
    String ptype;
    public static final String KEY_TOKEN = "token";
    ListView lv;
    TextView txtservice,step2;
    String serviceid,servicename,servlabel,billid,billname,label,idd,acnumber;
    BillerPayMenuAdapt aAdpt;
    ProgressDialog prgDialog;
    SessionManagement session;
    String sbpam = "0", pramo = "0";
    boolean blsbp = false, blpr = false, blpf = false, bllr = false, blms = false, blmpesa = false, blcash = false;
    ArrayList<String> ds = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_bill_payments);


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

        prgDialog = new ProgressDialog(this);

        prgDialog.setMessage("Please wait...");


        gridView = (GridView) findViewById(R.id.gridView1);
        txtservice = (TextView) findViewById(R.id.textView1);

        Intent intent = getIntent();
        if (intent != null) {
            BillMenuParcelable bcp = intent.getParcelableExtra("bcp");
            /*serviceid = intent.getStringExtra("serviceid");
            servicename = intent.getStringExtra("servicename");
            billid = intent.getStringExtra("billid");
            billname = intent.getStringExtra("billname");
            label = intent.getStringExtra("label");
            acnumber = intent.getStringExtra("acnumber");
            idd = intent.getStringExtra("idd");*/

            serviceid = bcp.getserviceid();
            servicename = bcp.getservicename();
            billid = bcp.getbillid();
            billname = bcp.getbillname();
            label = bcp.getservlabel();
            acnumber = bcp.getstracnumber();
            idd = bcp.getidd();
            txtservice.setText(billname);
        }

        prgDialog.setCancelable(false);

        step2 = (TextView) findViewById(R.id.tv2);
        step2.setOnClickListener(this);

        //checkInternetConnection2();
        lv = (ListView) findViewById(R.id.lv);
        String bsid = session.getString("getbillpay"+idd);
        if(bsid == null) {
            SetPop();
        }
        else{
            SetBillersStored();
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Fragment fragment = null;
                String title = null;
                String billid = planetsList.get(position).getBillerId();
                //    String billname = planetsList.get(position).getdisplayName();
                String packid = planetsList.get(position).getpackid();
                String paycode = planetsList.get(position).getpaymentCode();
                String charge = planetsList.get(position).getCharge();
                String dispname = planetsList.get(position).getdisplayName();


                Intent intent  = null;
                Log.v("Idd",idd);

                if(serviceid.equals("3")){
                    intent =  new Intent(GetBillPaymentsActivity.this,StateCollectActivity.class);
                }else{
                    intent =  new Intent(GetBillPaymentsActivity.this,CableTVActivity.class);
                }
                /*intent.putExtra("serviceid",serviceid);

                intent.putExtra("servicename",servicename);
                intent.putExtra("billid",billid);
                intent.putExtra("billname",billname);
                intent.putExtra("dispname",dispname);
                intent.putExtra("label",label);
                intent.putExtra("packid",packid);
                intent.putExtra("charge",charge);
                intent.putExtra("paymentCode",paycode);*/


                BillMenuParcelable bcp = new BillMenuParcelable(serviceid,servicename,billid,billname,idd,label,null,paycode,packid,charge,dispname,null,null,null,null,null);
                intent.putExtra("bcp",bcp);
                
                
                startActivity(intent);

         /*       if (fragment != null) {
                    fragment.setArguments(b);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    //  String tag = Integer.toString(title);
                    fragmentTransaction.replace(R.id.container_body, fragment, title);
                    fragmentTransaction.addToBackStack(title);
                    ((FMobActivity)getApplicationContext())
                            .setActionBarTitle(title);
                    fragmentTransaction.commit();
                }
*/

            }
        });
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void SetPop(){
        planetsList.clear();



        prgDialog.show();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String usid = Utility.gettUtilUserId(getApplicationContext());
        String agentid = Utility.gettUtilAgentId(getApplicationContext());
        String mobnoo = Utility.gettUtilMobno(getApplicationContext());


        String params = "1/"+usid+"/"+agentid+"/"+mobnoo+"/"+idd;

        GetServv(params);
      /*  Call<GetBillers> call = apiService.getBillers("1",usid,agentid,"0000",serviceid);
        call.enqueue(new Callback<GetBillers>() {
            @Override
            public void onResponse(Call<GetBillers>call, Response<GetBillers> response) {
                String responsemessage = response.body().getMessage();

                SecurityLayer.Log("Response Message",responsemessage);
                planetsList = response.body().getResults();

                if(!(planetsList == null)) {
                    if(planetsList.size() > 0) {
                        SecurityLayer.Log("Get Biller Data Name", planetsList.get(0).getBillerName());
                        Collections.sort(planetsList, new Comparator<GetBillersData>() {
                            public int compare(GetBillersData d1, GetBillersData d2) {
                                return d1.getBillerName().compareTo(d2.getBillerName());
                            }
                        });
                        aAdpt = new BillerMenuAdapt(planetsList, getApplicationContext());
                        lv.setAdapter(aAdpt);
                    }else{
                        Toast.makeText(
                                getApplicationContext(),
                                "No billers available  ",
                                Toast.LENGTH_LONG).show();
                    }
                }
                prgDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetBillers>call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(
                        getApplicationContext(),
                        "Throwable Error: "+t.toString(),
                        Toast.LENGTH_LONG).show();
                prgDialog.dismiss();
            }
        });*/
    }




    @Override
    public void onClick(View view) {


        if (view.getId() == R.id.tv2) {
           /* Fragment  fragment = new BillMenu();


            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,"Biller Menu");
            fragmentTransaction.addToBackStack("Biller Menu");
            ((FMobActivity)getApplicationContext())
                    .setActionBarTitle("Biller Menu");
            fragmentTransaction.commit();*/


            finish();
            Intent intent  = new Intent(GetBillPaymentsActivity.this,BillMenuActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
    }


    private void GetServv(String params) {

        String endpoint= "billpayment/billpaymentitems.action";


        String usid = Utility.gettUtilUserId(getApplicationContext());
        String agentid = Utility.gettUtilAgentId(getApplicationContext());


        SecurityLayer.Log("Before Req Tok",session.getString(KEY_TOKEN));

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


                    SecurityLayer.Log("Cable TV Resp", response.body());
                    SecurityLayer.Log("response..:", response.body());
                    JSONObject obj = new JSONObject(response.body());
                    //obj = Utility.onresp(obj,getApplicationContext());
                    obj = SecurityLayer.decryptTransaction(obj, getApplicationContext());
                    SecurityLayer.Log("decrypted_response", obj.toString());





                    JSONArray servdata = obj.optJSONArray("data");
                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if(!(response.body() == null)) {
                        String respcode = obj.optString("responseCode");
                        String responsemessage = obj.optString("message");

                        SecurityLayer.Log("Response Message", responsemessage);

                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if (!(Utility.checkUserLocked(respcode))) {
                                SecurityLayer.Log("Response Message", responsemessage);

                                if (respcode.equals("00")){
                                    SecurityLayer.Log("JSON Aray", servdata.toString());
                                    if(servdata.length() > 0){


                                        JSONObject json_data = null;
                                        for (int i = 0; i < servdata.length(); i++) {
                                            json_data = servdata.getJSONObject(i);
                                            //String accid = json_data.getString("benacid");


                                            String id = json_data.optString("packId");

                                            String billerId = json_data.optString("billerId");

                                            String displayName = json_data.optString("displayName");
                                            String charge = json_data.optString("charge");
                                            charge = Double.toString(Double.parseDouble(charge));
                                            String paymentCode = json_data.optString("paymentCode");



                                            planetsList.add( new GetBillPayData(id,displayName,billerId,paymentCode,charge));




                                        }
                                        session.setString("getbillpay"+idd,servdata.toString());
                                        if(!(getApplicationContext() == null)) {
                                            aAdpt = new BillerPayMenuAdapt(planetsList, GetBillPaymentsActivity.this);


                                            lv.setAdapter(aAdpt);
                                        }


                                    }else{
                                     /*   Toast.makeText(
                                                getApplicationContext(),
                                                "No billers available  ",
                                                Toast.LENGTH_LONG).show();*/
                                        goNextPage();
                                    }

                                }else{
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "" + responsemessage,
                                            Toast.LENGTH_LONG).show();
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


                        }
                    } else {

                        Toast.makeText(
                                getApplicationContext(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();


                    }
                    // prgDialog2.dismiss();




                } catch (JSONException e) {
                    Utility.errornexttoken();
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    if(!(getApplicationContext() == null)) {
                        Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                        SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());
                        // SecurityLayer.Log(e.toString());
                    }

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    Utility.errornexttoken();
                    if(!(getApplicationContext() == null)) {
                        SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());
                    }
                    // SecurityLayer.Log(e.toString());
                }
                SecurityLayer.Log("After Req Tok",session.getString(KEY_TOKEN));
                prgDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                // Log error here since request failed
                SecurityLayer.Log("throwable error",t.toString());

                if(!(getApplicationContext() == null)) {
                    Toast.makeText(
                            getApplicationContext(),
                            "There was an error on your request",
                            Toast.LENGTH_LONG).show();
                   SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getApplicationContext());

                }

                Utility.errornexttoken();
                SecurityLayer.Log("After Req Tok",session.getString(KEY_TOKEN));
                prgDialog.dismiss();
            }
        });

    }

    public void SetBillersStored(){
        planetsList.clear();
        try{
            String bsid = session.getString("getbillpay"+idd);
            JSONArray servdata = new JSONArray(bsid);
            if(servdata.length() > 0){


                JSONObject json_data = null;
                for (int i = 0; i < servdata.length(); i++) {
                    json_data = servdata.getJSONObject(i);
                    //String accid = json_data.getString("benacid");


                    String id = json_data.optString("packId");

                    String billerId = json_data.optString("billerId");

                    String displayName = json_data.optString("displayName");
                    String charge = json_data.optString("charge");
                    charge = Double.toString(Double.parseDouble(charge));
                    String paymentCode = json_data.optString("paymentCode");



                    planetsList.add( new GetBillPayData(id,displayName,billerId,paymentCode,charge));




                }

                if(!(getApplicationContext() == null)) {
                    aAdpt = new BillerPayMenuAdapt(planetsList, GetBillPaymentsActivity.this);


                    lv.setAdapter(aAdpt);
                }


            }else{
                                     /*   Toast.makeText(
                                                getApplicationContext(),
                                                "No billers available  ",
                                                Toast.LENGTH_LONG).show();*/
                goNextPage();
            }
        } catch (JSONException e) {
            SecurityLayer.Log("encryptionJSONException", e.toString());
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
            // SecurityLayer.Log(e.toString());

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void goNextPage(){


        Intent intent  = null;

        if(serviceid.equals("3")){
            intent =  new Intent(GetBillPaymentsActivity.this,StateCollectActivity.class);
        }else{
            intent =  new Intent(GetBillPaymentsActivity.this,CableTVActivity.class);
        }
        BillMenuParcelable bcp = new BillMenuParcelable(serviceid,servicename,billid,billname,idd,label,null,acnumber,"0","N",null,null,null,null,null,null);
        intent.putExtra("bcp",bcp);


        startActivity(intent);
       /* intent.putExtra("serviceid",serviceid);

        intent.putExtra("servicename",servicename);
        intent.putExtra("billid",billid);
        intent.putExtra("billname",billname);
        intent.putExtra("label",label);
        intent.putExtra("packid","0");
        intent.putExtra("charge","N");
        intent.putExtra("paymentCode",acnumber);


        startActivity(intent);*/
    /*    Bundle b  = new Bundle();
        b.putString("serviceid",serviceid);
        b.putString("servicename",servicename);
        b.putString("billid",billid);
        b.putString("billname",billname);
        b.putString("label",label);
        b.putString("packid","0");
        b.putString("charge","N");
        b.putString("paymentCode",acnumber);
        Fragment  fragment = new CableTV();
        String  title = "Cable";

        if (fragment != null) {
            fragment.setArguments(b);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment, title);
            fragmentTransaction.addToBackStack(title);
            ((FMobActivity)getApplicationContext())
                    .setActionBarTitle(title);
            fragmentTransaction.commit();
        }*/


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


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub


        if(prgDialog!=null && prgDialog.isShowing()){

            prgDialog.dismiss();
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
