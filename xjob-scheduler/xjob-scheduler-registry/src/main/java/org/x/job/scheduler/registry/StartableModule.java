package org.x.job.scheduler.registry;

/**
 * 启动项
 * 
 * @author ShawnShoper
 *
 */
public abstract class StartableModule
{
	// start module
	public abstract int start();
	// stop module
	public abstract void stop();
	// Module is started..
	private boolean started;
	public boolean isStarted()
	{
		return started;
	}
	public void setStarted(boolean started)
	{
		this.started = started;
	}
}
