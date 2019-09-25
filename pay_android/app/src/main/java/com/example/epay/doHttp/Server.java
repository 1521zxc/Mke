package com.example.epay.doHttp;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;

import com.example.epay.base.BaseActivity;
import com.example.epay.view.EPayProgressDialog;


public abstract class Server extends AsyncTask<String, Integer, Integer> {
	private Activity frameActivity;
	private Dialog progressDialog;
	private String prompt;
	
	public Server(Activity activity, String prompt){
		super();
		
		this.frameActivity = activity;
		this.prompt = prompt;
	}

	//Post-execution method after `doInBackground`
	protected void onPostExecute(Integer result){
		if(prompt != null) {
			if (progressDialog != null && progressDialog.isShowing() && frameActivity != null && !((BaseActivity) frameActivity).isFinishing()) {
				progressDialog.dismiss();
			}
		}
	}

	//Pre-execution method
	protected void onPreExecute (){
		if(prompt != null){
			progressDialog = EPayProgressDialog.createLoadingDialog(frameActivity);
			progressDialog.show();
		}

	}


}

