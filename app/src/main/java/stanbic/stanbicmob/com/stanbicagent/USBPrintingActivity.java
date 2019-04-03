package stanbic.stanbicmob.com.stanbicagent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import printer.PrintOrder;

public class USBPrintingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usbprinting);
        String msg = "This is a test message";
        PrintOrder printer = new PrintOrder();
        printer.Print(this,msg);
    }
}
