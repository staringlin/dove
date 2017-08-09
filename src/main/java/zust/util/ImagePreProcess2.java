package zust.util;

import java.awt.Color;  
import java.awt.image.BufferedImage;  
import java.io.File;  
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;  
import java.io.OutputStream;  
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
  
import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;

import zust.Config.APP;  
public class ImagePreProcess2 {  
	
   
    public static String downloadImage() throws IOException { 
    	
    	HttpResponse response = HttpUtils.getRawHtml(APP.code_url);
    	int StatusCode = response.getStatusLine().getStatusCode();
    	if(StatusCode == 200){
    		String sotreName = APP.CODE_SAVE_PATH+"verification_code.jpg";
    		HttpEntity entity = response.getEntity();
    		byte[] data = EntityUtils.toByteArray(entity);
    		File storePath = new File(sotreName);
    		FileOutputStream fos = new FileOutputStream(storePath);
    		fos.write(data);
    		fos.flush();
    		fos.close();
    		return sotreName;
    	}
    	return null;
    	

    }  
  
   
   
}