package cn.com.hyrt.carserver.base.helper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.com.hyrt.carserver.base.baseFunction.StreamTool;

/**
 * 获取网络图片帮助类
 * @author gsl
 *
 */
public class GetWebImageHelper {

	      
	    public static byte[] getImage(String path) throws IOException {  
	        URL url = new URL(path);  
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	        conn.setRequestMethod("GET");   //设置请求方法为GET  
	        conn.setReadTimeout(5*1000);    //设置请求过时时间为5秒  
	        InputStream inputStream = conn.getInputStream();   //通过输入流获得图片数据  
	        byte[] data = StreamTool.readInputStream(inputStream);     //获得图片的二进制数据  
	        return data;  
	}
 
}
