package cn.com.hyrt.carserver.base.helper;

import java.io.File;

import android.os.Environment;

public class FileHelper {
	
public static File createFile(String fileName )
	{
		String folderPath = "carserver";
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            folderPath = Environment.getExternalStorageDirectory() + File.separator + folderPath + File.separator;
            File destDir = new File(folderPath);
            if (!destDir.exists())
            {
                destDir.mkdirs();
            }
            return new File(folderPath,  fileName);
        }else{
            return null;
        }
	}
	
	public static boolean sdCardExist(){
		return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	
	public static File createFile1(String fileName )
	{
		String folderPath = "carserver";
            folderPath = Environment.getExternalStorageDirectory() + File.separator + folderPath + File.separator;
            File destDir = new File(folderPath);
            if (!destDir.exists())
            {
                destDir.mkdirs();
            }
            return new File(folderPath,  fileName);
	}
	
	/*if (FileHelper.sdCardExist()) {
		
	}else{
		AlertHelper.getInstance(getApplicationContext()).showCenterToast("sd卡不存在");
	}*/
	
	
}
