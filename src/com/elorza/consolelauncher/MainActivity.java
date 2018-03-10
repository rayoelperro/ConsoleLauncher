package com.elorza.consolelauncher;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public final class MainActivity extends Activity {

	EditText Multi;
	MultiEditor Edit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		Multi = (EditText)findViewById(R.id.multi);
		Edit = new MultiEditor(Multi);
	}
	
	@Override
	public void onBackPressed(){
		
	}
	
}
