package com.example.cia.work;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class Network {

	public static String CargarFoto(String FileName, String TypeFile)
	{
		String result="";
		ArrayList<NameValuePair> params;
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("fileuploaded",FileName));
		params.add(new BasicNameValuePair("filetype",TypeFile));
		result = GETData("http://www.chispudo.com:8000/cia/webapp/xfile.php?",params);
		return result;
		
	}
	
	
	private static String GETData(String url, ArrayList<NameValuePair> params) {
		String datos="";
		String linea;
		HttpContext mHttpContext = new BasicHttpContext();
		DefaultHttpClient mHttpClient = new DefaultHttpClient();
		HttpGet mHttpGet = null;
		if (params!= null) {
			mHttpGet = new HttpGet(url+"&"+ URLEncodedUtils.format(params, "utf-8"));
			
		}else{
			mHttpGet = new HttpGet(url);
		}
		try {
			BasicHttpResponse response = (BasicHttpResponse) mHttpClient.execute(mHttpGet,mHttpContext);
			InputStream is = response.getEntity().getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while((linea = br.readLine())!=null) {
				datos += linea;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datos;
	}
	

}
