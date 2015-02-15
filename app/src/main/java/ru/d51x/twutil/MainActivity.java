package ru.d51x.twutil;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.Date;
import java.text.SimpleDateFormat;

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

	private EditText id_edittext_handler;
	private Button id_button_handler;
	private TextView id_textview_log;

    private TWUtil mTW, mTW2;
	public boolean isHandlerStarted;

    public MainActivity() {
        this.mTW = null;
	    this.mTW2 = null;
	    this.isHandlerStarted = false;
    }

	static {


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

		this.id_edittext_handler = (EditText) findViewById(R.id.id_edittext_handler);
	    this.id_button_handler = (Button) findViewById (R.id.id_button_handler);
		this.id_textview_log = (TextView) findViewById (R.id.id_textview_log);

        this.id_button_sendcommand.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) // клик на кнопку
            {
	            int what = Integer.parseInt (id_edit_what.getText ().toString ());
	            int arg1 = Integer.parseInt (id_edit_arg1.getText ().toString ());
	            int arg2 = Integer.parseInt (id_edit_arg2.getText ().toString ());
	            //short[] Shorts = new short[] {(short) what};

	            mTW = new TWUtil ();
	            if (mTW.open (new short[]{(short) what}) == 0) {
		            mTW.start ();

		            int res = mTW.write (what, arg1, arg2);
		            id_textView_Result.setText (String.valueOf (res));
		            // close session
		            mTW.write (what, 255, 0); /// !!! зачем это???
	            }
	            mTW.close ();
	            mTW = null;

            }
        });

	    this.id_button_handler.setOnClickListener(new Button.OnClickListener () {
		    public void onClick (View v) // клик на кнопку
		    {
			    final int xwhat = Integer.parseInt (id_edittext_handler.getText ().toString ());
			    if (isHandlerStarted) {
				    // stop handler
				    // stop and close TWUtil
				    mTW2.removeHandler ("TWUtilHandler");
				    mTW2.stop ();
				    mTW2.close ();
				    mTW2 = null;
				    isHandlerStarted = false;
				    id_button_handler.setText ("Start Handler");
			    } else {
				    // open and start TWUtil
				    mTW2 = new TWUtil ();
				    if (mTW2.open (new short[]{(short) xwhat}) == 0) {
					    mTW2.start ();
					    mTW2.addHandler ("TWUtilHandler", new Handler () {
						    public void handleMessage(Message msg) {
							    try {
								    if (msg.what == xwhat ) {
										    Date date = new Date();
										    SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MMdd. hh:mm:ss");
											String str = String.format("%s:  Message: what: %i   arg1: %i  arg2: %i\n", ft.format(date), msg.what, msg.arg1, msg.arg2);
										    id_textview_log.append ( str );
								    }
							    } catch (Exception e) {
							    }
						    }
						});
					    mTW2.write (xwhat, 255);
						isHandlerStarted = true;
					    id_button_handler.setText ("Stop Handler");
				    }
				    // start handler

			    }
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
