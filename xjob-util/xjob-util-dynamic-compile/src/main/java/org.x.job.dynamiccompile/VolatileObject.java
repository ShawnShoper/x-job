package org.x.job.dynamiccompile;

import java.io.Serializable;

public class VolatileObject<T> implements Serializable
{
	private static final long serialVersionUID = -4935009486783465799L;
	private volatile T t;
	public VolatileObject(T t)
	{
		this.t = t;
	}
	public T getObject()
	{
		return t;
	}
	public void setObject(T t)
	{
		this.t = t;
	}
}