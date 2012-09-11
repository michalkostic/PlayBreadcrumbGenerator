package com.touchqode.editor.autocomplete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrefixTree
{
	private  PTreeNode root = new PTreeNode();
	
	public void addWord(String word, boolean forceRecalculateTerminals)
	{
		PTreeNode currNode = root;
		for (int j=0; j<word.length(); j++)
		{
			char currChar = word.charAt(j);
			PTreeNode nextNode = currNode.getNext(currChar); 
			if (nextNode==null)
			{
				nextNode = new PTreeNode();
				currNode.addNext(currChar, nextNode);
			}
			currNode = nextNode;
		}		
		currNode.terminal = true;
		if (forceRecalculateTerminals)
		{
			recalculateTerminals();
		}
	}
	
	public void loadWords(List<String> words)
	{
		for (int i=0; i<words.size(); i++)
		{
			PTreeNode currNode = root;
			String currWord = words.get(i);
			for (int j=0; j<currWord.length(); j++)
			{
				char currChar = currWord.charAt(j);
				PTreeNode nextNode = currNode.getNext(currChar); 
				if (nextNode==null)
				{
					nextNode = new PTreeNode();
					currNode.addNext(currChar, nextNode);
				}
				currNode = nextNode;
			}
			currNode.terminal = true;
		}
		recalculateTerminals();
	}
	
	public void recalculateTerminals()
	{
		root.recalculateTerminalChildren();
	}
	
	public PTreeNode getLongestPrefix(PTreeNode startingNode, List<PTreeNode> terminals)
	{
		//StringBuilder prefix = new StringBuilder();
		PTreeNode node = startingNode;
		while (node.children.size()==1)
		{			
			node = node.childrenList.get(0);
			//Log.i("AutocompleteEngine", "currNode:" + node.getText());
			if (node.isTerminal() && terminals!=null && node.children.size()==1)
			{
				//Log.i("AutocompleteEngine", "addingTerminal:" + node.getText());
				terminals.add(node);
			}
		}
		//Log.i("AutocompleteEngine", "furthestPrefix:" + node.getText());
		return node;
	}
	
	public PTreeNode getNodeForPrefix(CharSequence prefix)
	{
		int index = 0;
		PTreeNode currNode = root;
				
		while (index<prefix.length() && currNode!=null && currNode.children.containsKey(prefix.charAt(index)))
		{
			currNode = currNode.getNext(prefix.charAt(index));
			index++;
		}
		if (index>=prefix.length())
		{
			// found node
			return currNode;
		}
		else
		{
			return null;
		}
		
	}
	
	public class PTreeNode
	{
		private PTreeNode parent = null; 
		private HashMap<Character, PTreeNode> children = new HashMap<Character, PTreeNode>();
		private ArrayList<PTreeNode> childrenList = new ArrayList<PTreeNode>(); 
		private int terminalChildrenCount = 0;
		private boolean terminal = false;
		private String text = "";
		
		public PTreeNode getNext(char nextSequence)
		{
			if (children.containsKey(nextSequence))
			{
				return children.get(nextSequence);
			}
			else
			{
				return null;
			}
		}
		
		public void addNext(char nextChar, PTreeNode node)
		{
			children.put(nextChar, node);
			childrenList.add(node);
			node.parent = this;
			node.text = this.text + nextChar;
		}
		
		public int recalculateTerminalChildren()
		{
			terminalChildrenCount = 0;
			for (PTreeNode currChild:children.values())
			{
				terminalChildrenCount += currChild.recalculateTerminalChildren();
			}
			if (terminal)
			{
				terminalChildrenCount++;
			}
			return terminalChildrenCount;
		}
		
		public String getText()
		{
			return text;
		}
		
		public List<PTreeNode> getChildrenList()
		{
			return childrenList;
		}

		public boolean isTerminal() {
			return terminal;
		}

		public void setTerminal(boolean terminal) {
			this.terminal = terminal;
		}
	}

	public PTreeNode getRoot() {
		return root;
	}
	
	public static PrefixTree buildSampleSuggestionTree(String relevantPrefix)
	{
		//Log.i("AutocompleteEngine", "buildSuggestionTree:" + relevantPrefix + " " + selectionStartIndex);
		relevantPrefix = relevantPrefix.toLowerCase();
		PrefixTree suggestionTree = new PrefixTree();		
		String[] suggestions = new String[] {"public", "private", "protected", "void"};
		for (String suggestion:suggestions)
		{
			if (suggestion.toLowerCase().startsWith(relevantPrefix))
			{
				//relevantSuggestions.add(suggestion);
				suggestionTree.addWord(suggestion, false);
			}
		}		
		suggestionTree.recalculateTerminals();
		return suggestionTree;
	}

}
