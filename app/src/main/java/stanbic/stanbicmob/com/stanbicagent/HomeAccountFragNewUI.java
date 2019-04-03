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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adapter.Dashboard;
import adapter.DashboardAdapter;
import adapter.ServicesMenuAdapt;
import adapter.adapter.OTBRetroAdapt;
import model.BalInquiryData;
import model.BalanceInquiry;
import model.GetAgentIdData;
import model.GetBanksData;
import model.GetServicesData;
import rest.ApiClient;
import rest.ApiInterface;
import rest.ApiSecurityClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import security.SecurityLayer;


public class HomeAccountFragNewUI extends Fragment implements View.OnClickListener {
    ImageView imageView1;
    ListView lv;
    TextView tv,tvacco,tvcomm,txttogrid;
    List<GetBanksData> planetsList = new ArrayList<GetBanksData>();

    List<Dashboard> servicesList = new ArrayList<Dashboard>();
    List<GetServicesData> servicesdata = new ArrayList<GetServicesData>();
    OTBRetroAdapt aAdpt;

    Button btn1,btn2,btn3;
    ProgressDialog pro ;
    TextView curbal,lastl,greet,commamo;
    SessionManagement session;
    ProgressDialog prgDialog;
    ProgressBar prgbar;
    ImageView iv;
    ProgressDialog prgDialog2;
    ServicesMenuAdapt servadapt;
    GridView gridViewServices;
  //  LinearLayout rlbuttons,rlagbal;
    DashboardAdapter dashboardAdapter;
    LinearLayout rldepo,rlwithdrw,rltransf,rlpybill,rlairtime,rlopenacc;
    RelativeLayout rlagac,rlcommac;
    Button rlagbal,rlcomm;
    //
// ProgressBar pDialog;
    List<GetAgentIdData> plan = new ArrayList<GetAgentIdData>();
    String agid;
    public HomeAccountFragNewUI() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.content_mainnewui, null);
        btn1 = (Button) root.findViewById(R.id.btn1);
        curbal = (TextView) root.findViewById(R.id.currentbal);
        tvacco = (TextView) root.findViewById(R.id.hfjdj);
        lastl = (TextView) root.findViewById(R.id.txt2);
        greet = (TextView) root.findViewById(R.id.greet);
        tvcomm = (TextView) root.findViewById(R.id.comm);
        commamo = (TextView) root.findViewById(R.id.accountbal);
       // txttogrid = (TextView) root.findViewById(R.id.togrid);
       // txttogrid.setOnClickListener(this);

        rlagac = (RelativeLayout) root.findViewById(R.id.rlagac);
        rlcommac = (RelativeLayout) root.findViewById(R.id.rlcommwal);
        rlagac.setOnClickListener(this);
        rlcommac.setOnClickListener(this);


        pro = new ProgressDialog(getActivity());
        pro.setMessage("Loading...");
        pro.setTitle("");
        pro.setCancelable(false);

Pop();
        gridViewServices = (GridView) root.findViewById(R.id.gridView1);

//        prgDialog.setCancelable(false);

        dashboardAdapter = new DashboardAdapter(servicesList, getActivity());

      //  gridViewServices.setAdapter(dashboardAdapter);
        prgDialog2 = new ProgressDialog(getActivity());
        prgDialog2.setMessage("Loading....");
        prgDialog2.setCancelable(false);
        iv = (ImageView) root.findViewById(R.id.img);
        prgDialog = new ProgressDialog(getActivity());
       prgDialog.setMessage("Loading....");
        prgDialog.setCancelable(false);
