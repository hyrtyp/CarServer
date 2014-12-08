package cn.com.hyrt.carserversurvey.base.helper;

import java.io.File;

import cn.com.hyrt.carserversurvey.R;

import android.os.Environment;

public class FileHelper {
	
	/*public static File createFile(String fileName )
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
	}*/
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
	
	public static boolean sdCardExist(){
		return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	
	/*if (FileHelper.sdCardExist()) {
		
	}else{
		AlertHelper.getInstance(getApplicationContext()).showCenterToast(getString(R.string.no_sdcard));
	}*/
}
