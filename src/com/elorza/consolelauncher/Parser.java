package com.elorza.consolelauncher;

import android.annotation.SuppressLint;
import java.util.List;
import java.util.StringTokenizer;

public class Parser {
	
	private String[] tokens;
	private String line;
	private Players player;
	private List<String[]> shortcuts;
	
	public Parser(String line, Players pls, List<String[]> s){
		this.line = line;
		StringTokenizer tk = new StringTokenizer(line);
		tokens = new String[tk.countTokens()];
		int x = 0;
		while(tk.hasMoreTokens()){
			tokens[x] = tk.nextToken();
			x++;
		}
		shortcuts = s;
		player = pls;
	}
	
	private boolean AShortCutContain(String token){
		for(String[] a : shortcuts){
			if(a[0].equals(token))
				return true;
		}
		return false;
	}
	
	private String AShortCutIn(String token){
		for(String[] a : shortcuts){
			if(a[0].equals(token))
				return a[1];
		}
		return "";
	}
	
	@SuppressLint("DefaultLocale")
	public String Run(){
		for(Player p : player.CommandList){
			if(p.KeyWord().equals(tokens[0].toLowerCase())){
				if(p.Requeriments() == tokens.length || p.Requeriments() == -2){
					return p.Run(tokens);
				}else if(p.Requeriments() == -1){
					return p.Run(new String[]{line});
				}else{
					return "Error: Numero incorrecto de argumentos: " + tokens.length + "/" + p.Requeriments() + "\n";
				}
			}else if(AShortCutContain(tokens[0]))
				return new Parser(AShortCutIn(tokens[0]), player, shortcuts).Run();
		}
		return "Error: Comando erroneo: " + tokens[0] + "\n";
	}
}
