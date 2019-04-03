package stanbic.stanbicmob.com.stanbicagent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import org.json.JSONException;
import org.json.JSONObject;

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

public class ConfirmCashTransf extends Fragment  implements View.OnClickListener{
    TextView recacno,recname,recamo,recnarr,recsendnum,recsendnam,txtfee;
    Button btnsub;
    String recanno, amou ,narra, ednamee,ednumbb,txtname,agbalance ;
    ProgressDialog prgDialog,prgDialog2;
    EditText etpin;
    RelativeLayout rlsendname,rlsendno;
    TextView step1,step2,acbal;
    public ConfirmCashTransf() {
        // Required empty public constructor
    }
  /*  private static Fragment newInstance(Context context) {
        LayoutOne f = new LayoutOne();

        return f;
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.confcashtransf, null);
        recacno = (TextView) root.findViewById(R.id.textViewnb2);
        recname = (TextView) root.findViewById(R.id.textViewcvv);
        acbal = (TextView) root.findViewById(R.id.txtacbal);
        etpin = (EditText) root.findViewById(R.id.pin);
        recamo = (TextView) root.findViewById(R.id.textViewrrs);
        recnarr = (TextView) root.findViewById(R.id.textViewrr);
        txtfee = (TextView) root.findViewById(R.id.txtfee);
        step1 = (TextView) root.findViewById(R.id.tv);
        step1.setOnClickListener(this);
        step2 = (TextView) root.findViewById(R.id.tv2);
        step2.setOnClickListener(this);
        recsendnam = (TextView) root.findViewById(R.id.sendnammm);
        recsendnum = (TextView) root.findViewById(R.id.sendno);

        rlsendname = (RelativeLayout) root.findViewById(R.id.rlsendnam);
        rlsendno = (RelativeLayout) root.findViewById(R.id.rlsendnum);
        prgDialog2 = new ProgressDialog(getActivity());
        prgDialog2.setMessage("Loading....");
        prgDialog2.setCancelable(false);

        btnsub = (Button) root.findViewById(R.id.button2);
        btnsub.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            recanno = bundle.getString("recanno");
            amou = bundle.getString("amou");
            narra = bundle.getString("narra");
            ednamee = bundle.getString("ednamee");
            ednumbb = bundle.getString("ednumbb");
            txtname = bundle.getString("txtname");
            String trantype =  bundle.getString("trantype");
            if(trantype.equals("D")){
                rlsendname.setVisibility(View.GONE);
                rlsendno.setVisibility(View.GONE);
            }
            recacno.setText(recanno);
            recname.setText(txtname);

            recamo.setText(ApplicationConstants.KEY_NAIRA+amou);
            recnarr.setText(narra);

            recsendnam.setText(ednamee);
            recsendnum.setText(ednumbb);
            amou = Utility.convertProperNumber(amou);
            getFeeSec();
        }
        return root;
    }



    public void StartChartAct(int i){


    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.button2) {
            if (Utility.checkInternetConnection(getActivity())) {
                String agpin  = etpin.getText().toString();


                if (Utility.isNotNull(recanno)) {
                    if (Utility.isNotNull(amou)) {
                        if (Utility.isNotNull(narra)) {
                            if (Utility.isNotNull(ednamee)) {
                                if (Utility.isNotNull(ednumbb)) {
                                    if (Utility.isNotNull(agpin)) {
                                        double dbamo = Double.parseDouble(amou);
                                        Double dbagbal = Double.parseDouble(agbalance);
                                        if(dbamo <= dbagbal){
                                        String encrypted = null;

                                        if(!(prgDialog2 == null)) {
                                         //   prgDialog2.show();
                                        }

                                        String usid = Utility.gettUtilUserId(getActivity());
                                        String agentid = Utility.gettUtilAgentId(getActivity());
                                        String mobnoo = Utility.gettUtilMobno(getActivity());
                                        String params = "1/"+usid+"/"+agentid+"/"+mobnoo+"/1/"+amou+"/"+recanno+"/"+txtname+"/"+narra;

                                        // IntraBankResp(params);

                                        Bundle b  = new Bundle();
                                        b.putString("params",params);
                                        b.putString("serv","CASHTRAN");
                                        b.putString("recanno", recanno);
                                        b.putString("amou", amou);

                                        b.putString("narra", narra);
                                        b.putString("ednamee", ednamee);
                                        b.putString("ednumbb", ednumbb);
                                        b.putString("txtname", txtname);
                                        b.putString("txpin", encrypted);
                                        Fragment  fragment = new TransactingProcessing();

                                        fragment.setArguments(b);
                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        //  String tag = Integer.toString(title);
                                        fragmentTransaction.replace(R.id.container_body, fragment,"Confirm Transfer");
                                        fragmentTransaction.addToBackStack("Confirm Transfer");
                                        ((FMobActivity)getActivity())
                                                .setActionBarTitle("Confirm Transfer");
                                        fragmentTransaction.commit();
                                                /*    ApiInterface apiService =
                                                            ApiClient.getClient().create(ApiInterface.class);
                                    String usid = Utility.gettUtilUserId(getActivity());
                                    String agentid = Utility.gettUtilAgentId(getActivity());
                                    String mobnoo = Utility.gettUtilMobno(getActivity());
                                        // "0000"
                                                    Call<IntraBank> call = apiService.getIntraBankResp("1",usid,agentid,"0000","1",amou,recanno,txtname,narra,encrypted);
                                                    call.enqueue(new Callback<IntraBank>() {
                                                        @Override
                                                        public void onResponse(Call<IntraBank>call, Response<IntraBank> response) {

                                                        if(!(response.body() == null)) {
                                                            String responsemessage = response.body().getMessage();
                                                            String respcode = response.body().getRespCode();

                                                            String agcmsn = response.body().getFee();
//                                                            SecurityLayer.Log("Response Message", responsemessage);
                                                            IntraBankData datas = response.body().getResults();
                                                            if (Utility.isNotNull(respcode) && Utility.isNotNull(responsemessage)) {
                                                                if (!(Utility.checkUserLocked(respcode))) {
                                                                if(respcode.equals("00")){
                                                                String totfee = "0.00";
                                                                if(!(datas == null)){
                                                                    totfee = datas.getfee();
                                                                }
                                                                Bundle b  = new Bundle();
                                                                b.putString("recanno",recanno);
                                                                b.putString("amou",amou);
                                                                b.putString("narra",narra);
                                                                b.putString("ednamee",ednamee);
                                                                b.putString("ednumbb",ednumbb);
                                                                b.putString("txtname",txtname);
                                                                b.putString("trantype","T");
                                                                b.putString("agcmsn",agcmsn);
                                                                b.putString("fee",totfee);
                                                                Fragment  fragment = new FinalConfDepoTrans();

                                                                fragment.setArguments(b);
                                                                FragmentManager fragmentManager = getFragmentManager();
                                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                                //  String tag = Integer.toString(title);
                                                                fragmentTransaction.replace(R.id.container_body, fragment,"Confirm Transfer");
                                                                fragmentTransaction.addToBackStack("Confirm Transfer");
                                                                ((FMobActivity)getActivity())
                                                                        .setActionBarTitle("Confirm Transfer");
                                                                fragmentTransaction.commit();
                                                            }else {


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
                                                            prgDialog2.dismiss();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<IntraBank> call, Throwable t) {
                                                            // Log error here since request failed
                                                            SecurityLayer.Log("throwable error",t.toString());


                                                            Toast.makeText(
                                                                    getActivity(),
                                                                    "There was an error on your request",
                                                                    Toast.LENGTH_LONG).show();



                                                            prgDialog2.dismiss();
                                                        }
                                                    });*/
                                        ClearPin();
                                        }  else {
                                            Toast.makeText(
                                                    getActivity(),
                                                    "The amount set is higher than your agent balance",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }  else {
                                        Toast.makeText(
                                                getActivity(),
                                                "Please enter a valid value for Agent PIN",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }  else {
                                    Toast.makeText(
                                            getActivity(),
                                            "Please enter a valid value for Depositor Number",
                                            Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(
                                        getActivity(),
                                        "Please enter a valid value for Depositor Name",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(
                                    getActivity(),
                                    "Please enter a valid value for Narration",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(
                                getActivity(),
                                "Please enter a valid value for Amount",
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(
                            getActivity(),
                            "Please enter a value for Account Number",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        if (view.getId() == R.id.tv) {
            Fragment  fragment = new CashDepoTrans();


            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,"Cash Depo Trans");
            fragmentTransaction.addToBackStack("Cash Depo Trans");
            ((FMobActivity)getActivity())
                    .setActionBarTitle("Cash Depo Trans");
            fragmentTransaction.commit();
        }
        if (view.getId() == R.id.tv2) {
            Fragment  fragment = new FTMenu();


            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,"Confirm Transfer");
            fragmentTransaction.addToBackStack("Confirm Transfer");
            ((FMobActivity)getActivity())
                    .setActionBarTitle("Confirm Transfer");
            fragmentTransaction.commit();
        }
    }

    public void getFee(){
        if(!(prgDialog2 == null) ) {
            prgDialog2.show();
        }
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        String usid = Utility.gettUtilUserId(getActivity());
        String agentid = Utility.gettUtilAgentId(getActivity());
        Call<GetFee> call = apiService.getFee("1", usid, agentid, "FTINTRABANK",  amou);
        call.enqueue(new Callback<GetFee>() {
            @Override
            public void onResponse(Call<GetFee> call, Response<GetFee> response) {
                if (!(response.body() == null)) {
                String responsemessage = response.body().getMessage();
                String respfee = response.body().getFee();
                SecurityLayer.Log("Response Message", responsemessage);
                if(respfee == null || respfee.equals("")){
                    txtfee.setText("N/A");
                }else{
                    respfee = Utility.returnNumberFormat(respfee);
                    txtfee.setText(ApplicationConstants.KEY_NAIRA+respfee);
                }
                    if(!(prgDialog2 == null) && prgDialog2.isShowing() && getActivity() != null) {
                        prgDialog2.dismiss();
                    }
           }else{
                txtfee.setText("N/A");
            }
            }

            @Override
            public void onFailure(Call<GetFee> call, Throwable t) {
                // Log error here since request failed
                SecurityLayer.Log("Throwable error",t.toString());
                Toast.makeText(
                        getActivity(),
                        "There was an error processing your request",
                        Toast.LENGTH_LONG).show();
                if(!(prgDialog2 == null) && prgDialog2.isShowing() && getActivity() != null) {
                    prgDialog2.dismiss();
                }
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        // put your code here...

    }
    public void ClearPin(){
        etpin.setText("");
    }


    private void getFeeSec() {
        if(!(prgDialog2 == null) ) {
            prgDialog2.show();
        }
        String endpoint= "fee/getfee.action";


        String usid = Utility.gettUtilUserId(getActivity());
        String agentid = Utility.gettUtilAgentId(getActivity());

        String params = "1/"+usid+"/"+agentid+"/FTINTRABANK/"+amou;
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

                    SecurityLayer.Log("response..:", response.body());
                    JSONObject obj = new JSONObject(response.body());
                    //obj = Utility.onresp(obj,getActivity());
                    obj = SecurityLayer.decryptTransaction(obj, getActivity());
                    SecurityLayer.Log("decrypted_response", obj.toString());

                    String respcode = obj.optString("responseCode");
                    String responsemessage = obj.optString("message");
                    String respfee = obj.optString("fee");
                     agbalance = obj.optString("data");
                     if(Utility.isNotNull(agbalance)) {
                         acbal.setText(Utility.returnNumberFormat(agbalance)+ApplicationConstants.KEY_NAIRA);
                     }

                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                        if (!(Utility.checkUserLocked(respcode))) {
                            if (!(response.body() == null)) {
                                if (respcode.equals("00")) {

                                    SecurityLayer.Log("Response Message", responsemessage);

//                                    SecurityLayer.Log("Respnse getResults",datas.toString());
                                    if (respfee == null || respfee.equals("")) {
                                        txtfee.setText("N/A");
                                    } else {
                                        respfee = Utility.returnNumberFormat(respfee);
                                        txtfee.setText(ApplicationConstants.KEY_NAIRA + respfee);
                                    }

                                } else if (respcode.equals("93")) {
                                    Toast.makeText(
                                            getActivity(),
                                            responsemessage,
                                            Toast.LENGTH_LONG).show();
                                    Fragment fragment = new CashDepoTrans();
                                    String title = "Cash Transfer";

                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    //  String tag = Integer.toString(title);
                                    fragmentTransaction.replace(R.id.container_body, fragment, title);
                                    fragmentTransaction.addToBackStack(title);
                                    ((FMobActivity) getActivity())
                                            .setActionBarTitle(title);
                                    fragmentTransaction.commit();
                                    Toast.makeText(
                                            getActivity(),
                                            "Please ensure amount set is below the set limit",
                                            Toast.LENGTH_LONG).show();

                                } else {
                                    btnsub.setVisibility(View.GONE);
                                    Toast.makeText(
                                            getActivity(),
                                            responsemessage,
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                txtfee.setText("N/A");
                            }
                        }else{
                            ((FMobActivity) getActivity()).LogOut();
                        }
                    }



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
                        ((FMobActivity) getActivity()).SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getActivity());
                    }
                    // SecurityLayer.Log(e.toString());
                }
                if(!(prgDialog2 == null) && prgDialog2.isShowing() && getActivity() != null) {
                    prgDialog2.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                SecurityLayer.Log("Throwable error",t.toString());

                if(!(prgDialog2 == null) && prgDialog2.isShowing() && getActivity() != null) {
                    prgDialog2.dismiss();
                }
                if(!(getActivity() == null)) {
                    Toast.makeText(
                            getActivity(),
                            "There was an error processing your request",
                            Toast.LENGTH_LONG).show();
                    ((FMobActivity) getActivity()).SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getActivity());

                }
            }
        });

    }


    private void IntraBankResp(String params) {

        String endpoint= "transfer/intrabank.action";


        String usid = Utility.gettUtilUserId(getActivity());
        String agentid = Utility.gettUtilAgentId(getActivity());




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


                    SecurityLayer.Log("Intra Bank Resp", response.body());
                    SecurityLayer.Log("response..:", response.body());
                    JSONObject obj = new JSONObject(response.body());
                    //obj = Utility.onresp(obj,getActivity());
                    obj = SecurityLayer.decryptTransaction(obj, getActivity());
                    SecurityLayer.Log("decrypted_response", obj.toString());




                    JSONObject datas = obj.optJSONObject("data");
                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if(!(response.body() == null)) {
                        String respcode = obj.optString("responseCode");
                        String responsemessage = obj.optString("message");
                        String agcmsn = obj.optString("fee");
                        SecurityLayer.Log("Response Message", responsemessage);

                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if (!(Utility.checkUserLocked(respcode))) {
                                if(respcode.equals("00")){
                                    String totfee = "0.00";
                                    if(!(datas == null)){
                                        totfee = datas.optString("fee");
                                    }
                                    Bundle b  = new Bundle();
                                    b.putString("recanno",recanno);
                                    b.putString("amou",amou);
                                    b.putString("narra",narra);
                                    String refcodee = datas.optString("referenceCode");
                                    b.putString("refcode",refcodee);
                                    b.putString("ednamee",ednamee);
                                    b.putString("ednumbb",ednumbb);
                                    b.putString("txtname",txtname);
                                    b.putString("trantype","T");
                                    b.putString("agcmsn",agcmsn);
                                    b.putString("fee",totfee);
                                    Fragment  fragment = new FinalConfDepoTrans();

                                    fragment.setArguments(b);
                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    //  String tag = Integer.toString(title);
                                    fragmentTransaction.replace(R.id.container_body, fragment,"Confirm Transfer");
                                    fragmentTransaction.addToBackStack("Confirm Transfer");
                                    ((FMobActivity)getActivity())
                                            .setActionBarTitle("Confirm Transfer");
                                    fragmentTransaction.commit();
                                }else {
                                    new MaterialDialog.Builder(getActivity())
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
                                    Bundle params = new Bundle();
                                    params.putString("transfer_error", responsemessage);
                                    params.putString("response_code", respcode);


                                    Answers.getInstance().logCustom(new CustomEvent("cash_deposit error code")

                                            .putCustomAttribute("deposit_error",responsemessage)
                                            .putCustomAttribute("response_code",respcode)
                                    );

                                /*    Toast.makeText(
                                            getActivity(),
                                            "" + responsemessage,
                                            Toast.LENGTH_LONG).show();*/
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
                    if(!(getActivity() == null)) {
                        Toast.makeText(getActivity(), getActivity().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                        ((FMobActivity) getActivity()).SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getActivity());
                    }
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    if(!(getActivity() == null)) {
                        ((FMobActivity) getActivity()).SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getActivity());
                    }
                    // SecurityLayer.Log(e.toString());
                }
                if(!(prgDialog2 == null) && prgDialog2.isShowing() && getActivity() != null) {
                    prgDialog2.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                SecurityLayer.Log("throwable error",t.toString());





                if(!(prgDialog2 == null) && prgDialog2.isShowing() && getActivity() != null) {
                    prgDialog2.dismiss();
                }
                if(!(getActivity() == null)) {
                    Toast.makeText(
                            getActivity(),
                            "There was an error on your request",
                            Toast.LENGTH_LONG).show();
                    ((FMobActivity) getActivity()).SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getActivity());
                }
            }
        });

    }
}
