package com.elorza.consolelauncher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.BatteryManager;
import android.util.Log;

public class Players {
	public Player[] CommandList = new Player[]{new RunApp(), new ListApps(),
			new Echo(), new Settings(),new Status(), new Clear(), new Inspect(),
			new Phone(), new Call(), new WallPaper(), new Autor(), new Find(),
			new Shortcut(), new Shortcuts(), new RemoveShortcuts(), new Version()};
	private Context c;
	private Intent i;
	private MultiEditor m;
	
	public Players(Context context, MultiEditor multieditor){
		c = context;
		i = ((Activity)c).getIntent();
		this.m = multieditor;
	}
	
	public class Version implements Player{

		@Override
		public int Requeriments() {
			return 1;
		}

		@Override
		public String KeyWord() {
			return "version";
		}

		@Override
		public String Run(String[] req) {
			return Resources.VERSION;
		}
	}
	
	public class RemoveShortcuts implements Player{

		@Override
		public int Requeriments() {
			return 1;
		}

		@Override
		public String KeyWord() {
			return "remove";
		}

		@Override
		public String Run(String[] req) {
			new File(Resources.GetShortcuts(c)).delete();
			return "";
		}
	}
	
	public class Shortcuts implements Player{

		@Override
		public int Requeriments() {
			return 1;
		}

		@Override
		public String KeyWord() {
			return "shortcuts";
		}

		@Override
		public String Run(String[] req) {
			String tr = "";
			for(String[] i : m.GetShortCuts())
				tr += i[0] + " -> " + i[1] + "\n";
			return tr;
		}
	}
	
	public class Shortcut implements Player{

		@Override
		public int Requeriments() {
			return -1;
		}

		@Override
		public String KeyWord() {
			return "shortcut";
		}

		@Override
		public String Run(String[] req) {
			String ln = req[0].substring(8);
			if(ln.length() > 0){
				if(ln.split(" ").length > 1){
					ln = ln.substring(1);
					String name = ln.split(" ")[0];
					String line = ln.substring(name.length());
					String path = Resources.GetShortcuts(c);
					if(path.equals(""))
						return "Error al guardar el atajo";
					try {
						FileWriter f = new FileWriter(path,true);
						BufferedWriter b = new BufferedWriter(f);
						PrintWriter o = new PrintWriter(b);
						o.println(name);
						o.println(line);
						o.close();
						b.close();
						f.close();
						m.AddShortCut(new String[]{name,line});
						return "";
					} catch (IOException e) {
						return "Error al guardar el atajo";
					}
				}
				return "Error: Faltan argumentos\n";
			}
			return "Error: Faltan argumentos\n";
		}
	}
	
	public class Find implements Player{

		@Override
		public int Requeriments() {
			return -2;
		}

		@Override
		public String KeyWord() {
			return "find";
		}

		@Override
		public String Run(String[] req) {
			try{
				final PackageManager pm = c.getPackageManager();
				List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
				List<String> d = new LinkedList<String>(Arrays.asList(req));
				d.remove(0);
				String rt = "";
				for(String key : d){
					for (ApplicationInfo p : packages) {
			    		String a = p.packageName;
			    		String b = (String)(p != null ? pm.getApplicationLabel(p) : "(unknown)");
			    		if(a.contains(key) || b.contains(key))
			    			rt += "- " + a + "(" + b + ")\n";
					}
				}
				return rt;
			}catch(Exception e){
				return "Error: " + Log.getStackTraceString(e);
			}
		}
	}
	
	public class Autor implements Player{

		@Override
		public int Requeriments() {
			return 1;
		}

		@Override
		public String KeyWord() {
			return "autor";
		}

		@Override
		public String Run(String[] req) {
			return "\n****Alberto Elorza Rubio****\n\n";
		}
	}
	
	public class WallPaper implements Player{

		@Override
		public int Requeriments() {
			return 3;
		}

		@Override
		public String KeyWord() {
			return "wallpaper";
		}