//pDialog = new ProgressBar(getActivity());
        btn2 = (Button) root.findViewById(R.id.btn2);
        btn3 = (Button) root.findViewById(R.id.btn3);

     //   rlbuttons = (LinearLayout) root.findViewById(R.id.balances);
        rlagbal = (Button) root.findViewById(R.id.agacc);
        rlcomm = (Button) root.findViewById(R.id.commac);
        rlairtime = (LinearLayout) root.findViewById(R.id.airtim);
       rldepo = (LinearLayout) root.findViewById(R.id.depo);
        rltransf = (LinearLayout) root.findViewById(R.id.lintrans);
        rlpybill = (LinearLayout) root.findViewById(R.id.bills);
        rlopenacc = (LinearLayout) root.findViewById(R.id.opacc);
        rlwithdrw = (LinearLayout) root.findViewById(R.id.withd);
        rlagbal.setOnClickListener(this);
        rlcomm.setOnClickListener(this);
        rlairtime.setOnClickListener(this);
        rltransf.setOnClickListener(this);
        rlpybill.setOnClickListener(this);
        rlopenacc.setOnClickListener(this);
        rlwithdrw.setOnClickListener(this);
        rldepo.setOnClickListener(this);

        session = new SessionManagement(getActivity());
        //   pDialog.setBackgroundColor(getActivity().getResources().getColor(R.color.nbkyellow));
        boolean  checktpref = session.checkShwBal();
        SecurityLayer.Log("Boolean checkpref", String.valueOf(checktpref));
        if(checktpref == false){
            if(session.getString(SessionManagement.KEY_SETBANKS).equals("N")) {
             //   GetServv();
            }
          //  setBalInquSec();
         /*   rlpybill.setBackgroundResource(R.drawable.fbnmiddle);
            rlopenacc.setBackgroundResource(R.drawable.fbnmiddle);*/
        }else{
            //rlbuttons.setVisibility(View.GONE);
    /*        rlpybill.setBackgroundResource(R.drawable.fbnbottomright);
            rlopenacc.setBackgroundResource(R.drawable.fbnbottomleft);*/
        }

        String strlastl = Utility.getLastl(getActivity());
        strlastl = Utility.convertDate(strlastl);
       // lastl.setText("Last Login: "+strlastl);
        String accnoo = Utility.getAcountno(getActivity());
        if((accnoo != null)) {


        }
        Calendar cal = Calendar.getInstance();
        String time = "";
        int v =  cal.getTime().getHours();
        if(v < 12){
            time = "Morning";

        }
        if(v >= 12 && v < 18){
            time = "Afternoon";


        }
        if(v >= 18 && v <24){
            time = "Evening";

        }
        String custname = Utility.gettUtilCustname(getActivity());
