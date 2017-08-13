package com.way.util;

import android.content.Context;

import com.way.tabui.commonmodule.GosDeploy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AssetsUtils {

	public static void assetsDataToSD(String fileOutPutName,
                                      String fileInPutName, Context context) throws IOException {
		InputStream myInput;
		File file = new File(fileOutPutName);
		/*if (!file.exists()) {
		    file.createNewFile();
		   }else {
			return;
		}*/
		OutputStream myOutput = new FileOutputStream(fileOutPutName);
		myInput = context.getAssets().open(fileInPutName);
		byte[] buffer = new byte[1024];
		int length = myInput.read(buffer);
		while (length > 0) {
			myOutput.write(buffer, 0, length);
			length = myInput.read(buffer);
		}

		myOutput.flush();
		myInput.close();
		myOutput.close();
	}
	
	
	
	public static void saveFile(String str) {
		String filePath = null;
		
		
		filePath = GosDeploy.fileOutName;
		try {
			if(filePath!=null){
				File file = new File(filePath);
				if (!file.exists()) {
					File dir = new File(file.getParent());
					dir.mkdirs();
					file.createNewFile();
				}
				FileOutputStream outStream = new FileOutputStream(file);
				outStream.write(str.getBytes());
				outStream.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
}
	
}
