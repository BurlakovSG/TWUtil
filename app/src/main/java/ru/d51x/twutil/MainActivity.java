package ru.d51x.twutil;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;

import android.tw.john.TWUtil;


public class MainActivity extends ActionBarActivity {

    private static Handler mHandler;

    private EditText id_edit_what;
    private EditText id_edit_arg1;
    private EditText id_edit_arg2;
    private Button id_button_sendcommand;
    private TextView id_textView_Result;

    private TWUtil mTW;

    public MainActivity() {
        this.mTW = null;
    }

    static { //  !!!! what is this????
        mHandler = new Handler() {
            public void handleMessage(Message paramMessage) {
                try {
                    Log.e("AutoSettings1", String.valueOf(paramMessage.obj));
                    Log.e("AutoSettings2", String.valueOf(paramMessage.what));
                    Log.e("AutoSettings3", String.valueOf(paramMessage.arg1));
                } catch (Exception localException) {
                    Log.e("AutoSettingsERR", Log.getStackTraceString(localException));
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.id_edit_what = (EditText) findViewById(R.id.id_edit_what);
        this.id_edit_arg1 = (EditText) findViewById(R.id.id_edit_arg1);
        this.id_edit_arg2 = (EditText) findViewById(R.id.id_edit_arg2);

        this.id_button_sendcommand = (Button) findViewById(R.id.id_button_sendcommand);
        this.id_textView_Result = (TextView) findViewById(R.id.id_textView_Result);





        this.id_button_sendcommand.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) // клик на кнопку
            {
                mTW = TWSetting.open();
                mTW.addHandler("AutoSettings", mHandler);
                if (mTW != null) {
                    Log.e("Log", "OK: TWUtils: initialized successfully");

                    Log.e("Log", "Edittext (what): " + String.valueOf(id_edit_what.getText().toString()));
                    Log.e("Log", "Edittext (arg1): " + String.valueOf(id_edit_arg1.getText().toString()));
                    Log.e("Log", "Edittext (arg2): " + String.valueOf(id_edit_arg2.getText().toString()));

                    try {
                        int what = Integer.parseInt(id_edit_what.getText().toString());
                        int arg1 = Integer.parseInt(id_edit_arg1.getText().toString());
                        int arg2 = Integer.parseInt(id_edit_arg2.getText().toString());
                        Log.e("Log", "Button click: " + String.valueOf(what));
                        Log.e("Log", "Button click: " + String.valueOf(arg1));
                        Log.e("Log", "Button click: " + String.valueOf(arg2));
                        int res = mTW.write(what, arg1, arg2);
                        id_textView_Result.setText(String.valueOf(res));
                        // close session
                        mTW.write(266, 255, 0); /// !!! зачем это???
                    } catch (NumberFormatException e) {
                        // введено не число

                    }
                } else {
                    Log.e("Log", "ERROR: TWUtils: not initialized");
                }

                mTW.removeHandler("AutoSettings");
                mTW.close();
                mTW = null;
            }
        });
    }

    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
