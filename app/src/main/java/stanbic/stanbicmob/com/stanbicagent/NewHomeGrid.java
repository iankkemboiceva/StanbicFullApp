package stanbic.stanbicmob.com.stanbicagent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import rest.ApiInterface;
import rest.ApiSecurityClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import security.SecurityLayer;


public class NewHomeGrid extends Fragment implements View.OnClickListener {
    ImageView imageView1;
    TextView txttogrid;
    RelativeLayout rldepo,rlwithdrw,rltransf,rlpybill,rlairtime,rlopenacc,rlagac,rlcommac,rlopenaccinside;
    Button rlagbal,rlcomm;
    SessionManagement session;
    ProgressDialog pro ;
    Toolbar mToolbar;
    public NewHomeGrid() {
        // Required empty public constructor
    }
  /*  private static Fragment newInstance(Context context) {
        LayoutOne f = new LayoutOne();

        return f;
    }
*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.newvershomegrid, null);
        session = new SessionManagement(getActivity());
      /*  txttogrid = (TextView) root.findViewById(R.id.togrid);
        txttogrid.setOnClickListener(this);*/
        mToolbar = (Toolbar) root.findViewById(R.id.toolbar);

        pro = new ProgressDialog(getActivity());
        pro.setMessage("Loading...");
        pro.setTitle("");
        pro.setCancelable(false);
        rlagbal = (Button) root.findViewById(R.id.agacc);
        rlcomm = (Button) root.findViewById(R.id.comacc);
        rlairtime = (RelativeLayout) root.findViewById(R.id.rl1);
        rldepo = (RelativeLayout) root.findViewById(R.id.rl5);
        rltransf = (RelativeLayout) root.findViewById(R.id.rl2);
        rlpybill = (RelativeLayout) root.findViewById(R.id.rl6);
        rlopenacc = (RelativeLayout) root.findViewById(R.id.rl4);
        rlwithdrw = (RelativeLayout) root.findViewById(R.id.rl3);
        rlopenaccinside = (RelativeLayout) root.findViewById(R.id.rlopenaccinside);
        rlagbal.setOnClickListener(this);
        rlcomm.setOnClickListener(this);
        rlairtime.setOnClickListener(this);
        rltransf.setOnClickListener(this);
        rlpybill.setOnClickListener(this);
        rlopenaccinside.setOnClickListener(this);
        rlwithdrw.setOnClickListener(this);
        rldepo.setOnClickListener(this);


        rlagac = (RelativeLayout) root.findViewById(R.id.rlagac);
        rlcommac = (RelativeLayout) root.findViewById(R.id.rlcommwal);
        rlagac.setOnClickListener(this);
        rlcommac.setOnClickListener(this);
        boolean  checktpref = session.checkShwBal();
        SecurityLayer.Log("Boolean checkpref", String.valueOf(checktpref));

        String cntopen = session.getString(SessionManagement.KEY_SETCNTOPEN);
if(Utility.isNotNull(cntopen)) {
    Log.v("Security Can Open",cntopen);
    if (cntopen.equals("0")) {
        rlopenacc.setBackgroundColor(getResources().getColor(R.color.white));
        rlopenaccinside.setVisibility(View.GONE);
    } else if (cntopen.equals("1")) {
        rlopenacc.setBackground(getResources().getDrawable(R.drawable.rectangle428));
        rlopenaccinside.setVisibility(View.VISIBLE);
    }
}
        if(checktpref == false){
            if(session.getString(SessionManagement.KEY_SETBANKS).equals("N")) {
              //  GetServv();
            }
            //  setBalInquSec();
         /*   rlpybill.setBackgroundResource(R.drawable.fbnmiddle);
            rlopenacc.setBackgroundResource(R.drawable.fbnmiddle);*/
        }else{
            //rlbuttons.setVisibility(View.GONE);
    /*        rlpybill.setBackgroundResource(R.drawable.fbnbottomright);
            rlopenacc.setBackgroundResource(R.drawable.fbnbottomleft);*/
        }
        ((FMobActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.newtoolbar);
        checkAppvers();
        String chkappvs = session.getString("APPVERSBOOL");
        SecurityLayer.Log("chkappvs",chkappvs);
       if(chkappvs.equals("Y")) {
            GetAppversion();
       }
        return root;
    }



