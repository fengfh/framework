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
package leap.htpl.web;

import leap.core.BeanFactory;
import leap.core.annotation.Inject;
import leap.htpl.HtplEngine;
import leap.htpl.HtplTemplate;
import leap.htpl.HtplTemplateLazyCreator;
import leap.htpl.HtplTemplateResolver;
import leap.lang.Strings;
import leap.lang.http.ContentTypes;
import leap.lang.http.MimeTypes;
import leap.lang.resource.Resource;
import leap.web.App;
import leap.web.view.AbstractServletResourceViewResolver;
import leap.web.view.View;

import javax.servlet.ServletContext;
import java.util.Locale;

public class WebHtplViewResolver extends AbstractServletResourceViewResolver implements HtplTemplateResolver {

	@Inject
	protected HtplEngine engine;

	@Inject
	protected ServletContext servletContext;
	
	@Inject
	protected App app;

	protected String contentType;
	
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

    @Override
    protected View loadView(String prefix, String suffix, String viewName, Locale locale, String resourcePath, Resource resource) {
		boolean isDebug = app.config().isDebug();
		boolean isLazy  = app.config().isLazyTemplate();

		WebHtplResource res = new WebHtplResource(servletContext, prefix, suffix, resource, locale);

		WebHtplView view;
		if(isDebug && isLazy){
            HtplTemplateLazyCreator template = new HtplTemplateLazyCreator(engine, viewName, res);
			view = new WebHtplView(app, viewName, template);
		}else{
			HtplTemplate template = engine.createTemplate(res, viewName);
			view = new WebHtplView(app, viewName, template);
		}

		if(Strings.isEmpty(view.getContentType())) {
			view.setContentType(contentType);	
		}
		
		if(Strings.isEmpty(view.getCharacterEncoding())) {
			view.setCharacterEncoding(app.getDefaultCharset().name());	
		}
		
		return view;
    }

    /*
	protected ServletResource resolveJspResource(String prefix, String viewPath, Locale locale) {
		String jspPath = null;
		
		int lastSlashIndex = viewPath.lastIndexOf('/');
		if(lastSlashIndex >= 0) {
			jspPath = viewPath.substring(0,lastSlashIndex) + "/~" + viewPath.substring(lastSlashIndex + 1);
		}else{
			jspPath = "~" + viewPath;
		}
		
		ServletResource resource = getLocaleResource(prefix,".jsp", jspPath, locale);
		if(null != resource && resource.exists()) {
			return resource;
		}
		return null;
	}
	*/

	@Override
    public void postCreate(BeanFactory beanFactory) throws Throwable {
		super.postCreate(beanFactory);
		
		if(Strings.isEmpty(contentType)){
			contentType = ContentTypes.create(MimeTypes.TEXT_HTML, app.getDefaultCharset().name());
		}
    }

	@Override
    public HtplTemplate resolveTemplate(String templateName, Locale locale) {
		Resource resource = getLocaleResource(prefix, suffix, templateName, locale);
		
		if(null != resource && resource.exists()){
			return engine.createTemplate(new WebHtplResource(servletContext, prefix,suffix,resource, locale), templateName);
		}
		
	    return null;
    }
}