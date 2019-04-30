/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package leap.core.ioc;

import leap.core.annotation.Configurable;
import leap.lang.Classes;
import leap.lang.Strings;
import leap.lang.beans.BeanType;
import leap.lang.tostring.ToStringBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("rawtypes")
class BeanDefinitionBase implements BeanDefinition,TypeDefinition,BeanDefinitionConfigurator {
	
	protected final Object source;
    protected final Object singletonLock = new Object();

    protected boolean override        = true;
    protected boolean defaultOverride = false;

    protected String            namespace;
    protected String            id;
    protected String            name;
    protected Class<?>          type;
    protected Class<?>          beanClass;
    protected BeanType          beanClassType;
    protected Method            initMethod;
    protected Method            destroyMethod;
    protected boolean           singleton;
    protected boolean           lazyInit;
    protected boolean           primary;
    protected Constructor       constructor;
    protected Boolean           configurable;
    protected boolean			annotation;
    protected boolean			overrideAnnotation;
    protected String            configurationPrefix;
    protected boolean           exportMBean;
    protected String            MBeanName;
    protected FactoryDefinition factoryDefinition;

    protected List<TypeDefinition>        additionalTypeDefs   = new ArrayList<>();
    protected List<FactoryBeanDefinition> factoryBeanDefs      = new ArrayList<>();
    protected List<ArgumentDefinition>    constructorArguments = new ArrayList<>();
    protected List<PropertyDefinition>    properties           = new ArrayList<>();
    protected List<InvokeDefinition>      invokes              = new ArrayList<>();
    protected Set<String>                 qualifiers           = new LinkedHashSet<>();
    protected ValueDefinition          valueDefinition;
	
	protected Object singletonInstance;
    protected Object proxyInstance;
	protected float  sortOrder = DEFAULT_SORT_ORDER;

    protected boolean inited;
	
	public BeanDefinitionBase(Object source){
		this.source = source;
	}

    public Object getSingletonLock() {
        return singletonLock;
    }

    @Override
	public BeanDefinition definition() {
		return this;
	}

	@Override
    public Object getSource() {
	    return source;
    }
	
	@Override
    public float getSortOrder() {
	    return sortOrder;
    }

	@Override
	public void setSortOrder(float sortOrder) {
		this.sortOrder = sortOrder;
	}

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String getId() {
		return id;
	}

	@Override
    public String getName() {
		return name;
	}
	
	public String getIdOrName(){
		return Strings.firstNotEmpty(id,name);
	}

	protected void setId(String id) {
		this.id = id;
	}
	
	protected void setName(String name) {
		this.name = name;
	}
	
	public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public FactoryDefinition getFactoryDefinition() {
        return factoryDefinition;
    }

    public void setFactoryDefinition(FactoryDefinition factoryDefinition) {
        this.factoryDefinition = factoryDefinition;
    }

    public ValueDefinition getValueDefinition() {
		return valueDefinition;
	}

	protected void setValueDefinition(ValueDefinition value) {
		this.valueDefinition = value;
	}

	public Method getInitMethod() {
		return initMethod;
	}

	protected void setInitMethod(Method initMethod) {
		this.initMethod = initMethod;
	}
	
	public Method getDestroyMethod() {
		return destroyMethod;
	}

	protected void setDestroyMethod(Method destroyMethod) {
		this.destroyMethod = destroyMethod;
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}

