package com.elorza.consolelauncher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Resources {
	
	public final static String VERSION = "1.2.4.7";
	
	public static String GetShortcuts(Context c){
		File f = null;
		if(!(f = new File(c.getFilesDir().getAbsolutePath()+"/consolelauncher")).exists())
			f.mkdir();
		if(!(f = new File(c.getFilesDir().getAbsolutePath()+"/consolelauncher/shortcut.txt")).exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				return "";
			}
		}
		return c.getFilesDir().getAbsolutePath()+"/consolelauncher/shortcut.txt";
	}
	
	public static List<String[]> ReadShortcuts(Context c){
		try {
			FileReader rd = new FileReader(GetShortcuts(c));
			BufferedReader bu = new BufferedReader(rd);
			List<String[]> cu = new ArrayList<String[]>();
			String line = "";
			boolean on = false;
			String pre = "";
			while((line = bu.readLine()) != null){
				if(on){
					cu.add(new String[]{pre,line});
					on = false;
				}else{
					pre = line;
					on = true;
				}
			}
			bu.close();
			rd.close();
			return cu;
		} catch (Exception e) {
			return new ArrayList<String[]>();
		}
	}
	
	public static Bitmap CreateImage(int width, int height, int color) {
	    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap);
	    Paint paint = new Paint();
	    paint.setColor(color);
	    canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
	    return bitmap;
	}
	
	public static int ParseColor(String c){
		if(c.equals("red")){
			return Color.RED;
		}else if(c.equals("green")){
			return Color.GREEN;
		}else if(c.equals("blue")){
			return Color.BLUE;
		}else if(c.equals("white")){
			return Color.WHITE;
		}else if(c.equals("black")){
			return Color.BLACK;
		}else{
			return Color.WHITE;
		}
	}
}
