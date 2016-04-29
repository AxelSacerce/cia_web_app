package com.example.cia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ImageAdapter extends BaseAdapter
{

        private Activity activity;
        private static LayoutInflater inflater=null;

        public ImageAdapter(Activity a,String[] data,String[] name,String[] comment,Typeface font1) 
        {
            activity = a;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           /*
            String name2[] = {
                    "Image1", "Image2",
                    "Image3", "Image4",
                    "Image5", "Image6"
            };
            name = name2;
            data = name2;
            */
            
            /*
            File folder = new File(Environment.getExternalStorageDirectory().toString()+"/DCIM/CIA2");        
            File files[] = folder.listFiles();

            String nombres="";
            String paths="";
            for (int i=0; i < files.length; i++)
            {
            	if(files[i].isFile())
            	{	
            		if(files[i].getAbsolutePath().contains(".jpg"))
            		{
            			paths = paths+","+files[i].getAbsolutePath();
            			StringTokenizer st = new StringTokenizer(files[i].getName(),".");
            			File nome = new File(Environment.getExternalStorageDirectory().toString()+"/DCIM/CIA2/"+ st.nextToken() + ".cia");
            			if(nome.exists())
            			{
            				nombres = nombres + ","+files[i].getName();
            			}
            			else
            				nombres = nombres + ",Sin nombre";
            		}	
            	}
            }
            
            nombres = nombres.substring(1,nombres.length());
            paths = paths.substring(1,paths.length());
            
            name = nombres.split(",");
            data = paths.split(",");
            
            */
            this.data = data;
            this.name = name;
            this.comment = comment;
            this.font1 = font1;
            
        }

        public int getCount() 
        {
            return data.length;
        }

        public Object getItem(int position) 
        {
            return position;
        }

        public long getItemId(int position) 
        {
            return position;
        }

        public static class ViewHolder
        {
            public TextView text;
            public ImageView image;
            public TextView comentario,titulo1,titulo2;
        }

        public View getView(int position, View convertView, ViewGroup parent) 
        {
            View vi=convertView;
            ViewHolder holder;
            if(convertView==null)
            {
                vi = inflater.inflate(R.layout.image_gallery_items, null);
                holder=new ViewHolder();
                holder.text=(TextView)vi.findViewById(R.id.textView1);
                holder.image=(ImageView)vi.findViewById(R.id.image);
                holder.comentario=(TextView)vi.findViewById(R.id.textView2);
                holder.titulo1=(TextView)vi.findViewById(R.id.titulo1);
                holder.titulo2=(TextView)vi.findViewById(R.id.titulo2);
                vi.setTag(holder);
            }
            else
                holder=(ViewHolder)vi.getTag();
            
            holder.text.setText(""+name[position]+"\n");
            holder.comentario.setText(comment[position]);
            //final int stub_id=data[position];
            //holder.image.setImageResource(stub_id);
            File imgFile = new File(data[position]);
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());				
            holder.image.setImageBitmap(myBitmap);	
            
            holder.titulo1.setTypeface(font1, Typeface.BOLD);
            holder.titulo2.setTypeface(font1, Typeface.BOLD);
            
            return vi;
        }
        
    	

        String[] data;
        String[] name;
        String[] comment;
        Typeface font1,font2;
    }