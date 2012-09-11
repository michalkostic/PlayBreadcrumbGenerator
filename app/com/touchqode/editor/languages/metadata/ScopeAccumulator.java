/**
 * 
 */
package com.touchqode.editor.languages.metadata;

import java.util.ArrayList;
import java.util.LinkedList;

import com.touchqode.editor.autocomplete.model.Scope;
import com.touchqode.editor.autocomplete.model.Symbol;


public class ScopeAccumulator
{
	public LinkedList<Scope> currentContextPath = new LinkedList<Scope>();
	public int currScopeLevel = 0;
	public ArrayList<Scope> scopes = new ArrayList<Scope>();
    //public Layout layout;
	
    public boolean consumeNextBlockScope = false;
	protected LinkedList<Symbol> symbols = new LinkedList<Symbol>();
    
	public void clear()
	{
		currScopeLevel = 0;
		scopes.clear();
		symbols.clear();
		currentContextPath.clear();
		consumeNextBlockScope = false;
		
	}
    
    
    public void moveScopeUp()
    {
    	currentContextPath.removeLast();
    	currScopeLevel--;
    }
 
    public void addScope(Scope scope)
    {    	
    	currentContextPath.addLast(scope);
    	scopes.add(scope);
    	currScopeLevel++;    	    	
    }

}