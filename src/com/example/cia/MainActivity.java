package com.example.cia;




import java.io.BufferedReader;
import android.provider.Settings.Secure;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//PCID
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
// FIN PCID
@SuppressLint({ "SetJavaScriptEnabled", "DefaultLocale" }) 
public class MainActivity extends Activity {

	
	Button button;
	WebView myWebView,webViewOculto;
	TextView tv;
	boolean onRestart=false;
	String session="";
	String getURL ="";
	static String DateTyme;
	String uriRedirect ="";
	String RedirecTo="";
	String photoPath = "";
	String pathText = "";
	String name="";
	String NameFile= "";
	String NameFileLink = "";

	public static String fechaHoraActual(){
		   return new SimpleDateFormat( "yyyyMMdd_HHmmss", java.util.Locale.getDefault()).format(Calendar.getInstance() .getTime());
		}
	
	// verifica si existe conexion a internet
	 public static boolean verificaConexion(Context ctx) {
		    boolean bConectado = false;
		    ConnectivityManager connec = (ConnectivityManager) ctx
		            .getSystemService(Context.CONNECTIVITY_SERVICE);
		    // No sólo wifi, también GPRS
		    NetworkInfo[] redes = connec.getAllNetworkInfo();

		    for (int i = 0; i < redes.length; i++) {
		        // ¿Tenemos conexión? ponemos a true
		        if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
		            bConectado = true;
		        }
		    }
		    return bConectado;
		}
	
	
	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String android_id = Secure.getString(getBaseContext().getContentResolver(),
                Secure.ANDROID_ID); 
		
		
		File directorio = new File(Environment.getExternalStorageDirectory() + "/DCIM/CIA");
    	if(!directorio.exists())
    		directorio.mkdirs();
		
    	directorio = new File(Environment.getExternalStorageDirectory() + "/DCIM/CIA2");
    	if(!directorio.exists())
    		directorio.mkdirs();
    	
    	//Toast.makeText(getApplicationContext(),"Android ID: " + android_id, Toast.LENGTH_LONG).show();
    	
		if(verificaConexion(this) == false)
		{
			new AlertDialog.Builder(this)
			    .setTitle("Falla de conexión")
			    .setMessage("No existe conexión a internet por parte del dispositivo." +
			    		"\n¿Desea tomar una foto para poder registrar algún evento? " +
			    		"\nLa foto puede ser sincronizada cuando regrese la conexión." +
			    		"\n\nEsta opción también puede encontrarse en el menú de opciones.")
			    .setPositiveButton("Deseo tomar una foto.", new DialogInterface.OnClickListener() 
			    {
			         public void onClick(DialogInterface dialog, int whichButton) 
			         {
			        	File directorio = new File(Environment.getExternalStorageDirectory() + "/DCIM/CIA2");
					    	if(!directorio.exists())
					    		directorio.mkdir();
			        	String photoPath = "";
				    	String state = Environment.getExternalStorageState();
			            if (Environment.MEDIA_MOUNTED.equals(state)) 
			            {
			               
			            	DateTyme = fechaHoraActual();
			                
			                
			                photoPath = Environment.getExternalStorageDirectory() + "/DCIM/CIA2/" + DateTyme +".jpg";
			               
			                try
			                {
			                    File photo = new File(photoPath);
			                    
			                
			                    Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE"); 
			                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photo));
			                    startActivityForResult(cameraIntent,5);
			                
			                } 
			                catch (Exception e) 
			                {
			                	Toast.makeText(getApplicationContext(),e+"", Toast.LENGTH_SHORT).show();
			                }
			            }
			            else
			            {
			            	Toast.makeText(getApplicationContext(),"Existen fallos en la conexión con la cámara del dispositivo", Toast.LENGTH_SHORT).show();
			            }
			        	 
			             
			         }
			    })
			    .setNegativeButton("Ingresar a la aplicación aunque no tenga internet.", new DialogInterface.OnClickListener() {
			         public void onClick(DialogInterface dialog, int whichButton) 
			         {
			                //No se hace nada
			         }
			    }).show();
		}
   
				myWebView = (WebView) findViewById(R.id.webView);
				
				WebSettings webSettings = myWebView.getSettings();
		        webSettings.setJavaScriptEnabled(true);
		
		        /* Register a new JavaScript interface called HTMLOUT  ////// CALLING OBSOLET METHOD */
		       // myWebView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
		
		        myWebView.setWebViewClient(new MyCustomWebViewClient());
		        
		        //PCID
		        myWebView.setWebChromeClient(new WebChromeClient() {
		        	 public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
		        	    callback.invoke(origin, true, false);
		        	 }
		        	});
		        // FIN PCID
		        RedirecTo = getIntent().getStringExtra("redirec");
		        
		        if(RedirecTo != null){
		        	
		        	myWebView.loadUrl(RedirecTo);
		        	Log.i("Redirigido", RedirecTo.toString());
		            
		            
		            myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		        }else{
		        	RedirecTo = "http://www.chispudo.com:8000/cia/webapp";
			        myWebView.loadUrl(RedirecTo);
			        Log.i("Dirigido al inicio", RedirecTo.toString());
			        
			        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		        
		        }
		        
		        
		        //WebViewOculto
		        webViewOculto = (WebView) findViewById(R.id.webView2);
				
				WebSettings webSettings2 = webViewOculto.getSettings();
		        webSettings2.setJavaScriptEnabled(true);
		
		
		        /* Register a new JavaScript interface called HTMLOUT /// CALLING OBSOLET METHOD */
		        //webViewOculto.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
		
		        webViewOculto.setWebViewClient(new MyCustomWebViewClient());
		        
		        //PCID
		        webViewOculto.setWebChromeClient(new WebChromeClient() {
		        	 public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
		        	    callback.invoke(origin, true, false);
		        	 }
		        	});
		        // FIN PCID
		        webViewOculto.loadUrl("http://www.chispudo.com:8000/cia/webapp");
		        webViewOculto.setVisibility(View.GONE);	
        
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) 
	    {
	        case R.id.upload :
	        	if(verificaConexion(this) == true)
	        	{
	        		
	        		webViewOculto.loadUrl("http://www.chispudo.com:8000/cia/existsLogin.php");
	        		session = "UPLOAD";
	        		if (session == "UPLOAD"){
	    	    		
	    	    		
	    	    		
	    	    		session = "";
	    	    		
	    	    		Intent i = new Intent(MainActivity.this,UploadFTPActivity.class);
	    	    		i.putExtra("redirccion", uriRedirect);
	    	            startActivity(i);
	    	            
	    	            session = "";
	    	    	}
	        		
	        		
	        		   
	        	}
	        	else
	        	{
	        		Toast.makeText(getApplicationContext(),"Existen fallos en la conexión con internet.", Toast.LENGTH_SHORT).show();
	        	}
	            return true;        
	        case R.id.takephoto:
	        	if(verificaConexion(this) == true)
	        	{
	        		new AlertDialog.Builder(this)
				    .setTitle("Toma de Foto")
				    .setMessage("Actualmente el dispositivo se encuentra conectado a internet." +
				    		"\n¿Aún así desea tomar una foto para poder registrar algún evento? " +
				    		"\nLa foto puede ser sincronizada luego de haber sido tomada.")
				    .setPositiveButton("Deseo tomar una foto.", new DialogInterface.OnClickListener() 
				    {
				         public void onClick(DialogInterface dialog, int whichButton) 
				         {
				        	File directorio = new File(Environment.getExternalStorageDirectory() + "/DCIM/CIA2");
						    	if(!directorio.exists())
						    		directorio.mkdir();
				        	String photoPath = "";
					    	String state = Environment.getExternalStorageState();
				            if (Environment.MEDIA_MOUNTED.equals(state)) 
				            {
				               
				            	DateTyme = fechaHoraActual();
				                photoPath = Environment.getExternalStorageDirectory() + "/DCIM/CIA2/" + DateTyme +".jpg";
				             
				                try
				                {
				                    File photo = new File(photoPath);
				                    Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE"); 
				                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photo));
				                    startActivityForResult(cameraIntent,5);
				                 
				                
				                } 
				                catch (Exception e) 
				                {
				                	Toast.makeText(getApplicationContext(),e+"", Toast.LENGTH_SHORT).show();
				                }
				            }
				            else
				            {
				            	Toast.makeText(getApplicationContext(),"Existen fallos en la conexión con la cámara del dispositivo", Toast.LENGTH_SHORT).show();
				            }
				        	 
				             
				         }
				    })
				    .setNegativeButton("Regresar a la aplicación.", new DialogInterface.OnClickListener() {
				         public void onClick(DialogInterface dialog, int whichButton) 
				         {
				                //No se hace nada
				         }
				    }).show();
	        	}
	        	else
	        	{
	        		File directorio = new File(Environment.getExternalStorageDirectory() + "/DCIM/CIA2");
			    	if(!directorio.exists())
			    		directorio.mkdir();
		        	String photoPath = "";
			    	String state = Environment.getExternalStorageState();
		            if (Environment.MEDIA_MOUNTED.equals(state)) 
		            {
		                
		            	DateTyme = fechaHoraActual();
		                photoPath = Environment.getExternalStorageDirectory() + "/DCIM/CIA2/" + DateTyme +".jpg";
		                try
		                {
		                    File photo = new File(photoPath);
		                   
		                
		                    Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE"); 
		                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photo));
		                    startActivityForResult(cameraIntent,5);
		                
		                } 
		                catch (Exception e) 
		                {
		                	Toast.makeText(getApplicationContext(),e+"", Toast.LENGTH_SHORT).show();
		                }
		            }
		            else
		            {
		            	Toast.makeText(getApplicationContext(),"Existen fallos en la conexión con la cámara del dispositivo", Toast.LENGTH_SHORT).show();
		            }
	        	
	        	}
	        	return true;
	       
	        case R.id.close :
	        	new AlertDialog.Builder(this)
			    .setTitle("SALIR")
			    .setMessage("¿Desea salir de la aplicación?")
			    .setPositiveButton("Deseo salir.", new DialogInterface.OnClickListener() 
			    {
			         public void onClick(DialogInterface dialog, int whichButton) 
			         {
			        	 
			        	 myWebView.loadUrl("http://www.chispudo.com:8000/cia/webapp/index.php?logout=1") ;
			        	 Toast.makeText(getApplicationContext(), "Cerrando la aplicación un momento por favor...", Toast.LENGTH_LONG).show();
			        	 
			         }
			    })
			    .setNegativeButton("No deseo salir.", new DialogInterface.OnClickListener() {
			         public void onClick(DialogInterface dialog, int whichButton) 
			         {
			                //No se hace nada
			         }
			    }).show();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	    // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) 
	    {
	        myWebView.goBack();
	        return true;
	    }
	    // If it wasn't the Back key or there's no web page history, bubble up to the default
	    // system behavior (probably exit the activity)
	    return super.onKeyDown(keyCode, event);
	}

	
	// Code replace deprecated methods
	private class MyCustomWebViewClient extends WebViewClient
	{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) 
        {
        	
        	
        	if(url.contains("http://www.chispudo.com:8000/cia/webapp") == true)  	    	
        		view.loadUrl(url);
        	
        	
      	    return true;      	
        }
        
        @Override
        public void onPageFinished(WebView view, String url)
        { 	
            
        	
        	getURL = url;
        	processRequest(url);
        	
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
            	Toast.makeText(getApplicationContext(),"No se ha podido realizar el proceso, no se ha encontrado archivo txt", Toast.LENGTH_SHORT).show();
            } 
            catch (IOException e) 
            {
            	Toast.makeText(getApplicationContext(),"No se ha podido realizar el proceso, no se ha leido el archivo txt", Toast.LENGTH_SHORT).show();
            }
     
            return ret;
        }
    	
    }
	