//        greet.setText("Good "+time+" "+Utility.returnCustname(custname));
        String agentid = Utility.gettUtilAgentId(getActivity());
        if(Utility.isNotNull(agentid)){
            //    tvcomm.setText(agentid);
        }

        lv = (ListView) root.findViewById(R.id.lv);
        SetPop();
        String cntopen = session.getString(SessionManagement.KEY_SETCNTOPEN);
        if(Utility.isNotNull(cntopen)) {
            Log.v("Security Can Open",cntopen);
            if (cntopen.equals("0")) {
             rlopenacc.setVisibility(View.GONE);
            } else if (cntopen.equals("1")) {
                rlopenacc.setVisibility(View.VISIBLE);
            }
        }

     /*   gridViewServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String p = servicesList.get(i).getName();

                if (p.equals("Transfer")) {
                    Fragment  fragment = new FTMenu();
                    String title = "Transfer";
                    ((FMobActivity)getActivity()).addFragment(fragment,title);

                }
                else if (p.equals("Pay Bills")) {
                    Fragment  fragment = new BillMenu();
                    String title = "Pay Bills";
                    ((FMobActivity)getActivity()).addFragment(fragment,title);

                } else if (p.equals("Withdraw")) {
                    Fragment  fragment = new Withdraw();
                    String title = "Withdraw";
                    ((FMobActivity)getActivity()).addFragment(fragment,title);

                } else if(p.equals("Deposit")){
                    Fragment  fragment = new CashDepo();
                    String title = "Cash Deposit";
                    ((FMobActivity)getActivity()).addFragment(fragment,title);
                }else if (p.equals("Airtime")) {
                    Fragment  fragment = new AirtimeTransf();
                    String title = "Cash Deposit";
                    ((FMobActivity)getActivity()).addFragment(fragment,title);

                }else if (p.equals("Open Account")) {
                    Fragment  fragment = new ComingSoon();
                    String title = "Open Account";
                    ((FMobActivity)getActivity()).addFragment(fragment,title);
                    //nav = true;
                } else if (p.equals("Airtime TopUp")) {
                    Fragment fragment = new AirtimeTransf();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    //  String tag = Integer.toString(title);
                    fragmentTransaction.replace(R.id.container_body, fragment, "Airtime TopUp");
                    fragmentTransaction.addToBackStack("Airtime TopUp");
                    fragmentTransaction.commit();
                    ((MainActivity) getActivity())
                            .setActionBarTitle("Airtime TopUp");

                }

            }
        });*/


      // GetAppversion();

        checkAppvers();
        String chkappvs = session.getString("APPVERSBOOL");
        SecurityLayer.Log("chkappvs",chkappvs);
        if(chkappvs.equals("Y")) {
            GetAppversion();
        }
        return root;
    }
    public void  swithgrid(){
        Fragment  fragment = new NewHomeGrid();
        String title = "New Grid";

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
        //  String tag = Integer.toString(title);
        fragmentTransaction.replace(R.id.container_body, fragment,title);
        fragmentTransaction.addToBackStack(title);
        fragmentTransaction.commit();

       // ((FMobActivity)getActivity()).addFragment(fragment,title);
     //   getActivity().overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
    }
    public void Pop(){
        servicesList.add(new Dashboard("Transfer", R.drawable.transferic));
        servicesList.add(new Dashboard("Pay Bills", R.drawable.paybillic));


        servicesList.add(new Dashboard("Withdraw", R.drawable.withdrawic));
        servicesList.add(new Dashboard("Deposit", R.drawable.depositic));

        servicesList.add(new Dashboard("Airtime", R.drawable.airtimeic));
        servicesList.add(new Dashboard("Open Account", R.drawable.opaccic));

        //lvbann.setAdapter(FavAdapt);
    }

    public void SetPop(){
        planetsList.clear();

    }
    @Override
    public void onClick(View view) {
        /*if (view.getId() == R.id.togrid) {
           swithgrid();

        }*/
        if (view.getId() == R.id.btn1) {
            //   SetDialog("Select Bank");

           /* Fragment  fragment = new CashDepo();
            String title = "Deposit";
            ((FMobActivity)getActivity()).addFragment(fragment,title);*/

          /*  FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,"Deposit");

            ((FMobActivity)getActivity())
                    .setActionBarTitle("Deposit");
            fragmentTransaction.commit();*/



            startActivity(new Intent(getActivity(), CashDepoTransActivity.class));

        }
        if (view.getId() == R.id.btn2) {
            //   SetDialog("Select Bank");

           /* Fragment  fragment = new FTMenu();
            String title = "Transfer";
            ((FMobActivity)getActivity()).addFragment(fragment,title);*/

         /*   FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,"Transfer");

            ((FMobActivity)getActivity())
                    .setActionBarTitle("Transfer");
            fragmentTransaction.commit();*/

            startActivity(new Intent(getActivity(), CashDepoTransActivity.class));

        }
        if (view.getId() == R.id.btn3) {
            //   SetDialog("Select Bank");

           /* Fragment  fragment = new Withdraw();
            String title = "Withdraw";
            ((FMobActivity)getActivity()).addFragment(fragment,title);*/
          /*  FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,"Withdraw");

            ((FMobActivity)getActivity())
                    .setActionBarTitle("Withdraw");
            fragmentTransaction.commit();*/

            startActivity(new Intent(getActivity(), WithdrawActivity.class));

        }
        if (view.getId() == R.id.rlagac) {
            //   SetDialog("Select Bank");

           /* android.app.Fragment  fragment = new Minstat();
            String title = "Mini Statement";
            ((FMobActivity)getActivity()).addAppFragment(fragment,title);*/

            ((FMobActivity)getActivity()).showEditDialog("MINIST");

/*
            new MaterialDialog.Builder(getActivity())
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
            ((FMobActivity)getActivity()).showEditDialog("COMM");
/*
            new MaterialDialog.Builder(getActivity())
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
            /*android.app.Fragment  fragment = new CommReport();


            String title = "Commissions Report";
            ((FMobActivity)getActivity()).addAppFragment(fragment,title);*/
          /*  FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,"Commissions Report");

            ((FMobActivity)getActivity())
                    .setActionBarTitle("Commisions Report");
            fragmentTransaction.commit();*/

        }
/*
        if (view.getId() == R.id.rl1) {
            Fragment fragment = new CashDepo();
            String title = "Cash Deposit";
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment, title);
            fragmentTransaction.addToBackStack(title);
            fragmentTransaction.commit();
            ((FMobActivity) getActivity())
                    .setActionBarTitle(title);

        }*/
        if (view.getId() == R.id.depo) {
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
        if (view.getId() == R.id.lintrans) {
          /*  Fragment fragment = new FTMenu();
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
        if (view.getId() == R.id.withd) {
           /* Fragment fragment = new Withdraw();
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

        } if(view.getId() == R.id.bills){
            /*Fragment fragment = new BillMenu();
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
        }  if (view.getId() == R.id.airtim) {
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

        } if (view.getId() == R.id.opacc) {
           /* Fragment fragment = new OpenAcc();
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

    }
    public void StartChartAct(int i){


    }

    public void setBalInqu(){
        if (Utility.checkInternetConnection(getActivity())) {
            prgDialog.show();
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            String usid = Utility.gettUtilUserId(getActivity());
            String agentid = Utility.gettUtilAgentId(getActivity());
            String mobnoo = Utility.gettUtilMobno(getActivity());
            Call<BalanceInquiry> call = apiService.getBalInq("1", usid, agentid, "9493818389");
            call.enqueue(new Callback<BalanceInquiry>() {
                @Override
                public void onResponse(Call<BalanceInquiry> call, Response<BalanceInquiry> response) {
                    if(!(response.body() == null)) {
                        String responsemessage = response.body().getMessage();
                        BalInquiryData baldata = response.body().getData();
                        SecurityLayer.Log("Response Message", responsemessage);

//                                    SecurityLayer.Log("Respnse getResults",datas.toString());
                        if (!(baldata == null)) {
                            String balamo = baldata.getbalance();
                            String comamo =baldata.getcommision();


                            String fbal = Utility.returnNumberFormat(balamo);
                            curbal.setText(Html.fromHtml("&#8358") + " " + fbal);

                            String cmbal = Utility.returnNumberFormat(comamo);
                            commamo.setText(Html.fromHtml("&#8358") + " " + cmbal);
                        } else {
                            Toast.makeText(
                                    getActivity(),
                                    "There was an error retrieving your balance ",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(
                                getActivity(),
                                "There was an error retrieving your balance ",
                                Toast.LENGTH_LONG).show();
                    }  try {
                        if ((prgDialog != null) && prgDialog.isShowing()) {
                            prgDialog.dismiss();
                        }
                    } catch (final IllegalArgumentException e) {
                        // Handle or log or ignore
                    } catch (final Exception e) {
                        // Handle or log or ignore
                    } finally {
                        prgDialog = null;
                    }

                    //  prgDialog.dismiss();
                }

                @Override
                public void onFailure(Call<BalanceInquiry> call, Throwable t) {
                    // Log error here since request failed
                    SecurityLayer.Log("Throwable error",t.toString());
                    Toast.makeText(
                            getActivity(),
                            "There was an error retrieving your balance ",
                            Toast.LENGTH_LONG).show();
                    prgDialog.dismiss();
                }
            });
        }
    }
    private void dismissProgressDialog() {
        if (prgDialog != null && prgDialog.isShowing()) {
            prgDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }



    private void setBalInquSec() {

        prgDialog2.show();
        String endpoint= "core/balenquirey.action";


        String usid = Utility.gettUtilUserId(getActivity());
        String agentid = Utility.gettUtilAgentId(getActivity());
        String params = "1/"+usid+"/"+agentid+"/9493818389";
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


                    JSONObject plan = obj.optJSONObject("data");
                    //session.setString(SecurityLayer.KEY_APP_ID,appid);


                    if(!(response.body() == null)) {
                        if (respcode.equals("00")) {

                            SecurityLayer.Log("Response Message", responsemessage);

//                                    SecurityLayer.Log("Respnse getResults",datas.toString());
                            if (!(plan == null)) {
                                String balamo = plan.optString("balance");
                                String comamo = plan.optString("commision");


                                String fbal = Utility.returnNumberFormat(balamo);
                                curbal.setText(Html.fromHtml("&#8358") + " " + fbal);

                                String cmbal = Utility.returnNumberFormat(comamo);
                                commamo.setText(Html.fromHtml("&#8358") + " " + cmbal);
                            } else {
                                Toast.makeText(
                                        getActivity(),
                                        "There was an error retrieving your balance ",
                                        Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(
                                    getActivity(),
                                    "There was an error retrieving your balance ",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(
                                getActivity(),
                                "There was an error retrieving your balance ",
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

                prgDialog2.dismiss();
                if(session.getString(SessionManagement.KEY_SETBANKS).equals("N")) {
                    GetServv();
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
                //   pDialog.dismiss();
                prgbar.setVisibility(View.GONE);
                try {
                    if ((prgDialog2 != null) && prgDialog2.isShowing()) {
                        prgDialog2.dismiss();
                    }
                } catch (final IllegalArgumentException e) {
                    // Handle or log or ignore
                } catch (final Exception e) {
                    // Handle or log or ignore
                } finally {
                    prgDialog2 = null;
                }
                if(session.getString(SessionManagement.KEY_SETBANKS).equals("N")) {
                    GetServv();
                }
                prgDialog2.dismiss();
            }
        });

    }
    /* public void getAgentIDs(){




         ApiInterface apiService =
                 ApiClient.getClient().create(ApiInterface.class);
         String usid = Utility.gettUtilUserId(getActivity());
         String agentid = Utility.gettUtilAgentId(getActivity());
         String mobnoo = Utility.gettUtilMobno(getActivity());
         Call<GetAgentId> call = apiService.GetAgId("1", usid, agentid, "9493818389");
         call.enqueue(new Callback<GetAgentId>() {
             @Override
             public void onResponse(Call<GetAgentId> call, Response<GetAgentId> response) {
                 String responsemessage = response.body().getMessage();

                 SecurityLayer.Log("Response Message", responsemessage);

                 plan = response.body().getData();
 //                                    SecurityLayer.Log("Respnse getResults",datas.toString());
                 if (!(plan == null)) {
                     for(int sw = 0;sw < plan.size();sw++) {
                         if (!(plan.get(sw) == null)) {
                             String imgloc = plan.get(sw).getimgLoc();
                             if (imgloc.equals("FOOTER")) {
                                 agid = plan.get(sw).getId();
                                 new DownloadImg().execute("");
                             }
                         }else{
                             if(!(getActivity() == null)) {
                                 Picasso.with(getActivity()).load(R.drawable.fbankdebitcard).into(iv);
                                 prgbar.setVisibility(View.GONE);
                             }
                         }
                     }
                 }
                 else{
                     Toast.makeText(
                             getActivity(),
                             "There was an error loading ad image",
                             Toast.LENGTH_LONG).show();
                 }
            //     pDialog.setVisibility(View.INVISIBLE);
             }

             @Override
             public void onFailure(Call<GetAgentId> call, Throwable t) {
                 // Log error here since request failed
                 SecurityLayer.Log("Throwable error",t.toString());
                 Toast.makeText(
                         getActivity(),
                         "There was an error processing your request",
                         Toast.LENGTH_LONG).show();
             //    pDialog.setVisibility(View.INVISIBLE);
             }
         });

     }
 */




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
                    //    prgDialog2.dismiss();
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


                    //   prgDialog2.dismiss();
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

                                        String minversion = servdata.optString("minVersion");
                                        SecurityLayer.Log("Min version", minversion);
                                        double dbvers = Double.parseDouble(minversion);
                                        String currvers = Utility.getAppVersion(getActivity());
                                        double dbcurrvers = Double.parseDouble(currvers);
                                        if( dbcurrvers < dbvers) {
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
