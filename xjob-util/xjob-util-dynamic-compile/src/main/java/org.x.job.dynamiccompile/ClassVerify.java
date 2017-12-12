package org.x.job.dynamiccompile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * class验证缓存类.
 * 
 * @author ShawnShoper
 *
 */
public class ClassVerify
{
	public VolatileObject<Map<String, Object>> clazzVerify = new VolatileObject<Map<String, Object>>(
			new ConcurrentHashMap<String, Object>());

	public Object getValue(String className)
	{
		if (clazzVerify.getObject().containsKey(className))
		{
			return clazzVerify.getObject().get(className);
		}
		return null;
	}
	public void putClazzVerify(String className, Object verify)
	{
		this.clazzVerify.getObject().put(className, verify);
	}

}
