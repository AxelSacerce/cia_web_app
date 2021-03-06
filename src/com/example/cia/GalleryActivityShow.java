package com.example.cia;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GalleryActivityShow extends Activity {

	private GridView gridView;
	private GridViewAdapter gridViewAdapter;
	static Button btnVincular;
	static Button btnBack;
	static Button btnVer;
	String rutaDeLaImagen;
	Object positionSet = 0;
	String ExternalStorageDirectoryPath = Environment
    		.getExternalStorageDirectory()
    		.getAbsolutePath();
    
    String targetPath = ExternalStorageDirectoryPath + "/DCIM/CIA2/";
	String targetPathLink = ExternalStorageDirectoryPath +"/DCIM/CIA/";
	static String archivoImg;
	static String archivoTxt;
	String  RenameFIntent;
	String 	RedirecTo = null;
	String 	LoadRedirecTo = null;
    
	
	
	// Lista que obtiene una lista de im�genes del celular
	private ArrayList<String> items;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galery);
        
        gridView 	= (GridView)findViewById(R.id.PhoneImageGrid);
        btnVincular = (Button) findViewById(R.id.btnVincular);
        btnBack 	= (Button)findViewById(R.id.btnBack);
        btnVer  	= (Button)findViewById(R.id.btnVer);
        
        RenameFIntent = getIntent().getStringExtra("RenameFileLink");
        RedirecTo = getIntent().getStringExtra("redirect");
        LoadRedirecTo = "http://www.chispudo.com:8000/cia/webapp/main.php?"+RedirecTo;
        
        
        gridViewAdapter = new GridViewAdapter(getApplicationContext(), getAllShowImagesPath(targetPath));
        
        gridView.setAdapter(gridViewAdapter);
       
        		
        
        gridView.setOnItemClickListener(new OnItemClickListener(){
        	
        	@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
        		rutaDeLaImagen = gridViewAdapter.getItem(position).toString();
				positionSet = gridViewAdapter.getItem(position);				
				Toast.makeText(getApplicationContext(), "Fotograf�a seleccionada lista para vincular", Toast.LENGTH_SHORT).show();
				
				
			}
        	
        });
        
        
        
       btnVincular.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			
			
			String GetNameImage = positionSet.toString();
			String[] separador = GetNameImage.split("/");
			archivoImg = separador[6].trim();
			
			String[] texto = archivoImg.split(".jpg");
			archivoTxt = texto[0].trim();
			
	
			
			
			// Original File		
			File RenameImg = new File(targetPath+archivoImg);	
			File RenameTxt = new File(targetPath+archivoTxt+".txt");
			
			// Renamed Files and move
			File NewNameImg = new File(targetPathLink+RenameFIntent+archivoImg);
			File NewNameTxt = new File(targetPathLink+RenameFIntent+archivoTxt+".txt");
			
			
			boolean VincularTxt = RenameTxt.renameTo(NewNameTxt);
			boolean VincularImg = RenameImg.renameTo(NewNameImg);
			
			if(VincularImg && VincularTxt){
				Toast.makeText(getApplicationContext(), "Archivos vinculados, listos para ser procesados", 
						Toast.LENGTH_LONG).show();
				Intent redir = new Intent(GalleryActivityShow.this, MainActivity.class);
  				redir.putExtra("redirec", LoadRedirecTo);
  				startActivity(redir);
				finish();
			}else if(!VincularTxt){
				
				Toast.makeText(getApplicationContext(), "No existe comentario para la imagen vinculada", Toast.LENGTH_LONG).show();
				
				finish();
			}
						
		}
    	   
       });
       
       btnBack.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
				Intent redir = new Intent(GalleryActivityShow.this, MainActivity.class);
				redir.putExtra("redirec", LoadRedirecTo);
				startActivity(redir);			
				finish();
		}
    	   
    	   
       });
       btnVer.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
    		intent.setAction(Intent.ACTION_VIEW);
    		intent.setDataAndType(Uri.parse("file://"+rutaDeLaImagen), "image/*");
    		startActivity(intent);
			
		}
	});
        
        
        
        
    }
    
    
    /*
     * Obtener las imagenes de path del movil
     * */
    
    public static ArrayList<String> getAllShowImagesPath(String DirectoryPath){
    	ArrayList<String> listOfAllImages =  new ArrayList<String>();
    	String absolutePathImage = null;
    	
    		
    	
        //Toast.makeText(this, DirectoryPath, Toast.LENGTH_LONG).show();
        File targetDirector = new File(DirectoryPath);
        
        File[] files = targetDirector.listFiles();
        	if(files.length <= 0 ){

        		btnVer.setEnabled(false);
        		btnVer.setAlpha(100);
        		btnVincular.setEnabled(false);
        		btnVincular.setText("Sin im�genes");
        		btnVincular.setAlpha(75);
        		
        	}else{
        		
        		btnVer.setEnabled(true);
        		btnVincular.setEnabled(true);
        	}
        
		        for (File file : files){
		        	absolutePathImage = file.getAbsolutePath();
		        	
		        	if(file.getName().contains(".jpg")){
		        	listOfAllImages.add(absolutePathImage);
		        	
		        	}else if(file.getName().contains(".txt")){
		        		
		        		
		        		
		        		Log.i("File TXT", file.getName().toString());       		
		        		
		        	}      	
		        	
		        	
		        	}
    	
		        return listOfAllImages;
    	
    }
    
}
