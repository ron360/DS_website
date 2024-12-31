package com.google.demo.Service;

import java.util.Comparator;

public class PageComparator implements Comparator<WebPage>
{
	@Override
	public int compare(WebPage o1, WebPage o2)
	{
		if (o1 == null || o2 == null)
			throw new NullPointerException();
		
		if (o1.getScore() > o2.getScore()) 
            return -1;
		else if (o1.getScore() < o2.getScore()) 
        	return 1; 
        else 
            return 0; 
      
	}
}
