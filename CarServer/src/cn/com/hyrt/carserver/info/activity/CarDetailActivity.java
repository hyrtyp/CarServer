package cn.com.hyrt.carserver.info.activity;

import java.io.ByteArrayOutputStream;

import org.kobjects.base64.Base64;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.helper.FileHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.PhotoHelper;

public class CarDetailActivity extends BaseActivity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_detail);
	}
}
