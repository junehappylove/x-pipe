package com.ctrip.xpipe.lifecycle;

import java.util.HashMap;
import java.util.Map;

import com.ctrip.xpipe.api.lifecycle.ComponentRegistry;
import com.ctrip.xpipe.api.lifecycle.Lifecycle;

/**
 * @author wenchao.meng
 *
 * Jun 17, 2016
 */
public class DefaultRegistry extends AbstractComponentRegistry{
	
	private ComponentRegistry createdRegistry; 
	private ComponentRegistry springRegistry;
	
	public DefaultRegistry(ComponentRegistry createdRegistry, ComponentRegistry springRegistry) {
		
		this.createdRegistry = createdRegistry;
		this.springRegistry = springRegistry;
	}

	@Override
	public Object getComponent(String name) {
		
		Object component = null;
		
		if(springRegistry != null){
			component = springRegistry.getComponent(name);
		}
		
		if(component == null){
			component = createdRegistry.getComponent(name);
		}
		return component;
	}

	@Override
	protected void doAdd(String name, Object component) throws Exception {
		createdRegistry.add(name, component);
	}

	@Override
	protected String doAdd(Object component) throws Exception {
		
		return createdRegistry.add(component);
	}

	@Override
	protected Object doRemoveOfName(String name) {
		return createdRegistry.removeOfName(name);
	}

	@Override
	protected boolean doRemove(Object component) throws Exception {
		return createdRegistry.remove(component);
	}

	@Override
	protected <T> Map<String, T> doGetComponents(Class<T> clazz) {
		
		Map<String, T> result = new HashMap<>(createdRegistry.getComponents(clazz));
		if(springRegistry != null){
			result.putAll(springRegistry.getComponents(clazz));
		}
		return result;
	}

	@Override
	public Map<String, Object> allComponents() {
		
		Map<String, Object> result = new HashMap<>(createdRegistry.allComponents());
		if(springRegistry != null){
			result.putAll(springRegistry.allComponents());
		}
		return result;
	}

	@Override
	public Map<String, Lifecycle> lifecycleCallable() {
		
		Map<String, Lifecycle>  result = new HashMap<>();
		result.putAll(createdRegistry.lifecycleCallable());
		if(springRegistry != null){
			result.putAll(springRegistry.lifecycleCallable());
		}
		return result;
	}
	
	@Override
	protected void doInitialize() throws Exception {
		
		createdRegistry.initialize();
		if(springRegistry != null){
			springRegistry.initialize();
		}
	}
	
	@Override
	protected void doStart() throws Exception {
		
		createdRegistry.start();
		if(springRegistry != null){
			springRegistry.start();
		}
	}
	
	@Override
	protected void doStop() throws Exception {
		
		createdRegistry.stop();
		if(springRegistry != null){
			springRegistry.stop();
		}
	}
	
	@Override
	protected void doDispose() throws Exception {

		createdRegistry.dispose();
		if(springRegistry != null){
			springRegistry.dispose();
		}
	}
}
