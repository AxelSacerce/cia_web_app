package com.example.cia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;


public class GaleriaSubir extends Activity 
{
    private  Gallery galleryView;
    /** Called when the activity is first created. */
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galeria_enlazar);
        galleryView = (Gallery)findViewById(R.id.galleryid);
        
        
        File folder = new File(Environment.getExternalStorageDirectory().toString()+"/DCIM/CIA");        
        File files[] = folder.listFiles();

        String nombres="";
        String paths="";
        String comentarios = "";
        
        for (int i=0; i < files.length; i++)
        {
        	if(files[i].isFile())
        	{	
        		if(files[i].getAbsolutePath().contains(".jpg"))
        		{
        			paths = paths+","+files[i].getAbsolutePath();
        			StringTokenizer st = new StringTokenizer(files[i].getName(),".");
        			File nome = new File(Environment.getExternalStorageDirectory().toString()+"/DCIM/CIA/"+ st.nextToken() + ".cia");
        			if(nome.exists())
        			{
        				
        				File sdcard = Environment.getExternalStorageDirectory();

        				//Get the text file
        				File file = new File(sdcard,"/DCIM/CIA/"+files[i].getName().substring(0,  files[i].getName().length()-3 )+"cia");

        				//Read text from file
        				StringBuilder text = new StringBuilder();

        				try 
        				{
        				    BufferedReader br = new BufferedReader(new FileReader(file));
        				    String line;

        				    while ((line = br.readLine()) != null) 
        				    {
        				        text.append(line);
        				        text.append('\n');
        				    }
        				}
        				catch (IOException e) {
        				    //You'll need to add proper error handling here
        				}
        				
        				String contenido = text.toString();
        				
        				nombres += "," + ( contenido.substring( contenido.indexOf("<cia_title>")  +11 , contenido.indexOf("</cia_title>") ) );
        				comentarios += "," + ( contenido.substring( contenido.indexOf("<cia_comment>")  +13 , contenido.indexOf("</cia_comment>") ) );
        				
        				/*
        				contenido = "AA"+contenido;
        				String contenido2[] = contenido.split("<cia_title>");
        				StringTokenizer st1 = new StringTokenizer(contenido2[1],"<");
        				nombres += "," + st1.nextToken();
        				
        				
        				String contenido3[] = contenido2[1].split("</cia_title>");
        				contenido3[1] = "AA" + contenido3[1];
        				String contenido4[] = contenido3[1].split("<cia_comment>");
        				StringTokenizer st2 = new StringTokenizer(contenido4[1],"<");
        				comentarios += "," + st2.nextToken();
        				*/
        				
        				
        				//Toast toast = Toast.makeText(getApplicationContext(),nombres + "  "+ comentarios, Toast.LENGTH_SHORT);
        		    	//toast.show();
        				//nombres = nombres + ","+files[i].getName();Alexander Javier bolaños Chavac Alexander JAvier bolañs
        				
        			}
        			else
        			{
        				nombres += ",Sin nombre";
        				comentarios += ",Sin comentarios";
        			}
        		}	
        	}
        }
        
        
        nombres = nombres.substring(1,nombres.length());
        paths = paths.substring(1,paths.length());
        comentarios = comentarios.substring(1,comentarios.length());
        
        String data[] = paths.split(",");
        String name[] = nombres.split(",");
        String comment[] = comentarios.split(",");
        
		Typeface font1 = Typeface.createFromAsset(getAssets(), "MonaKo.ttf");
        
        galleryView.setAdapter(new ImageAdapter(this,data,name,comment,font1));
        //}fasjkdsalfs
    }
    
  //Metod para poder leer archivos
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
        	Toast toast = Toast.makeText(this,"No se ha podido realizar el proceso, no se ha encontrado archivo txt", Toast.LENGTH_SHORT);
	    	toast.show();
        } 
        catch (IOException e) 
        {
        	Toast toast = Toast.makeText(this,"No se ha podido realizar el proceso, no se ha leido el archivo txt", Toast.LENGTH_SHORT);
	    	toast.show();
        }
 
        return ret;
    }
    
    
}