	protected void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}
	
	public BeanType getBeanClassType() {
		return beanClassType;
	}

	public Set<String> getQualifiers() {
		return qualifiers;
	}

	public void addQualifier(String qualifier) {
		this.qualifiers.add(qualifier);
	}

	protected void setBeanClassType(BeanType beanType) {
		this.beanClassType = beanType;
	}
	
	@Override
	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public boolean isOverride() {
		return override;
	}

	public void setOverride(boolean overried) {
		this.override = overried;
	}

    public boolean isDefaultOverride() {
        return defaultOverride;
    }

    public void setDefaultOverride(boolean defaultOverride) {
        this.defaultOverride = defaultOverride;
    }

    public boolean isLazyInit() {
		return lazyInit;
	}

	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}

	@Override
    public boolean isPrimary() {
		return primary;
	}

	protected void setPrimary(boolean primary) {
		this.primary = primary;
	}

    @Override
    public boolean isExportMBean() {
        return exportMBean;
    }

    public void setExportMBean(boolean exportMBean) {
        this.exportMBean = exportMBean;
    }

    @Override
    public String getMBeanName() {
        return MBeanName;
    }

    public void setMBeanName(String MBeanName) {
        this.MBeanName = MBeanName;
    }

	public Constructor getConstructor() {
		return constructor;
	}

	protected void setConstructor(Constructor constructor) {
		this.constructor = constructor;
	}

	public List<ArgumentDefinition> getConstructorArguments() {
		return constructorArguments;
	}
	
	public void addConstructorArgument(ArgumentDefinition argumentDefinition){
		constructorArguments.add(argumentDefinition);
	}
	
	public List<TypeDefinition> getAdditionalTypeDefs() {
	    return additionalTypeDefs;
	}

	public void addAdditionalTypeDef(TypeDefinition def) {
	    additionalTypeDefs.add(def);
	}
	
	public List<FactoryBeanDefinition> getFactoryBeanDefs() {
	    return factoryBeanDefs;
	}
	
	public void addFactoryBeanDef(FactoryBeanDefinition def) {
	    factoryBeanDefs.add(def);
	}
	
	public List<PropertyDefinition> getProperties() {
		return properties;
	}

	public void addProperty(PropertyDefinition propertyDefinition){
		properties.add(propertyDefinition);
	}
	
	public List<InvokeDefinition> getInvokes() {
		return invokes;
	}
	
	public void addInvoke(InvokeDefinition invokeDefinition){
		invokes.add(invokeDefinition);
	}

	public boolean isValued(){
		return null != valueDefinition;
	}

    /**
     * Returns the proxy instance or the singleton instance.
     */
    public Object getInstance() {
        return null != proxyInstance ? proxyInstance : singletonInstance;
    }
	
	public Object getSingletonInstance() {
		return singletonInstance;
	}

	protected void setSingletonInstance(Object resolvedValue) {
		this.singletonInstance = resolvedValue;
	}

    public boolean hasProxy() {
        return null != proxyInstance;
    }

    public Object getProxyInstance() {
        return proxyInstance;
    }

    public void setProxyInstance(Object proxyInstance) {
        this.proxyInstance = proxyInstance;
    }

    public boolean isListBean(){
		return isTypeOf(List.class);
	}
	
	public boolean isSetBean(){
		return isTypeOf(Set.class);
	}
	
	public boolean isMapBean(){
		return isTypeOf(Map.class);
	}

    public boolean isInited() {
        return inited;
    }

    public void setInited(boolean inited) {
        this.inited = inited;
    }

    public boolean isConfigurable() {
		if(null == configurable) {
			configurable = Classes.isAnnotationPresent(beanClass,Configurable.class);
		}
		return configurable;
	}

	@Override
	public boolean isAnnotation() {
		return annotation;
	}

	@Override
	public boolean isOverrideAnnotation() {
		return overrideAnnotation;
	}

	public void setAnnotation(boolean annotation) {
		this.annotation = annotation;
	}

	public void setOverrideAnnotation(boolean overrideAnnotation) {
		this.overrideAnnotation = overrideAnnotation;
	}

	@Override
    public void setConfigurable(boolean b) {
        this.configurable = b;
    }

    public String getConfigurationPrefix() {
		if(null == configurationPrefix) {
			Configurable a = beanClass.getAnnotation(Configurable.class);
			configurationPrefix = null == a ? "" : a.prefix();
		}
		return configurationPrefix;
	}

	@Override
	public void setConfigurationPrefix(String prefix) {
		this.configurationPrefix = prefix;
	}

	protected boolean isTypeOf(Class<?> beanType){
	    if(null != type && type.equals(beanType)) {
	        return true;
	    }
	    
	    for(TypeDefinition td : additionalTypeDefs) {
	        if(td.getType().equals(beanType)) {
	            return true;
	        }
	    }
	    
	    return false;
	}
	
	@Override
    public String toString() {
		ToStringBuilder sb = new ToStringBuilder();
		
		sb.append("class", beanClass);
		  
		if(!Strings.isEmpty(id)) {
		    sb.append("id", id);
		}
		
		if(!Strings.isEmpty(name)) {
		    sb.append("name", name);
		}
		
		if(null != source) {
		    sb.append("source", source);
		}
		
		return sb.toString();
    }
}