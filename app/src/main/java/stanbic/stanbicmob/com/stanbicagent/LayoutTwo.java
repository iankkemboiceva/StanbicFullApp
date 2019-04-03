package stanbic.stanbicmob.com.stanbicagent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class LayoutTwo extends Fragment implements View.OnClickListener {
    ImageView imageView1;
    ListView lv;
    TextView tv;

    public LayoutTwo() {
        // Required empty public constructor
    }
  /*  private static Fragment newInstance(Context context) {
        LayoutOne f = new LayoutOne();

        return f;
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.productsfrag, null);

        lv = (ListView) root.findViewById(R.id.lv);
        tv = (TextView) root.findViewById(R.id.txt);
        tv.setOnClickListener(this);




        return root;
    }


    public void StartChartAct(int i){


    }
    @Override
    public void onResume(){
        super.onResume();
        // put your code here...

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.txt){
         startActivity(new Intent(getActivity(),AddProdActivity.class));
        }
    }
}
