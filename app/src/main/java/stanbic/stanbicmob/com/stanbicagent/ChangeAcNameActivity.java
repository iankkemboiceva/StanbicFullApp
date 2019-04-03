package stanbic.stanbicmob.com.stanbicagent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import adapter.AccountList;
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ChangeAcNameActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;

    TextView agentname,agemail,agphonenumb;

    CheckBox chkb,chkus,chkast,chktpin,chkbal;

    String numb;
    boolean initdisp = false;

    String upLoadServerUri = null;

    private final int CAMERA_RESULT = 1;

    private static String filePath;
    private static final String IMAGE_DIRECTORY_NAME = Constant.ROOT_FOLDER_NAME;
    private Bitmap photoBitmap;
    RelativeLayout rlem,rlid,rlno,rllast;
    CardView cvlast;
    LinearLayout lyf;
    SessionManagement session;


    List<AccountList> planetsList = new ArrayList<AccountList>();


    int serverResponseCode = 0;
    public  String acc,defac;
    String uploadFilePath = null;
    String uploadFileName = null;
    private String image;
    RecyclerView lv,lv2,lv3;
    Button myact,chglgpin;
    RadioButton grid,list;
    ImageView ivgrid,ivlist;
    CircleImageView iv;
    RelativeLayout rlbutton;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_acname);

        session = new SessionManagement(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        lv = (RecyclerView) findViewById(R.id.listView1);
        cvlast = (CardView) findViewById(R.id.card_view023);
        cvlast.setOnClickListener(this);

        lv2 = (RecyclerView)findViewById(R.id.listView2);

        rlbutton = (RelativeLayout) findViewById(R.id.rlbutton);
        rlbutton.setOnClickListener(this);
        lv3 = (RecyclerView) findViewById(R.id.listView3);

        ivgrid = (ImageView) findViewById(R.id.ivgrid);
        ivlist = (ImageView) findViewById(R.id.ivlist);
        ivgrid.setOnClickListener(this);
        ivlist.setOnClickListener(this);
       /* grid = (RadioButton) rootView.findViewById(R.id.grid);
        list = (RadioButton) rootView.findViewById(R.id.list);
        String vtype = session.getString("VTYPE");
        if( vtype == null){
           grid.setChecked(true);
        }else {
            if (vtype.equals("N") || vtype.equals("grid")) {
                grid.setChecked(true);
            }
            if (vtype.equals("list")) {
                list.setChecked(true);
            }
        }
        grid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(grid.isChecked()){
//Utility.setUtilView(getActivity(),"grid");
                    session.setString("VTYPE","grid");
                }
            }
        });
        list.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(list.isChecked()){
                   // Utility.setUtilView(getActivity(),"list");
                    session.setString("VTYPE","list");
                }
            }
        });*/
        chkb = (CheckBox) findViewById(R.id.biochk);
        chkb.setOnClickListener(this);



        iv = (CircleImageView) findViewById(R.id.profile_image);
        //  iv.setImageBitmap(null);
        iv.setOnClickListener(this);
        //loadImage();

        agentname = (TextView)findViewById(R.id.textViewnb2);
        agemail = (TextView) findViewById(R.id.textViewrrs);
        agphonenumb = (TextView) findViewById(R.id.textViewcvv);

        String txtphonenumb = Utility.gettUtilMobno(getApplicationContext());
        String txtagname =   Utility.gettUtilCustname(getApplicationContext());
        String txtemail =   Utility.gettUtilEmail(getApplicationContext());
        agphonenumb.setText(txtphonenumb);
        agemail.setText(txtemail);
        agentname.setText(txtagname);

        myact = (Button) findViewById(R.id.tdispedit);
        chglgpin = (Button) findViewById(R.id.button10);


        session = new SessionManagement(getApplicationContext());


        uploadFilePath = Environment.getExternalStorageDirectory() + File.separator + "cache" + File.separator;
        // uploadFileName = numb;
        uploadFileName = numb;

        uploadFilePath = Environment.getExternalStorageDirectory() + File.separator + "req_images" + File.separator;




        upLoadServerUri = "";



    }


    public void StartChartAct(int i){
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



        if(v.getId() == R.id.rlbutton){
            /*Fragment fragment = new ChangePin();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,"Change Pin");
            fragmentTransaction.addToBackStack("Change Pin");
            fragmentTransaction.commit();
            ((FMobActivity)getActivity())
                    .setActionBarTitle("Change Pin");*/

            startActivity(new Intent(getApplicationContext(), ChangePinActivity.class));

        }

        if(v.getId() == R.id.homepagead){
            if(chkast.isChecked()){
                session.setAst();

            }else{
                session.UnSetAst();
            }

            Toast.makeText(
                    getApplicationContext(),
                    "Settings Applied Successfully",
                    Toast.LENGTH_LONG).show();

        }
        if(v.getId() == R.id.shwbal){
            if(chkbal.isChecked()){
                session.UnSetShwBal();

            }else{
                session.setShwbal();
            }

            Toast.makeText(
                    getApplicationContext(),
                    "Settings Applied Successfully",
                    Toast.LENGTH_LONG).show();

        }
        if(v.getId() == R.id.distpin){
            if(chktpin.isChecked()){
                session.setTpinPref();
            }else{
                session.UnSetTpinPref();
            }

            Toast.makeText(
                    getApplicationContext(),
                    "Settings Applied Successfully",
                    Toast.LENGTH_LONG).show();

        }

        if(v.getId() == R.id.chkus){
            if(chkus.isChecked()){
                session.setUser();
            }else{
                session.UnSetUser();
            }

            Toast.makeText(
                    getApplicationContext(),
                    "Settings Applied Successfully",
                    Toast.LENGTH_LONG).show();

        }

        if(v.getId() == R.id.ivgrid){
            session.setString("VTYPE","grid");
            finish();


            // After logout redirect user to Loing Activity
            Intent i = new Intent(getApplicationContext(), FMobActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Staring Login Activity
            startActivity(i);
        }
        if(v.getId() == R.id.ivlist){
            session.setString("VTYPE","list");
            finish();


            Intent i = new Intent(getApplicationContext(), FMobActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Staring Login Activity
            startActivity(i);
        }
        if(v.getId() == R.id.tdispedit){



        }


    }

    public void SetDialog(String msg,String title){
        new MaterialDialog.Builder(this)
                .title(title)
                .content(msg)

                .negativeText("Close")
                .show();
    }
}
