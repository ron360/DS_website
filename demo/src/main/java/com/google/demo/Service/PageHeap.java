package com.google.demo.Service;

import java.util.PriorityQueue;

public class PageHeap
{
	private PriorityQueue<WebPage> heap;

	public PageHeap()
	{
		this.heap = new PriorityQueue<WebPage>(50, new PageComparator());
	}

	public void add(WebPage wp)
	{
		heap.offer(wp);
	}
	public WebPage poll()
	{
		WebPage wp = heap.poll();
		return wp;
	}
	public PriorityQueue<WebPage> getHeap(){
		return heap;
	}
}