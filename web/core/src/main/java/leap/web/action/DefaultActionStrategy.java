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
package leap.web.action;

import leap.core.AppConfigException;
import leap.core.BeanFactory;
import leap.core.annotation.Inject;
import leap.core.web.path.PathTemplate;
import leap.lang.Classes;
import leap.lang.Strings;
import leap.lang.http.HTTP;
import leap.lang.path.Paths;
import leap.lang.reflect.Reflection;
import leap.web.App;
import leap.web.annotation.*;
import leap.web.annotation.http.HttpMethod;
import leap.web.config.WebConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * The default implementation of {@link ActionStrategy}.
 */
public class DefaultActionStrategy implements ActionStrategy {

    public static final String   CONTROLLER_SUFFIX   = "Controller";
    public static final String   RESOURCE_SUFFIX     = "Resource";
    public static final String[] CONTROLLER_PACKAGES = new String[]{"controllers", "controller"};
    public static final String[] RESOURCE_PACKAGES   = new String[]{"resources", "resource"};

    protected @Inject App                     app;
    protected @Inject BeanFactory             factory;
    protected @Inject WebConfig               config;
    protected @Inject ActionStrategySupport[] supports;

	@Override
    public boolean isControllerClass(Class<?> cls) {
        for(ActionStrategySupport support : supports) {
            if(support.isExplicitNonController(cls)) {
                return false;
            }
        }

		if(Classes.isAnnotationPresent(cls, NonController.class)){
			return false;
		}

        if(cls.isInterface() || Modifier.isAbstract(cls.getModifiers())) {
            return false;
        }

        //must defines public constructor(s).
        if(!Modifier.isPublic(cls.getModifiers()) && cls.getConstructors().length == 0) {
            return false;
        }

        if(Classes.isAnnotationPresent(cls.getAnnotations(), Controller.class, true)) {
            return true;
        }

        String name = cls.getSimpleName();
        if(name.endsWith(CONTROLLER_SUFFIX) && name.length() > CONTROLLER_SUFFIX.length()) {
            return true;
        }

		return false;
    }

