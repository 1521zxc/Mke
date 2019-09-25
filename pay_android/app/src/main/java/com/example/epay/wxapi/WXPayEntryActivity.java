package com.example.epay.wxapi;






import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	@Override
	public void onReq(BaseReq baseReq) {
		Toast.makeText(this, "openid = " + baseReq.openId, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResp(BaseResp baseResp) {

	}

	@Override
	public void onPointerCaptureChanged(boolean hasCapture) {

	}
}