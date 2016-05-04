package com.example.cia;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;





@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
public class NamePhoto extends Activity 
{
	private EditText edit1,edit2;
	private ImageButton btnOk,btnExit;
	private ImageView image1;
	private String path,name,actividad;
	private TextView tv1,tv2,tv3;
	private File imgFile;
	String 	RedirecTo= null;
	String  LoadRedirectTo = null;
	
	@Override
       public void onCreate(Bundle savedInstanceState)
       {		
              super.onCreate(savedInstanceState);
              setContentView(R.layout.link);
              edit1 =  (EditText)findViewById(R.id.editText1);
              edit2 =  (EditText)findViewById(R.id.editText2);
              btnOk = (ImageButton) findViewById(R.id.imageButton1);
              btnExit = (ImageButton) findViewById(R.id.imageButton2);
              tv1 = (TextView)findViewById(R.id.textView1);
              tv2 = (TextView)findViewById(R.id.textView2);
              tv3 = (TextView)findViewById(R.id.textView3);
              image1 = (ImageView) findViewById(R.id.imageView1);
              
              path = getIntent().getStringExtra("path");
              name = getIntent().getStringExtra("name");
              actividad = getIntent().getStringExtra("actividad");
              RedirecTo = getIntent().getStringExtra("redireccion");
              
              LoadRedirectTo = "http://www.chispudo.com:8000/cia/webapp/main.php?"+RedirecTo;
              
              //Toast.makeText(getApplicationContext(),"Graba y Redirecciona a: "+LoadRedirectTo, Toast.LENGTH_LONG).show();
              
              //edit1.setText(name);
              imgFile = new  File(path);
              Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());              					
              image1.setImageBitmap(myBitmap);	
              
              //tv1.setText(imgFile.toString());
              
              btnOk.setOnClickListener(new OnClickListener() 
      		  {
      			public void onClick(View v) 
      			{
      				
      				try
      				{
      				if(edit1.getText().equals(""))
      				{
      					Toast toast = Toast.makeText(getApplicationContext(),"Debe nombrar la fotografía tomada.", Toast.LENGTH_SHORT);
      			    	toast.show();
      				}
      				else
      				{
      					
      					if(actividad.equals("interna")){
      						File file;
      						file = new File(Environment.getExternalStorageDirectory() + "/DCIM/CIA/"+name+".txt");
      						 FileWriter writer = new FileWriter(file);
            			        String comentario = edit2.getText()+"";
            					
            			        if(edit2.getText().equals("")){
            			        	comentario = "Sin comentarios";}
            			        writer.append("<cia_title>"+edit1.getText()+"</cia_title><cia_comment>"+comentario+"</cia_comment>");
            			        writer.flush();
            			        writer.close();
            			      
      	      			    Toast toast = Toast.makeText(getApplicationContext(),"Foto guardada, nombrada y preparada para utilizarla.", Toast.LENGTH_SHORT);
      	      		    	toast.show();
      	      		    	
	      	      		    Intent redir = new Intent(NamePhoto.this, MainActivity.class);
	          				redir.putExtra("redirec", LoadRedirectTo);
	          				startActivity(redir);
	          				finish();     
      	      		    	
      						Log.i("LOG MESSAGE",  file.toString());
      					}
      					else if(actividad.equals("Externa"))
      					{
      						File file;
      						file = new File(Environment.getExternalStorageDirectory() + "/DCIM/CIA2/"+name+".txt");
      						 FileWriter writer = new FileWriter(file);
           			        String comentario = edit2.getText()+"";
           					
           			        if(edit2.getText().equals("")){
           			        	comentario = "Sin comentarios";}
           			        writer.append("<cia_title>"+edit1.getText()+"</cia_title><cia_comment>"+comentario+"</cia_comment>");
           			        writer.flush();
           			        writer.close();
           			      
     	      			    Toast toast = Toast.makeText(getApplicationContext(),"Foto guardada, nombrada y preparada para utilizarla.", Toast.LENGTH_SHORT);
     	      		    	toast.show();
      					}
      					
      			       
      			        //onBackPressed();
      					finish();
      			        
      			        
      				}
      				}
      				catch(Exception e){
      					Toast toast = Toast.makeText(getApplicationContext(),"Error "+e, Toast.LENGTH_SHORT);
      			    	toast.show();
      				}
      					
      			}
      		  });
              
              btnExit.setOnClickListener(new OnClickListener() 
      		  {
      			public void onClick(View v) 
      			{
      				new AlertDialog.Builder(v.getContext())
    			    .setTitle("Salir")
    			    .setMessage("¿Esta seguro que desea salir?" +
    			    		"\n\nSi sale en este momento no se guardará la foto tomada.")
    			    .setPositiveButton("Cancelar.", new DialogInterface.OnClickListener() 
    			    {
    			         public void onClick(DialogInterface dialog, int whichButton) 
    			         {
    			        	//No se hace nada
    			        	
    			         }
    			    })
    			    .setNegativeButton("Deseo Salir.", new DialogInterface.OnClickListener() {
    			         public void onClick(DialogInterface dialog, int whichButton) 
    			         {
    			        	imgFile.delete();
    			        	Toast.makeText(getApplicationContext(),"No ha sido guardada la fotografía.", Toast.LENGTH_SHORT).show();
    	      		    	Intent redir = new Intent(NamePhoto.this, MainActivity.class);
	          				redir.putExtra("redirec", LoadRedirectTo);
	          				startActivity(redir);
    	      		    	finish();
    			         }
    			    }).show();
      					
      			}
      		  });

       }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	    // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) 
	    {
	    	Toast toast = Toast.makeText(getApplicationContext(),"Debe nombrar la foto antes de salir.", Toast.LENGTH_SHORT);
	    	toast.show();
	        return true;
	    }
	    
	    return super.onKeyDown(keyCode, event);
	}

	public void writeToFile(String data,String file) 
    {
        try 
        {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(file, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) 
        {
        	Toast toast = Toast.makeText(getApplicationContext(),"No se ha podido realizar el proceso", Toast.LENGTH_SHORT);
	    	toast.show();
        } 
         
    }


}