		@Override
		public String Run(String[] req) {
			Bitmap b = null;
			if(req[1].equals("color")){
				b = Resources.CreateImage(10, 10, Resources.ParseColor(req[2]));
			}else if(req[1].equals("file")){
				b = BitmapFactory.decodeFile(req[2]);
			}
			try{
				WallpaperManager.getInstance(c).setBitmap(b);
			}catch(Exception e){
				return "Error: " + Log.getStackTraceString(e);
			}
			return "";
		}
	}
	
	public class Call implements Player{

		@Override
		public int Requeriments() {
			return 2;
		}

		@Override
		public String KeyWord() {
			return "call";
		}

		@Override
		public String Run(String[] req) {
			Intent intent = new Intent(Intent.ACTION_DIAL);
	        intent.setData(Uri.parse("tel:" + req[1]));
	        c.startActivity(intent);
			return "";
		}
	}
	
	public class Phone implements Player{

		@Override
		public int Requeriments() {
			return 1;
		}

		@Override
		public String KeyWord() {
			return "phone";
		}

		@Override
		public String Run(String[] req) {
			c.startActivity(new Intent("android.intent.action.DIAL"));
			return "";
		}
	}
	
	public class Inspect implements Player{

		@Override
		public int Requeriments() {
			return 2;
		}

		@Override
		public String KeyWord() {
			return "inspect";
		}

		@Override
		public String Run(String[] req) {
			try {
				PackageInfo pi = c.getPackageManager().getPackageInfo(req[1], PackageManager.GET_ACTIVITIES);
				String tr = "";
				for(ActivityInfo p : pi.activities)
				tr += "- " + p.name + "\n";
				return tr;
			} catch (NameNotFoundException e) {
				return "Error: " + Log.getStackTraceString(e);
			}
			
		}
		
	}
	
	public class Clear implements Player{

		@Override
		public int Requeriments() {
			return 1;
		}

		@Override
		public String KeyWord() {
			return "clear";
		}

		@Override
		public String Run(String[] req) {
			m.Clear();
			return "";
		}
	}
	
	public class Status implements Player{

		@Override
		public int Requeriments() {
			return 1;
		}

		@Override
		public String KeyWord() {
			return "status";
		}

		@Override
		public String Run(String[] req) {
			return "Battery: "+String.valueOf(i.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)/(float)i.getIntExtra(BatteryManager.EXTRA_SCALE, -1))+"%\n";
		}
	}
	
	public class Settings implements Player{

		@Override
		public int Requeriments() {
			return 1;
		}

		@Override
		public String KeyWord() {
			return "settings";
		}

		@Override
		public String Run(String[] req) {
			c.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
			return "";
		}
		
	}
	
	public class Echo implements Player{

		@Override
		public int Requeriments() {
			return -1;
		}

		@Override
		public String KeyWord() {
			return "echo";
		}

		@Override
		public String Run(String[] req) {
			if(req[0].length()>4)
				return "\n"+req[0].substring(5)+"\n\n";
			return "\n"+req[0].substring(4)+"\n\n";
		}
		
	}
	
	public class RunApp implements Player{
		
		@Override
		public int Requeriments() {
			return 2;
		}

		@Override
		public String KeyWord() {
			return "run";
		}

		@Override
		public String Run(String[] req) {
			Intent launchIntent = c.getPackageManager().getLaunchIntentForPackage(req[1]);
			if (launchIntent != null) { 
			    c.startActivity(launchIntent);
			}
			return "";
		}
	}

	public class ListApps implements Player{

		@Override
		public int Requeriments() {
			return 1;
		}

		@Override
		public String KeyWord() {
			return "list";
		}

		@Override
		public String Run(String[] req) {
			final PackageManager pm = c.getPackageManager();
			List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
			String s = "";
			for (ApplicationInfo p : packages) {
			    s += "- " + p.packageName + "(" + (String)(p != null ? pm.getApplicationLabel(p) : "(unknown)") + ")\n";
			}
			return s;
		}
	}
}
