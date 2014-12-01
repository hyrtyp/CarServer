package cn.com.hyrt.carserverseller.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.LogHelper;

import com.pipework.push.IIIService;

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
