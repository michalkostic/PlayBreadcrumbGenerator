package com.touchqode.editor.autocomplete.model;

import java.util.List;

public class Type {
	public int typeId;
	public String typePackage;
	public String name;
	public List<Symbol> members;
	
	public String getCanonicalName()
	{
		if (typePackage!=null && "".equals(typePackage))
		{
			return typePackage + "." + name;	
		}
		else
		{
			return name;
		}
		
	}
}