    @Override
    public boolean isActionMethod(ControllerInfo ci, Method m) {
        if(m.isAnnotationPresent(NonAction.class)) {
            return false;
        }

        if(!Modifier.isPublic(m.getModifiers())) {
            return false;
        }

        if(Modifier.isAbstract(m.getModifiers())) {
            return false;
        }

        if(Modifier.isStatic(m.getModifiers())) {
            return false;
        }

        if(ci.isRestful() && null == Classes.getAnnotationByMetaType(m.getAnnotations(), HttpMethod.class)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isIndexAction(ActionBuilder action) {
	    return Strings.equalsIgnoreCase(config.getIndexActionName(),action.getName()) ||
               Classes.isAnnotationPresent(action.getAnnotations(), Index.class);
    }

	@Override
    public String getControllerName(Class<?> cls) {
		int suffixStartIndex = cls.getSimpleName().indexOf(CONTROLLER_SUFFIX);
		if(suffixStartIndex > 0){
			return Strings.lowerUnderscore(cls.getSimpleName().substring(0,suffixStartIndex));
		}

        suffixStartIndex = cls.getSimpleName().indexOf(RESOURCE_SUFFIX);
        if(suffixStartIndex > 0) {
            return Strings.lowerUnderscore(cls.getSimpleName().substring(0,suffixStartIndex));
        }

        return Strings.lowerUnderscore(cls.getSimpleName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getControllerInstance(Class<?> cls) {
        try {
        	return factory.getOrAddBean(cls);
        } catch (Exception e) {
        	throw new AppConfigException("Error get or creating instance of controller '" + cls.getName() + "' : " + e.getMessage(), e);
        }
    }

	@Override
    public String[] getControllerPaths(Class<?> cls) {
        Class<?> parent = getParentControllerClass(cls);

        String   pathPrefix             = "";
        String   clsPackageName         = Classes.getPackageName(cls);

        String[] baseControllerPackages = controllerPackagePrefixes();
        for(String baseControllerPackage : baseControllerPackages) {
            if(clsPackageName.startsWith(baseControllerPackage)){
                //package name starts with {base-package}.controller
                pathPrefix = clsPackageName.substring(baseControllerPackage.length()).replace('.', '/');
                break;
            }
        }

        if(Strings.isEmpty(pathPrefix)) {
            String[] baseResourcePackages = resourcePackagePrefixes();
            for(String baseResourcePackage : baseResourcePackages) {
                if(clsPackageName.startsWith(baseResourcePackage)){
                    //package name starts with {base-package}.resource
                    pathPrefix = clsPackageName.substring(baseResourcePackage.length()).replace('.', '/');
                    break;
                }
            }
        }

		//get path from annotation
		Path a = cls.getAnnotation(Path.class);
		if(null != a){
            if(null != parent) {
                String[] parentPaths = getControllerPaths(parent);
                if(parentPaths.length == 1) {
                    String path = path(pathPrefix, a.value());
                    if(parentPaths[0].endsWith("/")){
                        return new String[]{parentPaths[0] + Paths.prefixWithoutSlash(path)};
                    }else{
                        return new String[]{parentPaths[0] + Paths.prefixWithSlash(path)};
                    }
                }else{
                    throw new IllegalStateException("Controller only supports one path value");
                }
            }
            return new String[]{Paths.prefixWithSlash(path(pathPrefix, a.value()))};
		}

		//get path conventional
		String controllerName = getControllerName(cls);
		
        String pathSuffix = isHome(controllerName) ? "" : controllerName.toLowerCase();

        if(null != parent) {
            String[] parentPaths = getControllerPaths(parent);
            if(parentPaths.length == 1) {
                pathPrefix = pathPrefix + Paths.prefixWithSlash(parentPaths[0]);
            }else{
                throw new IllegalStateException("Controller only supports one path value");
            }
        }

        if("/".equals(pathPrefix)) {
            pathPrefix = "";
        }

		if(Strings.isEmpty(pathPrefix)){
			return new String[]{pathSuffix};
		}else{
			return new String[]{pathPrefix + "/" + pathSuffix};
		}
    }

    protected String path(String pathPrefix, String path) {
        if(path.startsWith("/")) {
            return path;
        }else{
            return pathPrefix + "/" + path;
        }
    }

    protected Class<?> getParentControllerClass(Class<?> c) {
        Parent p = c.getAnnotation(Parent.class);
        if(null != p) {
            return p.value();
        }

        Class<?> enclosingClass = c.getEnclosingClass();
        if(null != enclosingClass && isControllerClass(enclosingClass)) {
            return enclosingClass;
        }

        return null;
    }

	public ActionMapping[] getActionMappings(ActionBuilder action) {
		List<ActionMapping> mappings = new ArrayList<>();
		
		Annotation[] annotations = action.getAnnotations();

		String defaultPath   = "";
		String defaultMethod = "*";
		for(Annotation a : annotations){
            HttpMethod hm = a.annotationType().getAnnotation(HttpMethod.class);

			if(null != hm){
				defaultMethod = Strings.firstNotEmpty(hm.value().toUpperCase(),
                                                        a.annotationType().getSimpleName().toUpperCase());

                Method value = Reflection.findMethod(a.annotationType(), "value");
                if(null != value){
                    defaultPath = (String)Reflection.invokeMethod(value, a);
                    if(!"".equals(defaultPath)) {
                        mappings.add(new ActionMapping(defaultPath,defaultMethod));
                    }
                }
				break;
			}
		}
		
		//Path annotation
		Path pa = Classes.getAnnotation(annotations, Path.class);
		if(null != pa){
			defaultPath = pa.value();
			mappings.add(new ActionMapping(defaultPath,defaultMethod));
		}
		
		if(Strings.isEmpty(defaultPath)){
			defaultPath = getActionPathByName(action.getName());
		}
		
		//RequestMappings annotation
		RequestMappings rms = Classes.getAnnotation(annotations, RequestMappings.class);
		if(null != rms){
			for(RequestMapping rm : rms.value()){
				mappings.add(createActionMapping(action, defaultMethod, defaultPath, rm));
			}
		}
		
		//RequestMapping annotation
		RequestMapping rm = Classes.getAnnotation(annotations, RequestMapping.class);
		if(null != rm){
			mappings.add(createActionMapping(action, defaultMethod, defaultPath, rm));
		}

        //Check is restful style.
        Object controller = action.getController();
        boolean restful = false;
        if(null != controller && Classes.isAnnotationPresent(controller.getClass(), Restful.class)) {
            restful = true;
        }

		//Default mappings
		if(mappings.isEmpty()){
            //index path ""
            if(isIndexAction(action)){
                mappings.add(new ActionMapping("", defaultMethod));
                mappings.add(new ActionMapping(config.getIndexActionName(), defaultMethod));
            } else if(restful) {
                //checks default-method
                if("*".equals(defaultMethod)) {
                    if(action.hasReturnType()) {
                        defaultMethod = HTTP.Method.GET.name();
                    }else{
                        defaultMethod = HTTP.Method.POST.name();
                    }
                }
                mappings.add(new ActionMapping("", defaultMethod));
            }else{
                //action name to path
                mappings.add(new ActionMapping(getActionPathByName(action.getName()), defaultMethod));
            }
		}
		
		return mappings.toArray(new ActionMapping[mappings.size()]);
	}
	
	protected ActionMapping createActionMapping(ActionBuilder action, String defaultMethod, String defaultPath, RequestMapping mapping) {
		String method = mapping.method().isNone() ? defaultMethod : mapping.method().name();
		String path   = Strings.firstNotEmpty(mapping.path(),defaultPath);
		String params = mapping.params();
		
		ActionMapping am = new ActionMapping(path, method);
		
		if(!Strings.isEmpty(params)){
			String[] keyValues = Strings.split(params,"&");
			for(String keyValue : keyValues){
				String[] keyValuePair = Strings.split(keyValue,"=");
				if(keyValuePair.length != 2){
					throw new AppConfigException("Invalid 'params' value '" + keyValue + "' in RequestMapping annotation on action '" + action + "'");
				}
				am.getParams().put(keyValuePair[0], keyValuePair[1]);
			}
		}
		
		return am;
	}

	@Override
    public String[] getDefaultViewNames(ActionBuilder action, String controllerPath, String actionPath, PathTemplate pathTemplate) {
        Annotation[] annotations = action.getAnnotations();
        DefaultView dv = Classes.getAnnotation(annotations, DefaultView.class);
        if(null != dv) {
            return new String[]{dv.value()};
        }

        Success success = Classes.getAnnotation(annotations, Success.class);
        if(null != success && !Strings.isEmpty(success.defaultView())) {
            return new String[]{success.defaultView()};
        }

		String fullActionPath  = pathTemplate.getTemplate();
		String defaultViewPath = null;
		if(fullActionPath.equals("/")){
			defaultViewPath = "/" + Strings.lowerUnderscore(action.getName());
		}else if(!pathTemplate.hasVariables()){
			defaultViewPath = pathTemplate.getTemplate();
		}
		
		String controllerActionPath = controllerPath + "/" + Strings.lowerUnderscore(action.getName());
		if(null == defaultViewPath){
			return new String[]{controllerActionPath};
		}else if(!Strings.equals(defaultViewPath, controllerActionPath)){
			return new String[]{defaultViewPath,controllerActionPath};
		}else{
			return new String[]{defaultViewPath};
		}
    }

    protected String[] controllerPackagePrefixes() {
        String[] a = new String[CONTROLLER_PACKAGES.length];
        for(int i=0;i<a.length;i++) {
            a[i] = app.config().getBasePackage() + "." + CONTROLLER_PACKAGES[i] + ".";
        }
        return a;
    }

    protected String[] resourcePackagePrefixes() {
        String[] a = new String[RESOURCE_PACKAGES.length];
        for(int i=0;i<a.length;i++) {
            a[i] = app.config().getBasePackage() + "." + RESOURCE_PACKAGES[i] + ".";
        }
        return a;
    }

    protected String getControllerPathByName(String controllerName) {
	    return nameToPath(controllerName);
    }

    protected String getActionPathByName(String actionName) {
	    return nameToPath(actionName);
    }

	protected String nameToPath(String name){
		//Removes the underscore like _name or name_ or _name_
		if(name.startsWith("_")){
			name = name.substring(1);
		}
		
		if(name.endsWith("_")){
			name = name.substring(0,name.length() - 1);
		}
		
		return Strings.lowerUnderscore(name);
	}
	
	protected boolean isHome(String controllerName){
		return Strings.equalsIgnoreCase(config.getHomeControllerName(),controllerName);
	}

}