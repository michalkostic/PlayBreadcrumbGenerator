package com.touchqode.editor.autocomplete.model;

import java.util.ArrayList;

public class Scope {
	public long scopeId;
	public String scopeName;
	public String scopeType;
	public int startIndex;
	public int endIndex;
	public int level;
	public boolean inclusiveBegin = false;
	public boolean inclusiveEnd = false;
	public ArrayList<Symbol> symbols = new ArrayList<Symbol>(); 
	
	public Scope()
	{
		
	}

	public Scope cloneWithoutSymbols()
	{
		Scope newScope = new Scope();
		newScope.scopeId = -1;
		newScope.scopeName = scopeName;
		newScope.scopeType = scopeType;
		newScope.startIndex = startIndex;
		newScope.endIndex = endIndex;
		newScope.level = level;		
		return newScope;
	}
	
	public static final String SCOPE_TYPE_PACKAGE = "package";
	public static final String SCOPE_TYPE_CLASS = "class";
	public static final String SCOPE_TYPE_CONSTRUCTOR = "constructor";
	public static final String SCOPE_TYPE_METHOD = "method";
	public static final String SCOPE_TYPE_BLOCK = "{}";
	public static final String SCOPE_TYPE_NA = "n/a";
	public static final String SCOPE_TYPE_LANGUAGE_MODEL = "languageModel";
}
