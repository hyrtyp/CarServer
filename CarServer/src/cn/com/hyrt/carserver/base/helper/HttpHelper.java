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
	
	public HttpHelper() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void commentSucess(String url){
		get(url, Define.BASE.class);
	}
}
