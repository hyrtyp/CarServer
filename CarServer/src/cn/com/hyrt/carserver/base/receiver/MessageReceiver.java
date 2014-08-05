package cn.com.hyrt.carserver.base.receiver;

import com.pipework.push.IIIService;

import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MessageReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action != null && action.equals(IIIService.MESSAGE_ACTION)) {
			String message = intent.getStringExtra("message");
			LogHelper.i("tag", "message:"+message);
			AlertHelper.getInstance(context).showCenterToast("msg:"+message);
		}
	}

}