// Code Deprecated deleted
	
// **********************	
	
	
	public void processRequest(String Get){

	    	//Verifico existencia del directorio, sino lo creo
	    	File directorio = new File(Environment.getExternalStorageDirectory() + "/DCIM/CIA");
	    	if(!directorio.exists())
		    	directorio.mkdir();
	    	
	    	
	    	if(Get.contains("take_photo"))
	    	{	    		
	    		int numero = 0;
        		String Usuario = "";
        		String Type = "";
        		String Comando = "";
        		String UrlString = Get;        		
        		String[] separador = UrlString.split("=");  
        		
        		
        		String Texto1 = separador[8].trim();
        		String Texto2 = separador[6].trim();
        		String Texto3 = separador[7].trim();
        		String Texto4 = separador[3].trim();
        		
        		        		
        		String[] sTwo = Texto1.split("&");
        		String usr = sTwo[0].trim();
        		
        		numero = Integer.parseInt(usr);  		
        		         		 
        		if( numero < 100){
        			
        			Usuario = "0"+usr;
        		}else if(numero < 10){
        			Usuario = "00"+usr;
        		}        		
        		
        		String[] sThree = Texto2.split("&");    		        		       		
        		String SinIgual2 = sThree[0].trim();
        		
        		String[] sFour = Texto3.split("&");
        		String RefType = sFour[0].trim();        		
        		String[] sFive = Texto4.split("&");
        		String IdRef = sFive[0].trim();
        		
        		Type = RefType.toUpperCase();
        		Comando = SinIgual2.replace("take_photo", "TP");
        		DateTyme = fechaHoraActual();
        		name =Usuario+"_"+Comando+"_"+Type+"_"+IdRef+"_";    		
        		       		
        		
        		// Construct URL redirection
        		String Redirect = Get;
        		
        		String[] SeparaRedirect = Redirect.split("=");
        		 
        		String mnuOptAnd1 = SeparaRedirect[1].trim();
        		String[] SeparaRand	= mnuOptAnd1.split("&");
        		String mnuOpt = SeparaRand[0].trim();
        		
        		String idrow = SeparaRedirect[2].trim();
        		String[] SeparaRand2 = idrow.split("&");
        		String idrowR = SeparaRand2[0].trim();
        		
        		String idrow2 = SeparaRedirect[3].trim();
        		String[] SeparaRand3 = idrow2.split("&");
        		String idrowR2 = SeparaRand3[0].trim();
        		
        		uriRedirect = "mnopt="+mnuOpt+"&idrow="+idrowR+"&idrow2="+idrowR2;
	    		
	    		
		    	String state = Environment.getExternalStorageState();
	            if (Environment.MEDIA_MOUNTED.equals(state)) 
	            {
	            	NameFile = name+DateTyme;
	            	
	                photoPath = Environment.getExternalStorageDirectory() + "/DCIM/CIA/" + NameFile;
	                
	                try
	                {
	                	
	                    File photo = new File(photoPath+".jpg");              
	                    Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE"); 
	                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photo));
	                    startActivityForResult(cameraIntent,4);
	                
	                } 
	                catch (Exception e) 
	                {
	                	Toast.makeText(getApplicationContext(),e+"", Toast.LENGTH_SHORT).show();
	    		    	
	                }
	            }
	            else
	            {
	            	Toast.makeText(getApplicationContext(),"Existen fallos en la conexión con la cámara del dispositivo", Toast.LENGTH_SHORT).show();
    		    
	            }
	    	}else if(Get.contains("link_photo")){
	    		
	    			
	    			Log.i("CAPTURA LINK FOTO URL", Get);
	    			
	    			int numero = 0;
	        		String Usuario = "";
	        		String Type = "";
	        		String Comando = "";
	        		String UrlLink = Get;        		
	        		String[] splitLink = UrlLink.split("=");
	        		String Reference ="";
	        		
	        		
	        		String textLink1 = splitLink[8].trim();
	        		String textLink2 = splitLink[7].trim();
	        		String textLink3 = splitLink[6].trim();
	        		String textLink4 = splitLink[3].trim();

	        		// Usuario
	        		String[] splitLink2 = textLink1.split("&");	        		 
	        		String usr = splitLink2[0].trim();
	        		
	        		// Tipo
	        		String[] splitLink3 = textLink2.split("&");	        		
	        		String tipo = splitLink3[0].trim();
	        		
	        		String[] splitLink4 = textLink3.split("&");
	        		Comando = splitLink4[0].replace("link_photo", "TP");
	        		Type = tipo.toUpperCase();
	        		
	        		String[] splitLink5 = textLink4.split("&");
	        		Reference = splitLink5[0].trim();
	        		
	        		
	        		if( numero < 100){
	        			
	        			Usuario = "0"+usr;
	        		}else if(numero < 10){
	        			Usuario = "00"+usr;
	        		}      
	        		
	        		
	        		
	        		NameFileLink = Usuario+"_"+Comando+"_"+Type+"_"+Reference+"_";
	        		
	        		
	        		
	        		// Construct URL redirection
	        		String Redirect = Get;
	        		
	        		String[] SeparaRedirect = Redirect.split("=");
	        		 
	        		String mnuOptAnd1 = SeparaRedirect[1].trim();
	        		String[] SeparaRand	= mnuOptAnd1.split("&");
	        		String mnuOpt = SeparaRand[0].trim();
	        		
	        		String idrow = SeparaRedirect[2].trim();
	        		String[] SeparaRand2 = idrow.split("&");
	        		String idrowR = SeparaRand2[0].trim();
	        		
	        		String idrow2 = SeparaRedirect[3].trim();
	        		String[] SeparaRand3 = idrow2.split("&");
	        		String idrowR2 = SeparaRand3[0].trim();
	        		
	        		uriRedirect = "mnopt="+mnuOpt+"&idrow="+idrowR+"&idrow2="+idrowR2;
	        		
	        		
	        		
	        		
	        		
	        		
	        		Log.i("TEXT PROSSECED", NameFileLink.toString());
	        		
	        		Intent gal = new Intent(MainActivity.this, GalleryActivityShow.class );
	        		gal.putExtra("RenameFileLink", NameFileLink);
	        		gal.putExtra("redirect", uriRedirect);
	        		startActivity(gal);
	        		
	    			
	    	}else if(Get.contains("logout=1"))
	    	{ 
	    	    //finish();
	    	    //System.exit(0);
	    		onDestroy();
	    	}
	    		
    
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    if(requestCode == 4 && resultCode == RESULT_OK)
	    {
	    	
	    	 photoPath = Environment.getExternalStorageDirectory() + "/DCIM/CIA/" + name + DateTyme +".jpg";
             writeToFile(photoPath,"pathphoto.txt");
             writeToFile(name+DateTyme,"name.txt");
	    	
	    	Intent i = new Intent(MainActivity.this,NamePhoto.class); 
	    	i.putExtra("path",readFromFile("pathphoto.txt"));
	    	i.putExtra("name",readFromFile("name.txt"));
	    	i.putExtra("actividad","interna"); 	
    		i.putExtra("redireccion", uriRedirect);
            
            startActivityForResult(i,4);
            Toast.makeText(getApplicationContext(),"Foto tomada, agregue un nombre\n y una descripción.", Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(),"Redireccionará a: "+ uriRedirect, Toast.LENGTH_SHORT).show();
	    }
	    else if(requestCode == 4 && resultCode != RESULT_OK)
	    {
	    	//Toast toast = Toast.makeText(getApplicationContext(),"No ha sido tomada la foto", Toast.LENGTH_SHORT);
	    	//toast.show();
	    }
	    else if(requestCode == 3)
	    {
	    	myWebView.loadUrl(readFromFile("linktogo2.txt"));
	    }
	    else if(requestCode == 5  && resultCode == RESULT_OK)
	    {
	    	photoPath = Environment.getExternalStorageDirectory() + "/DCIM/CIA2/" +DateTyme +".jpg";
            writeToFile(photoPath,"pathphoto.txt");
            writeToFile(DateTyme,"name.txt");
	    	
	    	Intent i = new Intent(MainActivity.this,NamePhoto.class); 
	    	i.putExtra("path",readFromFile("pathphoto.txt"));
	    	i.putExtra("name",readFromFile("name.txt"));
	    	i.putExtra("actividad","Externa");
           startActivityForResult(i,5);
           Toast.makeText(getApplicationContext(),"Foto tomada, agregue un nombre y una descripción.", Toast.LENGTH_SHORT).show();	    		    	
	    }
	}
	
	
	private void writeToFile(String data,String file) 
    {
        try 
        {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(file, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) 
        {
        	Toast.makeText(getApplicationContext(),"No se ha podido realizar el proceso", Toast.LENGTH_SHORT).show();
        } 
         
    }
	
	
	
	//Metod para poder leer archivos
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
        	Toast.makeText(getApplicationContext(),"No se ha podido realizar el proceso, no se ha encontrado archivo txt", Toast.LENGTH_SHORT).show();
        } 
        catch (IOException e) 
        {
        	Toast.makeText(getApplicationContext(),"No se ha podido realizar el proceso, no se ha leido el archivo txt", Toast.LENGTH_SHORT).show();
        }
 
        return ret;
    }
	


	
	//Este metodo comprueba la conexion a internet
	public boolean isOnline() 
	{
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) 
			return true;

		return false;
	}
	
	@Override
	public void onDestroy() 
	{
        super.onDestroy();
        //myWebView.loadUrl("http://www.chispudo.com:8000/cia/webapp/index.php?logout=1"); 
   	 	finish();
        System.exit(0);
    }
	
	@Override
    public void onStop() 
	{
        super.onStop();
        //myWebView.loadUrl("http://www.chispudo.com:8000/cia/webapp/index.php?logout=1"); 
   	 	//finish();
        //System.exit(0);
    }
	
	@Override
	public void onRestart() 
	{
		super.onRestart();
		onRestart=true;
	}

	
	@Override
	public void onResume() 
	{
		super.onResume();
		
		
	    if (!onRestart) 
	    {
	    	webViewOculto.loadUrl("http://www.chispudo.com:8000/cia/existsLogin.php");
	    	session = "LOGOUT";	    	
	    }
	    onRestart = false;
		
	}

}