    public void StartChartAct(int i){


    }




    @Override
    public void onClick(View view) {
       /* if (view.getId() == R.id.togrid) {
            swithgrid();

        }*/
        if (view.getId() == R.id.rl2) {
           /* Fragment fragment = new FTMenu();
            String title = "Transfer";
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment, title);
            fragmentTransaction.addToBackStack(title);
            fragmentTransaction.commit();
            ((FMobActivity) getActivity())
                    .setActionBarTitle(title);*/

            startActivity(new Intent(getActivity(), CashDepoTransActivity.class));

        }

        if (view.getId() == R.id.rl3) {
            /*Fragment fragment = new Withdraw();
            String title = "Withdraw";
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment, title);
            fragmentTransaction.addToBackStack(title);
            fragmentTransaction.commit();
            ((FMobActivity) getActivity())
                    .setActionBarTitle(title);*/


            startActivity(new Intent(getActivity(), WithdrawActivity.class));

        } if(view.getId() == R.id.rl6){
          /*  Fragment fragment = new BillMenu();
            String title = "Pay Bills";
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment, title);
            fragmentTransaction.addToBackStack(title);
            fragmentTransaction.commit();
            ((FMobActivity) getActivity())
                    .setActionBarTitle(title);*/

            startActivity(new Intent(getActivity(), BillMenuActivity.class));
        }  if (view.getId() == R.id.rl1) {
           /* Fragment fragment = new AirtimeTransf();
            String title = "Airtime Transfer";
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment, title);
            fragmentTransaction.addToBackStack(title);
            fragmentTransaction.commit();
            ((FMobActivity) getActivity())
                    .setActionBarTitle(title);*/

            startActivity(new Intent(getActivity(), AirtimeTransfActivity.class));

        }
        if (view.getId() == R.id.rl5) {
           /* Fragment fragment = new CashDepo();
            String title = "Transfer";
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment, title);
            fragmentTransaction.addToBackStack(title);
            fragmentTransaction.commit();
            ((FMobActivity) getActivity())
                    .setActionBarTitle(title);*/



            startActivity(new Intent(getActivity(), CashDepoTransActivity.class));

        }
        if (view.getId() == R.id.rlopenaccinside) {
      /*      Fragment fragment = new OpenAcc();
            String title = "Coming Soon";
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment, title);
            fragmentTransaction.addToBackStack(title);
            fragmentTransaction.commit();
            ((FMobActivity) getActivity())
                    .setActionBarTitle(title);*/
            //nav = true;

            startActivity(new Intent(getActivity(), OpenAccActivity.class));
        }
        if (view.getId() == R.id.rlagac) {
            //   SetDialog("Select Bank");

            /*android.app.Fragment  fragment = new Minstat();
            String title = "Mini Statement";
            ((FMobActivity)getActivity()).addAppFragment(fragment,title);*/
           /* new MaterialDialog.Builder(getActivity())
                    .title("Enter PIN")
                    .content("Please enter Login PIN")
                    .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    .inputMaxLength(5)
                    .input("Login PIN", "", new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            // Do something
                           String pin =  input.toString();
                           Log.v("Login Pin",pin);
                           String encpin = Utility.getencpin(pin);
                            String usid = Utility.gettUtilUserId(getActivity());
                            String mobnoo = Utility.gettUtilMobno(getActivity());
                            String params = "1" + "/"+usid+"/" + encpin  + "/"+mobnoo;
                            LogRetro(params,"MINIST");
                        }
                    }).show();*/

            ((FMobActivity)getActivity()).showEditDialog("MINIST");
           /* FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,"Mini Statement");

            ((FMobActivity)getActivity())
                    .setActionBarTitle("Mini Statement");
            fragmentTransaction.commit();*/

        }
        if (view.getId() == R.id.rlcommwal) {
            //   SetDialog("Select Bank");
           /* new MaterialDialog.Builder(getActivity())
                    .title("Enter PIN")
                    .content("Please enter Login PIN")
                    .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    .inputMaxLength(5)
                    .input("Login PIN", "", new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            // Do something
                            String pin =  input.toString();
                            Log.v("Login Pin",pin);
                            String encpin = Utility.getencpin(pin);
                            String usid = Utility.gettUtilUserId(getActivity());
                            String mobnoo = Utility.gettUtilMobno(getActivity());
                            String params = "1" + "/"+usid+"/" + encpin  + "/"+mobnoo;
                            LogRetro(params,"COMM");
                        }
                    }).show();*/


            ((FMobActivity)getActivity()).showEditDialog("COMM");
          /*  FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,"Commissions Report");

            ((FMobActivity)getActivity())
                    .setActionBarTitle("Commisions Report");
            fragmentTransaction.commit();*/

        }
    }

