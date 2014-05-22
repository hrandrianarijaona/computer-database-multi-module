package com.excilys.core.om;

import java.util.Map;

public class ThreadLocalPerso<T> {

	private Map<Long, T> content;
	
	public ThreadLocalPerso() {
		// TODO Auto-generated constructor stub
	}
	
	public T get(){
		Thread t = Thread.currentThread();
		return ((T) content.get(t.getId()));
	}
	
	public void set(T obj){
		Thread t = Thread.currentThread();
		content.put(t.getId(), obj);
	}
	
	public void remove(){
		Thread t = Thread.currentThread();
		content.remove(t.getId());
	}

}
