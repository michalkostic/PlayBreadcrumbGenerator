package com.touchqode.editor.autocomplete.model.dao;

import java.util.ArrayList;
import java.util.LinkedList;

import com.touchqode.editor.autocomplete.model.Scope;
import com.touchqode.editor.languages.metadata.ScopeAccumulator;

public class ScopeDisjointer {
	
	LinkedList<Scope> splitScopesStack = new LinkedList<Scope>();
	
	public ArrayList<Scope> makeDisjoint(ScopeAccumulator scopeAccumulator)
	{
		ArrayList<Scope> scopes = scopeAccumulator.scopes;		
		//return makeDisjoint(scopes, 0);
//		ArrayList<Scope> disjointScopes = new ArrayList<Scope>();
//		makeDisjointIter(scopes, 0, disjointScopes);
//		return disjointScopes;
		
		return makeDisjoint2(scopes, 0);
	}

	public ArrayList<Scope> makeDisjoint(ArrayList<Scope> scopes)
	{
		//return makeDisjoint(scopes, 0);
//		ArrayList<Scope> disjointScopes = new ArrayList<Scope>();
//		makeDisjointIter(scopes, 0, disjointScopes);
//		return disjointScopes;
		return makeDisjoint2(scopes, 0);
	}
		
	private Scope pivotScope = null;
	private LinkedList<Scope> scopesBefore = new LinkedList<Scope>();
	private LinkedList<Scope> scopesAfter = new LinkedList<Scope>();
	
	public static boolean isScopeInPivot(Scope newScope, Scope pivotScope)
	{
		return (newScope.startIndex>=pivotScope.startIndex && newScope.endIndex<=pivotScope.endIndex);
	}

	public static boolean isScopeBeforePivot(Scope newScope, Scope pivotScope)
	{
		return (newScope.endIndex<=pivotScope.startIndex);
	}

	public static boolean isScopeAfterPivot(Scope newScope, Scope pivotScope)
	{
		return pivotScope.endIndex<=newScope.startIndex;
	}

	
	public ArrayList<Scope> makeDisjoint2(ArrayList<Scope> scopes, int scopeIndex)
	{
		for (int i=0; i<scopes.size(); i++)
		{
			Scope newScope =  scopes.get(i);
			if (pivotScope==null)
			{
				pivotScope = newScope;
				continue;
			}
			else
			{
				// scope is after current pivot
				while (isScopeAfterPivot(newScope, pivotScope) && scopesAfter.size()>0)
				{
					scopesBefore.addLast(pivotScope);
					pivotScope = scopesAfter.removeFirst();					
				}
				
				// scope is after all previously seen scopes => is disjoint, just add as pivot
				if (scopesAfter.size()==0 && isScopeAfterPivot(newScope, pivotScope))
				{
					scopesBefore.addLast(pivotScope);
					pivotScope = newScope;
					continue;
				}
				
				if (isScopeInPivot(newScope, pivotScope))
				{
					splitScope(pivotScope, newScope, scopesBefore, scopesAfter);
					pivotScope = newScope;
					continue;
				}
				
				// probably should never happen
				if (isScopeBeforePivot(newScope, pivotScope))
				{
					while (isScopeBeforePivot(newScope, pivotScope) && scopesBefore.size()>0)
					{
						scopesAfter.addFirst(pivotScope);
						pivotScope = scopesBefore.removeFirst();					
					} 
					
					// scope is after all previously seen scopes => is disjoint, just add as pivot
					if (scopesBefore.size()==0 && isScopeBeforePivot(newScope, pivotScope))
					{
						scopesAfter.addFirst(pivotScope);
						pivotScope = newScope;
						continue;
					}
				}
			}
		}
		ArrayList<Scope> disjointScopes = new ArrayList<Scope>();
		disjointScopes.addAll(scopesBefore);
		if (pivotScope!=null) { disjointScopes.add(pivotScope); }
		disjointScopes.addAll(scopesAfter);
		return disjointScopes;
	}
	

	public static void splitScope(Scope outerScope, Scope innerScope, LinkedList<Scope> scopesBefore, LinkedList<Scope> scopesAfter)
	{
		Scope outerPart1 = outerScope.cloneWithoutSymbols();
		Scope outerPart2 = outerScope.cloneWithoutSymbols();
		outerPart1.endIndex = innerScope.startIndex;
		outerPart2.startIndex = innerScope.endIndex;
		scopesBefore.addLast(outerPart1);
		scopesAfter.addLast(outerPart2);
		return ;
	}

	
}
