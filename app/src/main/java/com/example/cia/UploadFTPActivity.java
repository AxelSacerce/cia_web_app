package com.example.cia;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.util.StringTokenizer;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.io.CopyStreamAdapter;
import org.apache.commons.net.io.CopyStreamEvent;
import com.example.cia.work.Network;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;






@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
public class UploadFTPActivity extends Activity 
{
	private TextView tv;
	private ImageButton btn,btn2;
	private int cantidad;
	WebView webView;
	String onLoad = "";
	File filee;
	FTPClient ftpClient;
	String RedirecTo = "";
	String LoadRedirecto = "";
	
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
       public void onCreate(Bundle savedInstanceState)
       {		
              super.onCreate(savedInstanceState);
              
              StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
          	  StrictMode.setThreadPolicy(policy);
              
              setContentView(R.layout.upload);
              tv =  (TextView)findViewById(R.id.textView1);
              btn2 = (ImageButton) findViewById(R.id.imageButton1);
              btn = (ImageButton) findViewById(R.id.imageButton2);
              webView = (WebView) findViewById(R.id.webView1);
              
              RedirecTo = getIntent().getStringExtra("redirccion");
              //+RedirecTo
              LoadRedirecto ="http://www.chispudo.com:8000/cia/webapp/main.php?"+RedirecTo;

              // Activo JavaScript
              webView.getSettings().setJavaScriptEnabled(true);
              
              webView.setWebViewClient(new MyCustomWebViewClient());
              // Cargamos la url que necesitamos    
              
              webView = (WebView) findViewById(R.id.webView1);
      		
      		  WebSettings webSettings = webView.getSettings();
              webSettings.setJavaScriptEnabled(true);


              webView.setWebViewClient(new MyCustomWebViewClient());
              
              //PCID
              webView.setWebChromeClient(new WebChromeClient() {
              	 public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
              	    callback.invoke(origin, true, false);
              	 }
              	});
              // FIN PCID
              webView.loadUrl("http://www.chispudo.com:8000/cia/webapp");
              //myWebView.loadUrl("http://www.twitter.com");
              
              webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
      		
              webView.setVisibility(View.GONE);
              
              String path = Environment.getExternalStorageDirectory().toString()+"/DCIM/CIA";
              File f = new File(path);        
              File file[] = f.listFiles();
              
              if(file.length > 0){
            	  btn2.setImageResource(R.drawable.ic_button_upload);
              }else{
            	  btn2.setImageResource(R.drawable.ic_button_upload_bn);
              }
              
              tv.setText(file.length + " archivos por procesar");
              
              btn.setOnClickListener(new OnClickListener() 
      		  {
      			public void onClick(View v) 
      			{
      				//onBackPressed();
      				Intent redir = new Intent(UploadFTPActivity.this, MainActivity.class);
      				redir.putExtra("redirec", LoadRedirecto);
      				startActivity(redir);
      				finish();      				
      				
      			}
      		  });
              
