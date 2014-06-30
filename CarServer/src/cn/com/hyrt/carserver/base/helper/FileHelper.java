package cn.com.hyrt.carserver.base.helper;

import java.io.File;

import android.os.Environment;

public class FileHelper {
	
	public static File createFile( String folderPath, String fileName )
	{
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
}
