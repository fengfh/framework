/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package leap.web.api.meta;

import java.util.*;

import leap.lang.Args;
import leap.lang.Arrays2;
import leap.lang.Builders;
import leap.lang.Collections2;
import leap.lang.Strings;
import leap.lang.exception.ObjectExistsException;
import leap.web.api.meta.model.*;

/**
 * The builder of {@link ApiMetadata}
 */
public class ApiMetadataBuilder extends MApiNamedWithDescBuilder<ApiMetadata> {
	
    protected String            basePath;
    protected String            description;
    protected String            termsOfService;
    protected MApiConcatBuilder concat;
    protected String            version;
    protected String            host;
    protected Set<String>                   protocols    = new LinkedHashSet<>();
    protected Set<String>                   consumes     = new LinkedHashSet<>();
    protected Set<String>                   produces     = new LinkedHashSet<>();
    protected Map<String, MApiPathBuilder>  paths        = new LinkedHashMap<>();
    protected Map<String, MApiModelBuilder> models       = new LinkedHashMap<>();
    protected List<MApiSecurityDef>         securityDefs = new ArrayList<>();
	
	public ApiMetadataBuilder() {
        super();
    }

	public ApiMetadataBuilder(String basePath) {
		this.basePath = basePath;
	}
	
	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getTermsOfService() {
		return termsOfService;
	}

	public void setTermsOfService(String termsOfService) {
		this.termsOfService = termsOfService;
	}

	public MApiConcatBuilder getConcat() {
		return concat;
	}

	public void setConcat(MApiConcatBuilder concat) {
		this.concat = concat;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Set<String> getProtocols() {
		return protocols;
	}
	
	public void addProtocol(String p) {
		protocols.add(p);
	}
	
	public void addProtocols(String... ps) {
		Collections2.addAll(protocols, ps);
	}

	public Set<String> getConsumes() {
		return consumes;
	}
	
	public void addConsume(String mimeType) {
		consumes.add(mimeType);
	}
	
	public void addConsumes(String... cs) {
		Collections2.addAll(consumes, cs);
	}

	public Set<String> getProduces() {
		return produces;
	}
	
	public void addProduce(String mimeType) {
		produces.add(mimeType);
	}
	
	public void addProduces(String... ps) {
		Collections2.addAll(produces, ps);
	}

	public void addPath(MApiPathBuilder path) throws ObjectExistsException {
		Args.notNull(path, "api path");
		Args.notEmpty(path.getPathTemplate(), "path template");
		
		if(paths.containsKey(path.getPathTemplate().getTemplate())) {
			throw new ObjectExistsException("The path template '" + path.getPathTemplate().getTemplate() + "' aleady exists");
		}
		
		String fullPath = path.getPathTemplate().getTemplate();
		String relativePath = Strings.removeStart(fullPath, basePath);
		
		paths.put(relativePath, path);
	}
	
	public Map<String, MApiPathBuilder> getPaths() {
		return paths;
	}
	
	public MApiPathBuilder getPath(String pathTemplate) {
		Args.notEmpty(pathTemplate,"path template");
        String relativePath = Strings.removeStart(pathTemplate, basePath);
		return paths.get(relativePath);
	}

	public Map<String, MApiModelBuilder> getModels() {
		return models;
	}
	
	public void addModel(MApiModelBuilder model) throws ObjectExistsException {
		Args.notNull(model, "model");
		
		if(models.containsKey(model.getName())) {
			throw new ObjectExistsException("The model '" + model.getName() + "' aleady exists");
		}
		
		models.put(model.getName(), model);
	}

	public boolean containsModel(String name) {
		return models.containsKey(name);
	}
	
	public List<MApiSecurityDef> getSecurityDefs() {
        return securityDefs;
    }
	
	public void addSecurityDef(MApiSecurityDef def) {
	    securityDefs.add(def);
	}

    @Override
    public ApiMetadata build() {
        return new ApiMetadata(name, title, summary, description,
        					termsOfService, null == concat ? null : concat.build(), 
        					version, host, basePath,
        					protocols.toArray(Arrays2.EMPTY_STRING_ARRAY), 
        					consumes.toArray(Arrays2.EMPTY_STRING_ARRAY), 
        					produces.toArray(Arrays2.EMPTY_STRING_ARRAY), 
        					Builders.buildMap(paths),
        					Builders.buildMap(models),
        					securityDefs.toArray(new MApiSecurityDef[]{}),
        					attrs);
    }
	
}