package gui;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;
import jason.asSyntax.StringTermImpl;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.*;
import org.json.JSONArray;

/** internal action that allow agents to translate strings */

public class translating extends DefaultInternalAction {


    @Override
    public Object execute(final TransitionSystem ts, Unifier un, Term[] args) throws Exception {

        // get the receiver
		String langFrom = ((StringTerm)args[0]).getString();	
		
		// get the subject
        String langTo = ((StringTerm)args[1]).getString();	
		
		// get the message	
		String msg = ((StringTerm)args[2]).getString();	
			
		// inicializo la traducción
		String word = "I have no translation for that";

        try {
			word = callUrlAndParseResult(langFrom, langTo, msg);
			System.out.println(word);		
		} catch (Exception eLabel) {
			eLabel.printStackTrace();
		};
				
        //return true;
		StringTerm result = new StringTermImpl(word);
		return un.unifies(result, args[3]);
    }
 
 private String callUrlAndParseResult(String langFrom, String langTo,
                                             String word) throws Exception 
 {

  String url = "https://translate.googleapis.com/translate_a/single?"+
    "client=gtx&"+
    "sl=" + langFrom + 
    "&tl=" + langTo + 
    "&dt=t&q=" + URLEncoder.encode(word, "UTF-8");    
  
  URL obj = new URL(url);
  HttpURLConnection con = (HttpURLConnection) obj.openConnection(); 
  con.setRequestProperty("User-Agent", "Mozilla/5.0");
 
  BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
  String inputLine;
  StringBuffer response = new StringBuffer();
 
  while ((inputLine = in.readLine()) != null) {
   response.append(inputLine);
  }
  in.close();
 
  return parseResult(response.toString());
  //return response.toString();
 }
 
private String parseResult(String inputJson) throws Exception
 {
  
  JSONArray jsonArray = new JSONArray(inputJson);
  JSONArray jsonArray2 = (JSONArray) jsonArray.get(0);
  JSONArray jsonArray3 = (JSONArray) jsonArray2.get(0);
  
  return jsonArray3.get(0).toString();
 }

}
