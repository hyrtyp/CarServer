package cn.com.hyrt.carserver.base.helper;

import cn.com.hyrt.carserver.base.baseFunction.Define;

/**
 * http请求助手
 * @author zoe
 *
 */
public class HttpHelper extends BaseHttpHelper{

	public HttpHelper(RequestCallback<?> mCallback) {
		super(mCallback);
		// TODO Auto-generated constructor stub
	}
	
	public void getUpdate(){
		String url = "http://api.chinaxueqian.com/syn/update";
		get(url, Define.TEST.class);
	}

}
