package com.openbuilding;

import android.app.Activity;
import android.os.Bundle;

public class UselessActivity extends Activity {
     //solves impossible drawing bug
	//this way we force the operating system to call onPause,OnStop,etc
	//we do this because android is buggy and is drawing the views in the wrong order
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_useless);
		finish();
	}

	

}