              btn2.setOnClickListener(new OnClickListener() 
      		  {
      			public void onClick(View v) 
      			{
      				btn2.setImageResource(R.drawable.ic_button_upload_bn);
      				btn2.setEnabled(false);
      				String path = Environment.getExternalStorageDirectory().toString()+"/DCIM/CIA";
                    File f = new File(path);        
                    File file[] = f.listFiles();
                    
      				if(file.length>0)
      				{
      					upload upload = new upload();
		                upload.execute();
      				}
		            else
		            {
		            
		            	btn2.setImageResource(R.drawable.ic_button_upload_bn);
		            	btn2.setEnabled(false);
		            	Toast toast = Toast.makeText(getApplicationContext(),"No hay archivos por procesar", Toast.LENGTH_SHORT);
                		toast.show();
      				}
      				
      			}
      		  });
       }

  //ASYNC TASK Upload Class which uploads the file and shows the progress via a dialog
    public class upload extends AsyncTask<Object, Integer, Void> 
    {
    	 //Initializations
    	 public ProgressDialog dialog;
 		 int progress=0;
 	     int total;
 	     int last=0;
 	     
 	    //Before start to upload the file creating a dialog
        @Override
        public void onPreExecute() 
        {
        	filee = new File(Environment.getExternalStorageDirectory().toString()+"/DCIM/CIA/031_TP_EVE_38_20142307_102359.jpg"); 
			
        	 dialog = new ProgressDialog(UploadFTPActivity.this);
             dialog.setMessage("Subiendo....");
             dialog.setIndeterminate(false);
            // dialog.setTitle("Subiendo archivos al servidor");
             dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
             dialog.setProgress(0);
             dialog.setMax(100);
             dialog.show();      
             dialog.setCancelable(false);
             
             	

             
          
        }
        
        //Uploading the file in background with showing the progress
		@Override
		public Void doInBackground(Object... arg0) 
		{	
			ftpClient = null;
			filee = new File(Environment.getExternalStorageDirectory().toString()+"/DCIM/CIA/031_TP_EVE_38_20142307_102359.jpg"); 
			// TODO Auto-generated method stub
    	    try
    	    {
    	    	ftpClient = new FTPClient();
    	    	
    	    	ftpClient.connect("chispudo.com");
    	    	ftpClient.login("ciaphoto", "ciaphoto0.");
    	    	ftpClient.changeWorkingDirectory("/AndroidApp");
    	    	ftpClient.enterLocalPassiveMode();
                File folder = new File(Environment.getExternalStorageDirectory().toString()+"/DCIM/CIA");        
                File files[] = folder.listFiles();

                int counter=0;
                total = files.length;
                
                String ext[] = {"txt", "jpg"};
                
                 
        
                /// Inicia For Each
                 for(String extension : ext)
                 {
			                for (int i=0; i < files.length; i++)
			                {
			                	if(files[i].isFile())
			                	{	
			                		
			                		filee = new File(Environment.getExternalStorageDirectory().toString()+"/DCIM/CIA/"+files[i].getName());
			                		FileInputStream in=new FileInputStream(filee);
			    	    	    	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			    	    	    	StringTokenizer st = new StringTokenizer(filee.getName(),".");
			    	    	    	String name = st.nextToken();
			    	    	    	boolean uploaded = ftpClient.storeFile(name+".upl", in);
			    	    	    	//Log.i("PASA POR", name);
			    	    	    	
			    	    	    	try {
			    	    	    		  InputStream stO = new BufferedInputStream(ftpClient.retrieveFileStream("foo.bar"),
			    	    	    		                ftpClient.getBufferSize());
			    	    	    		  OutputStream stD = new FileOutputStream("bar.foo");
			    	
			    	    	    		  org.apache.commons.net.io.Util.copyStream(stO, stD, ftpClient.getBufferSize(),
			    	    	    		                CopyStreamEvent.UNKNOWN_STREAM_SIZE,
			    	    	    		                new CopyStreamAdapter() 
			    	    	    		                {
			    	    	    		                    public void bytesTransferred(long totalBytesTransferred,
			    	    	    		                            int bytesTransferred,
			    	    	    		                            long streamSize) 
			    	    	    		                    {
			    	    	    		                            
			    	    	    		                    	    publishProgress((int)((totalBytesTransferred*100)/(filee.length()) ) );
			    	    	    		                    }
			    	    	    		               });
			    	    	    		  ftpClient.completePendingCommand();
			    	    	    		} catch (Exception e) { 
			    	    	    			
			    	    	    			//publishProgress("45");
			    	    	    		}
			    	    	    	if(uploaded)
			    	    	    	{
			    	    	    		
			    	    	    		if(filee.getName().contains(extension)){
			    	    	    		
			    	    	    		
					    	    	    		ftpClient.rename(name+".upl",filee.getName());
					        	    	    	counter++;
					        	    	    	publishProgress(counter);
					        	    	    	files[i].delete();
					        	    	    	StringTokenizer ultimo = new StringTokenizer(files[i].getName(),".");
					        	    	    	String type = "";
					        	    	    	String nameTUp = ultimo.nextToken().toString();
					        	    	    	type = ultimo.nextToken().toString();
					                    		
					        	    	    	String Result= "";
					        	    	    	
					        	    	    	if (extension.equals("jpg"))
					        	    	    	{
					        	    	    			type = "JPG";
					        	    	    		
					        	    	    		Result = Network.CargarFoto(nameTUp,type);
					        	    	    	}
					        	    	    	
					                    		if(Result != null){
					                    					
					               					Log.i("EXTENSIÓN: ", " OK:  "+Result);
					               							
					                    		}else{
					
					                    			Log.i("El archivo txt: ", nameTUp+extension+" no se pudo procesar correctamente");
					                    		}
					             		
			    	    	    		}
			    	    	    	}
			    	    	    	else
			    	    	    	{
			    	    	    		publishProgress(counter);
			    	    	    	}
			    	    	    	
			    	    	    	
			    	    	    	
			                	}
			                }
                 }
                // termina Foreach
                
    	    		
                publishProgress(total);
    	    	ftpClient.logout();
    	    	
    	    }
    	    
    	    catch (Exception ex)
    	    {
    	        ex.printStackTrace();
    	    }    
			return null;
		 }//end of doInBackground method
		
		 //making an update on progress
		 @Override
		 public void onProgressUpdate(Integer... values)
		 	{
			 	super.onProgressUpdate(values);
			    int num = ((int)((values[0]*100)/(total) ) );
			    dialog.setProgress(num);
		        if(last==num)
		        	dialog.setMessage("Error al procesar el último archivo. \nSe han procesado "+values[0]+" archivos de "+total);
		        else
		        {
		        	dialog.setMessage("Procesados:  "+values[0]+" de "+total + " archivos");
		        	last++;
		        }
		        tv.setText("Procesados: "+values[0]+"  de "+total + " archivos");
		    }//end of onProgressUpdate
		 
		 //After finishing the progress the dialog will disappear!
		 @Override
		 public void onPostExecute(Void result) 
		 { 
			 dialog.dismiss();			 
			 
			 String path = Environment.getExternalStorageDirectory().toString()+"/DCIM/CIA";
             File f = new File(path);        
             File file[] = f.listFiles();
             
             if(file.length > 0){
	             tv.setText(file.length + " archivos por procesar");
	             btn2.callOnClick();
             }else{
				 
				 //btn2.setClickable(false);
				 tv.setText("Se han procesado todas las fotos"); 
			 
             }
	     }//end of onPostExecute method
    }// end of asyncTask class 
    
       
	
	private Bitmap decodeFile(File f) {
	    try {

	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(f),null,o);

	        final int REQUIRED_SIZE=450;

	        int scale=1;
	        while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
	            scale*=2;

	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize=scale;
	        Bitmap bit1 = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	        return bit1;
	    } catch (FileNotFoundException e) {}
	    return null;
	}
	
	private void ImageResizer(Bitmap bitmap) {
	    String root = Environment.getExternalStorageDirectory().toString();
	    File myDir = new File(root + "/Pic");    
	    if(!myDir.exists()) myDir.mkdirs();
	    String fname = "resized.jpg";
	    File file = new File (myDir, fname);
	    if (file.exists()){
	        file.delete();
	        SaveResized(file, bitmap);
	    } else {
	        SaveResized(file, bitmap);
	    }
	}

	private void SaveResized(File file, Bitmap bitmap) {
	    try {
	           FileOutputStream out = new FileOutputStream(file);
	           bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
	           out.flush();
	           out.close();
	    } catch (Exception e) {
	           e.printStackTrace();
	    }
	}
	

	private class MyCustomWebViewClient extends WebViewClient
	{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) 
        {
        	
        	//if(url.contains("http://www.chispudo.com:8000/cia/webapp") == true)  	 
        	if(url.contains("http://www.chispudo.com:8000/cia/webapp") == true) 
        		view.loadUrl(url);
      	    return true;      	
        }
        
        @Override
        public void onPageFinished(WebView view, String url)
        { 	
            /* This call inject JavaScript into the page which just finished loading. */
            view.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
        }
        
      //Metodo para leer el html
        @SuppressWarnings("unused")
    	private String readFromFile(String file) 
    	{
            String ret = "";     
            try 
            {
                InputStream inputStream = openFileInput(file);
                 
                if ( inputStream != null ) 
                {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();
                     
                    while ( (receiveString = bufferedReader.readLine()) != null ) 
                        stringBuilder.append(receiveString);
                     
                    inputStream.close();
                    ret = stringBuilder.toString();
                }
            }
            catch (FileNotFoundException e) 
            {
            	Toast toast = Toast.makeText(getApplicationContext(),"No se ha podido realizar el proceso, no se ha encontrado archivo txt", Toast.LENGTH_SHORT);
    	    	toast.show();
            } 
            catch (IOException e) 
            {
            	Toast toast = Toast.makeText(getApplicationContext(),"No se ha podido realizar el proceso, no se ha leido el archivo txt", Toast.LENGTH_SHORT);
    	    	toast.show();
            }
     
            return ret;
        }
    	
    }
}