package com.project.timecard.Utils;

public class Pair<T,X>
{
	private final T data1;
	private final X data2;

	public Pair(T data1, X data2)
	{
		this.data1 = data1;
		this.data2 = data2;
	}

	public T getData1()
	{
		return data1;
	}
	public X getData2()
	{
		return data2;
	}
}
