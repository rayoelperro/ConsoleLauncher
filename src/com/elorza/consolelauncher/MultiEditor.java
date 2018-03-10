package com.elorza.consolelauncher;

import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MultiEditor implements TextWatcher{
	
	private EditText t;
	private Context context;
	private Players players;
	private String header;
	private boolean noch;
	private String bef;
	private List<String[]> shortcuts;
	
	public MultiEditor(EditText t){
		this.t = t;
		header = "";
		context = t.getContext();
		bef = "";
		players = new Players(context,this);
		shortcuts = Resources.ReadShortcuts(context);
		t.addTextChangedListener(this);
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		bef = s.toString();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(noch){
			header = s.toString();
		}
		else if(!s.toString().startsWith(header)){
			noch = true;
			t.setText(header);
			noch = false;
			t.setSelection(t.getText().length());
		}
		else if(s.length() > 0 && s.toString().charAt(s.length()-1)=='\n'){
			header = s.toString();
			String f_[] = (bef+"a").split("\n");
			String s_[] = (s.toString()+"a").split("\n");
			if(!(f_.length == s_.length) && s.toString().split("\n").length>=1 && !s.toString().endsWith("\n\n"))
				Operate(s.toString().split("\n")[s.toString().split("\n").length-1]);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}
	
	private void Operate(String s){
		String rt = new Parser(s,players,shortcuts).Run();
		noch = true;
		t.setText(t.getText()+rt);
		noch = false;
		t.setSelection(t.getText().length());
	}

	public void AddShortCut(String[] srt){
		shortcuts.add(srt);
	}
	
	public List<String[]> GetShortCuts(){
		return shortcuts;
	}
	
	public void Clear() {
		noch = true;
		t.setText("");
		noch = false;
	}

}
