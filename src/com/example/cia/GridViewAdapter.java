package com.example.cia;


import java.util.ArrayList;
import com.squareup.picasso.Picasso;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class GridViewAdapter extends BaseAdapter 
{

	private Context context; // Contexto
	//private String[] items; // obtiene el arreglo de las imagenes
	
	private ArrayList<String> items;
	
	// construccion del adaptador
	public GridViewAdapter(Context context, ArrayList<String> items){
		
		super();
		this.context = context;
		this.items = items;
	};

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		//return items.length;
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		//return items[position];
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ImageView img;
		
		// Obtiene la imagen 
		
		if(convertView == null){
				img = new ImageView(context);
				
				convertView = img;
				img.setPadding(5, 5, 5, 5);
				
		}else{
			img = (ImageView) convertView;
		}
		
		/* Libreria para galerias
		Picasso.with(context)
		.load("file://"+items.get(position))
		.placeholder(R.drawable.lost)
		.resize(350, 400)
		.into(img);*/
		
		
		
		
		
		 Bitmap bm = decodeSampledBitmapFromUri(items.get(position), 200, 350);

		 ((ImageView) convertView).setImageBitmap(bm);
	        
		
		
		

		return convertView;
	}
	
	
	
	
	
	public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
		
		Bitmap bm = null;
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
	     
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	     
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, options); 
	     
		return bm;  	
	}
	
	public int calculateInSampleSize(
			
		BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		
		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float)height / (float)reqHeight);   	
			} else {
				inSampleSize = Math.round((float)width / (float)reqWidth);   	
			}   
		}
		
		return inSampleSize;   	
	}


	
	
}