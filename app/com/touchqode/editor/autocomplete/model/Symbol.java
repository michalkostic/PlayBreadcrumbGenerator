package com.touchqode.editor.autocomplete.model;

public class Symbol {
	public long symbolId;
	public String symbolType;
	public Scope scope;
	public String name;
	public Type type;
	public String visibility;
	public int startIndex;
	public int endIndex;
	public int declarationIndex;	// used for local variables - valid only after they are declared
	
	public static final String SYMBOL_TYPE_MEMBER = "member";
	public static final String SYMBOL_TYPE_LOCAL_VARIABLE = "variable";
	public static final String SYMBOL_TYPE_STATIC_MEMBER = "static member";
	public static final String SYMBOL_TYPE_PARAMETER = "parameter";
	public static final String SYMBOL_TYPE_NA = "n/a";
}