    public void  swithgrid(){
        Fragment  fragment = new HomeAccountFragNewUI();
        String title = "New Grid";

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_down);
        //  String tag = Integer.toString(title);
        fragmentTransaction.replace(R.id.container_body, fragment,title);
        fragmentTransaction.addToBackStack(title);
        fragmentTransaction.commit();

        // ((FMobActivity)getActivity()).addFragment(fragment,title);
        //   getActivity().overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
    }

    private void GetServv() {
        //  prgDialog2.show();
        String endpoint= "transfer/getbanks.action";

        if(!(getActivity() == null)) {
            String usid = Utility.gettUtilUserId(getActivity());
            String agentid = Utility.gettUtilAgentId(getActivity());


            String params = usid + "/" + agentid + "/93939393";

            String urlparams = "";
            try {
                urlparams = SecurityLayer.genURLCBC(params, endpoint, getActivity());
                //SecurityLayer.Log("cbcurl",url);
                SecurityLayer.Log("RefURL", urlparams);
                SecurityLayer.Log("refurl", urlparams);
                SecurityLayer.Log("params", params);
            } catch (Exception e) {
                SecurityLayer.Log("encryptionerror", e.toString());
            }


            ApiInterface apiService =
                    ApiSecurityClient.getClient(getActivity()).create(ApiInterface.class);


            Call<String> call = apiService.setGenericRequestRaw(urlparams);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        // JSON Object


                        SecurityLayer.Log("Get Banks Resp", response.body());
                        SecurityLayer.Log("response..:", response.body());
                        JSONObject obj = new JSONObject(response.body());
                        //obj = Utility.onresp(obj,getActivity());
                        obj = SecurityLayer.decryptTransaction(obj, getActivity());
                        SecurityLayer.Log("decrypted_response", obj.toString());


                        JSONArray servdata = obj.optJSONArray("data");
                        String strservdata = servdata.toString();
                        //session.setString(SecurityLayer.KEY_APP_ID,appid);

                        if (!(response.body() == null)) {
                            String respcode = obj.optString("responseCode");
                            String responsemessage = obj.optString("message");

                            SecurityLayer.Log("Response Message", responsemessage);

                            if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                                if (!(Utility.checkUserLocked(respcode))) {
                                    SecurityLayer.Log("Response Message", responsemessage);

                                    if (respcode.equals("00")) {
                                        SecurityLayer.Log("JSON Aray", servdata.toString());
                                        session.setString(SessionManagement.KEY_SETBANKS, "Y");
                                        session.setString(SessionManagement.KEY_BANKS, strservdata);

                                    } else {
                                        Toast.makeText(
                                                getActivity(),
                                                "" + responsemessage,
                                                Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    getActivity().finish();
                                    startActivity(new Intent(getActivity(), SignInActivity.class));
                                    Toast.makeText(
                                            getActivity(),
                                            "You have been locked out of the app.Please call customer care for further details",
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
                        // prgDialog2.dismiss();


                    } catch (JSONException e) {
                        SecurityLayer.Log("encryptionJSONException", e.toString());
                        // TODO Auto-generated catch block
                        Toast.makeText(getActivity(), getActivity().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                        // SecurityLayer.Log(e.toString());

                    } catch (Exception e) {
                        SecurityLayer.Log("encryptionJSONException", e.toString());
                        // SecurityLayer.Log(e.toString());
                    }
                    //    prgDialog2.dismiss();
                    if (session.getString(SessionManagement.KEY_SETBILLERS).equals("N")) {
                        if (!(getActivity() == null)) {
                            GetBillPay();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // Log error here since request failed
                    // Log error here since request failed
                    SecurityLayer.Log("throwable error", t.toString());


                    Toast.makeText(
                            getActivity(),
                            "There was an error on your request",
                            Toast.LENGTH_LONG).show();


                    //   prgDialog2.dismiss();
                    if (session.getString(SessionManagement.KEY_SETBILLERS).equals("N")) {
                        GetBillPay();
                    }
                }
            });
        }
    }


    private void GetBillPay() {
        //   prgDialog2.show();
        String endpoint= "billpayment/services.action";
        if(!(getActivity() == null)) {
            String usid = Utility.gettUtilUserId(getActivity());
            String agentid = Utility.gettUtilAgentId(getActivity());
            String mobnoo = Utility.gettUtilMobno(getActivity());
            String params = "1/" + usid + "/" + agentid + "/9493818389";


            String urlparams = "";
            try {
                urlparams = SecurityLayer.genURLCBC(params, endpoint, getActivity());
                //SecurityLayer.Log("cbcurl",url);
                SecurityLayer.Log("RefURL", urlparams);
                SecurityLayer.Log("refurl", urlparams);
                SecurityLayer.Log("params", params);
            } catch (Exception e) {
                SecurityLayer.Log("encryptionerror", e.toString());
            }


            ApiInterface apiService =
                    ApiSecurityClient.getClient(getActivity()).create(ApiInterface.class);


            Call<String> call = apiService.setGenericRequestRaw(urlparams);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        // JSON Object


                        SecurityLayer.Log("Cable TV Resp", response.body());
                        SecurityLayer.Log("response..:", response.body());
                        JSONObject obj = new JSONObject(response.body());
                        //obj = Utility.onresp(obj,getActivity());
                        obj = SecurityLayer.decryptTransaction(obj, getActivity());
                        SecurityLayer.Log("decrypted_response", obj.toString());


                        JSONArray servdata = obj.optJSONArray("data");
                        //session.setString(SecurityLayer.KEY_APP_ID,appid);

                        if (!(response.body() == null)) {
                            String respcode = obj.optString("responseCode");
                            String responsemessage = obj.optString("message");

                            SecurityLayer.Log("Response Message", responsemessage);

                            if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                                if (!(Utility.checkUserLocked(respcode))) {
                                    SecurityLayer.Log("Response Message", responsemessage);

                                    if (respcode.equals("00")) {
                                        SecurityLayer.Log("JSON Aray", servdata.toString());
                                        String billerdata = servdata.toString();
                                        session.setString(SessionManagement.KEY_SETBILLERS, "Y");
                                        session.setString(SessionManagement.KEY_BILLERS, billerdata);
                                    } else {
                                        Toast.makeText(
                                                getActivity(),
                                                "" + responsemessage,
                                                Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    getActivity().finish();
                                    startActivity(new Intent(getActivity(), SignInActivity.class));
                                    Toast.makeText(
                                            getActivity(),
                                            "You have been locked out of the app.Please call customer care for further details",
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
                        // prgDialog2.dismiss();


                    } catch (JSONException e) {
                        SecurityLayer.Log("encryptionJSONException", e.toString());
                        // TODO Auto-generated catch block
                        Toast.makeText(getActivity(), getActivity().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                        // SecurityLayer.Log(e.toString());

                    } catch (Exception e) {
                        SecurityLayer.Log("encryptionJSONException", e.toString());
                        // SecurityLayer.Log(e.toString());
                    }
                    if(!(getActivity() == null)) {
                        if(session.getString(SessionManagement.KEY_SETWALLETS).equals("N")) {
                            GetWallets();
                        }

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // Log error here since request failed
                    // Log error here since request failed
                    SecurityLayer.Log("throwable error", t.toString());


                    Toast.makeText(
                            getActivity(),
                            "There was an error on your request",
                            Toast.LENGTH_LONG).show();

                    if(!(getActivity() == null)) {
                        if(session.getString(SessionManagement.KEY_SETWALLETS).equals("N")) {
                            GetWallets();
                        }

                    }
                }
            });
        }
    }

    private void PopulateAirtime() {
        //  planetsList.clear();
        String endpoint= "billpayment/MMObillers.action";

        //  prgDialog.show();
        if(!(getActivity() == null)) {
            String usid = Utility.gettUtilUserId(getActivity());
            String agentid = Utility.gettUtilAgentId(getActivity());
            String mobnoo = Utility.gettUtilMobno(getActivity());
            String params = "1/" + usid + "/" + agentid + "/" + mobnoo + "/1";
            String urlparams = "";
            try {
                urlparams = SecurityLayer.genURLCBC(params, endpoint, getActivity());
                //SecurityLayer.Log("cbcurl",url);
                SecurityLayer.Log("RefURL", urlparams);
                SecurityLayer.Log("refurl", urlparams);
                SecurityLayer.Log("params", params);
            } catch (Exception e) {
                SecurityLayer.Log("encryptionerror", e.toString());
            }


            ApiInterface apiService =
                    ApiSecurityClient.getClient(getActivity()).create(ApiInterface.class);


            Call<String> call = apiService.setGenericRequestRaw(urlparams);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        // JSON Object

                        SecurityLayer.Log("response..:", response.body());
                        JSONObject obj = new JSONObject(response.body());
                        //obj = Utility.onresp(obj,getActivity());
                        obj = SecurityLayer.decryptTransaction(obj, getActivity());
                        SecurityLayer.Log("decrypted_response", obj.toString());

                        String respcode = obj.optString("responseCode");
                        String responsemessage = obj.optString("message");


                        JSONArray plan = obj.optJSONArray("data");
                        //session.setString(SecurityLayer.KEY_APP_ID,appid);


                        if (!(response.body() == null)) {
                            if (respcode.equals("00")) {

                                SecurityLayer.Log("Response Message", responsemessage);
                                SecurityLayer.Log("JSON Aray", plan.toString());
                                String billerdata = plan.toString();
                                session.setString(SessionManagement.KEY_SETAIRTIME, "Y");
                                session.setString(SessionManagement.KEY_AIRTIME, billerdata);


                            } else {
                                Toast.makeText(
                                        getActivity(),
                                        responsemessage,
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(
                                    getActivity(),
                                    "There was an error processing your request ",
                                    Toast.LENGTH_LONG).show();
                        }


                    } catch (JSONException e) {
                        SecurityLayer.Log("encryptionJSONException", e.toString());
                        Utility.errornexttoken();
                        // TODO Auto-generated catch block
                        Toast.makeText(getActivity(), getActivity().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                        // SecurityLayer.Log(e.toString());

                    } catch (Exception e) {
                        Utility.errornexttoken();
                        SecurityLayer.Log("encryptionJSONException", e.toString());
                        // SecurityLayer.Log(e.toString());
                    }
                    //        prgDialog.dismiss();

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // Log error here since request failed
                    SecurityLayer.Log("Throwable error", t.toString());
                    Utility.errornexttoken();
                    Toast.makeText(
                            getActivity(),
                            "There was an error processing your request",
                            Toast.LENGTH_LONG).show();
                    //   prgDialog.dismiss();

                }
            });
        }
    }

    private void GetWallets() {
        //   prgDialog2.show();
        String endpoint= "transfer/getwallets.action";


        String usid = Utility.gettUtilUserId(getActivity());
        String agentid = Utility.gettUtilAgentId(getActivity());


        String params = usid+"/"+agentid+"/93939393";

        String urlparams = "";
        try {
            urlparams = SecurityLayer.genURLCBC(params,endpoint,getActivity());
            //SecurityLayer.Log("cbcurl",url);
            SecurityLayer.Log("RefURL",urlparams);
            SecurityLayer.Log("refurl", urlparams);
            SecurityLayer.Log("params", params);
        } catch (Exception e) {
            SecurityLayer.Log("encryptionerror",e.toString());
        }





        ApiInterface apiService =
                ApiSecurityClient.getClient(getActivity()).create(ApiInterface.class);


        Call<String> call = apiService.setGenericRequestRaw(urlparams);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    // JSON Object


                    SecurityLayer.Log("Get Wallets Resp", response.body());
                    SecurityLayer.Log("response..:", response.body());
                    JSONObject obj = new JSONObject(response.body());
                    //obj = Utility.onresp(obj,getActivity());
                    obj = SecurityLayer.decryptTransaction(obj, getActivity());
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

                                        SecurityLayer.Log("JSON Aray", servdata.toString());
                                        String billerdata = servdata.toString();
                                        session.setString(SessionManagement.KEY_SETWALLETS,"Y");
                                        session.setString(SessionManagement.KEY_WALLETS,billerdata);

                                    }else{
                                        Toast.makeText(
                                                getActivity(),
                                                "No services available  ",
                                                Toast.LENGTH_LONG).show();
                                    }

                                }else{
                                    Toast.makeText(
                                            getActivity(),
                                            "" + responsemessage,
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                getActivity().finish();
                                startActivity(new Intent(getActivity(), SignInActivity.class));
                                Toast.makeText(
                                        getActivity(),
                                        "You have been locked out of the app.Please call customer care for further details",
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
                    // prgDialog2.dismiss();




                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    Toast.makeText(getActivity(), getActivity().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // SecurityLayer.Log(e.toString());
                }
                if(!(getActivity() == null)) {
                    if(session.getString(SessionManagement.KEY_SETAIRTIME).equals("N")) {
                        PopulateAirtime();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                // Log error here since request failed
                SecurityLayer.Log("throwable error",t.toString());


                Toast.makeText(
                        getActivity(),
                        "There was an error on your request",
                        Toast.LENGTH_LONG).show();


                if(!(getActivity() == null)) {
                    if(session.getString(SessionManagement.KEY_SETAIRTIME).equals("N")) {
                        PopulateAirtime();
                    }

                }

            }
        });

    }

    private void LogRetro(String params, final String service) {


pro.show();
        String endpoint= "login/login.action/";

        String urlparams = "";
        try {
            urlparams = SecurityLayer.generalLogin(params,"23322",getActivity(),endpoint);
            //SecurityLayer.Log("cbcurl",url);
            SecurityLayer.Log("RefURL",urlparams);
            SecurityLayer.Log("refurl", urlparams);
            SecurityLayer.Log("params", params);
        } catch (Exception e) {
            SecurityLayer.Log("encryptionerror",e.toString());
        }





        ApiInterface apiService =
                ApiSecurityClient.getClient(getActivity()).create(ApiInterface.class);


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
                    obj = SecurityLayer.decryptGeneralLogin(obj, getActivity());
                    SecurityLayer.Log("decrypted_response", obj.toString());

                    String respcode = obj.optString("responseCode");
                    String responsemessage = obj.optString("message");




                    JSONObject datas = obj.optJSONObject("data");

                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if (Utility.isNotNull(respcode) && Utility.isNotNull(responsemessage)) {
                        SecurityLayer.Log("Response Message", responsemessage);

                        if (respcode.equals("00")) {
                            if (!(datas == null)) {
                                if(service.equals("MINIST")) {
                                    android.app.Fragment fragment = new Minstat();
                                    String title = "Mini Statement";
                                    ((FMobActivity) getActivity()).addAppFragment(fragment, title);
                                }
                                if(service.equals("COMM")) {
                                    android.app.Fragment  fragment = new CommReport();


                                    String title = "Commissions Report";
                                    ((FMobActivity)getActivity()).addAppFragment(fragment,title);
                                }
                            }
                        }
                        else {

                            Toast.makeText(
                                    getActivity(),
                                    responsemessage,
                                    Toast.LENGTH_LONG).show();


                        }

                    }
                    else {

                        Toast.makeText(
                                getActivity(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();


                    }

                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    Toast.makeText(getActivity(), getActivity().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // SecurityLayer.Log(e.toString());
                }
                if ((pro != null) && pro.isShowing() && !(getActivity() == null)) {
                    pro.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                SecurityLayer.Log("Throwable error",t.toString());
                Toast.makeText(
                        getActivity(),
                        "There was an error processing your request",
                        Toast.LENGTH_LONG).show();
                if ((pro != null) && pro.isShowing() && !(getActivity() == null)) {
                    pro.dismiss();
                }

            }
        });

    }


    private void GetAppversion() {
pro.show();
        String endpoint= "reg/appVersion.action";
        if(!(getActivity() == null)) {
            String usid = Utility.gettUtilUserId(getActivity());
            String appid = Utility.getFinAppid(getActivity());
            String appvers = Utility.getAppVersion(getActivity());
            String params = "1/" + usid + "/" + appid + "/"+appvers;

            SecurityLayer.Log("params", params);
            String urlparams = "";
            try {
                urlparams = SecurityLayer.genURLCBC(params, endpoint, getActivity());
                //SecurityLayer.Log("cbcurl",url);
                SecurityLayer.Log("RefURL", urlparams);
                SecurityLayer.Log("refurl", urlparams);
                SecurityLayer.Log("params", params);
            } catch (Exception e) {
                SecurityLayer.Log("encryptionerror", e.toString());
            }


            ApiInterface apiService =
                    ApiSecurityClient.getClient(getActivity()).create(ApiInterface.class);


            Call<String> call = apiService.setGenericRequestRaw(urlparams);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        // JSON Object

                        long secs = (new Date().getTime())/1000;
                        session.setLong("CHECKTIME",secs);
                        SecurityLayer.Log("Cable TV Resp", response.body());
                        SecurityLayer.Log("response..:", response.body());
                        JSONObject obj = new JSONObject(response.body());
                        //obj = Utility.onresp(obj,getActivity());
                        obj = SecurityLayer.decryptTransaction(obj, getActivity());
                        SecurityLayer.Log("decrypted_response", obj.toString());


                        JSONObject servdata = obj.optJSONObject("data");
                        //session.setString(SecurityLayer.KEY_APP_ID,appid);

                        if (!(response.body() == null)) {
                            String respcode = obj.optString("responseCode");
                            String responsemessage = obj.optString("message");

                            SecurityLayer.Log("Response Message", responsemessage);

                            if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                                if (!(Utility.checkUserLocked(respcode))) {
                                    SecurityLayer.Log("Response Message", responsemessage);

                                    if (respcode.equals("00")) {

                                        String reqversion = servdata.optString("minVersion");
                                        SecurityLayer.Log("Min version", reqversion);
                                     //   double dbvers = Double.parseDouble(minversion);
                                        String currvers = Utility.getAppVersion(getActivity());
                                        SecurityLayer.Log("Curr version", currvers);


                                                SecurityLayer.Log("Curr version sorted", currvers.replace(".",""));
                                      //  double dbcurrvers = Double.parseDouble(currvers);
                                        if( Utility.compareversionsupdatenew(reqversion,currvers)) {
                                            final String packageName = "stanbic.stanbicmob.com.stanbicagent";
                                            new MaterialDialog.Builder(getActivity())
                                                    .title(getActivity().getString(R.string.appupd_verstitle))
                                                    .content(getActivity().getString(R.string.appupd_vers))
                                                    .positiveText("Upgrade")
                                                    .negativeText("Not Now")
                                                    .callback(new MaterialDialog.ButtonCallback() {
                                                        @Override
                                                        public void onPositive(MaterialDialog dialog) {
                                                            try {
                                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                                                            } catch (android.content.ActivityNotFoundException anfe) {
                                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                                                            }

                                                        }

                                                        @Override
                                                        public void onNegative(MaterialDialog dialog) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    } else {
                                       /* Toast.makeText(
                                                getActivity(),
                                                "" + responsemessage,
                                                Toast.LENGTH_LONG).show();*/
                                    }
                                } else {
                                  /*  getActivity().finish();
                                    startActivity(new Intent(getActivity(), SignInActivity.class));
                                    Toast.makeText(
                                            getActivity(),
                                            "You have been locked out of the app.Please call customer care for further details",
                                            Toast.LENGTH_LONG).show();*/
                                    if(!(getActivity() == null)) {
                                        ((FMobActivity) getActivity()).LogOut();
                                    }
                                }
                            } else {

                               /* Toast.makeText(
                                        getActivity(),
                                        "There was an error on your request",
                                        Toast.LENGTH_LONG).show();*/


                            }
                        } else {

                          /*  Toast.makeText(
                                    getActivity(),
                                    "There was an error on your request",
                                    Toast.LENGTH_LONG).show();
*/

                        }
                        // prgDialog2.dismiss();


                    } catch (JSONException e) {
                        SecurityLayer.Log("encryptionJSONException", e.toString());
                        // TODO Auto-generated catch block
                        if(!(getActivity() == null)) {
                            Toast.makeText(getActivity(), getActivity().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                            ((FMobActivity) getActivity()).SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getActivity());

                        }
                        // SecurityLayer.Log(e.toString());

                    } catch (Exception e) {
                        SecurityLayer.Log("encryptionJSONException", e.toString());
                        if(!(getActivity() == null)) {
                            Toast.makeText(getActivity(), getActivity().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                            // SecurityLayer.Log(e.toString());
                            ((FMobActivity) getActivity()).SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getActivity());
                        }
                        // SecurityLayer.Log(e.toString());
                    }
                    if(!(getActivity() == null)){
                        pro.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // Log error here since request failed
                    // Log error here since request failed
                    SecurityLayer.Log("throwable error", t.toString());

                    long secs = (new Date().getTime())/1000;
                    session.setLong("CHECKTIME",secs);

                    if(!(getActivity() == null)){
                        pro.dismiss();
                        Toast.makeText(
                                getActivity(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();
                    }


                }
            });
        }
    }

public void checkAppvers(){
    session.setString("APPVERSBOOL","Y");
        long checkcurrtime = session.getLong("CHECKTIME");
    SecurityLayer.Log("checkcurrtime",Long.toString(checkcurrtime));
        if(checkcurrtime == 0){
            long secs = (new Date().getTime())/1000;
            session.setLong("CHECKTIME",secs);
        }
    long checknewcurrtime = session.getLong("CHECKTIME");
    SecurityLayer.Log("checknewcurrtime",Long.toString(checknewcurrtime));
    long currsecs = (new Date().getTime()) / 1000;
    long diffsecs = currsecs-checknewcurrtime;
    SecurityLayer.Log("diffsecs",Long.toString(diffsecs));
        if(diffsecs > 86400){
            session.setString("APPVERSBOOL","Y");
        }
        else{
            session.setString("APPVERSBOOL","N");
        }
}


    @Override
    public void onResume() {
        super.onResume();
        ((FMobActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

    }
    @Override
    public void onStop() {
        super.onStop();
        ((FMobActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5f5f5")));

    }



}
