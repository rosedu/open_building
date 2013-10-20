package com.openbuilding;





import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class DialogAct extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog);
	}

	

	
	public void Done(View v)
	{ 
	
		EditText ed1=(EditText)findViewById(R.id.editText1);
		
		Intent data= new Intent();
		data.putExtra("text", ed1.getText().toString());
		setResult(1, data);
	
	    finish();
		
	}
	
	public void Cancel(View v)
	{setResult(0);
		finish();}
	
}