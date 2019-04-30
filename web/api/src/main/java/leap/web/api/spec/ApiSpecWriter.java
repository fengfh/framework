/*
 * Copyright 2015 the original author or authors.
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
package leap.web.api.spec;

import java.io.IOException;

import leap.web.api.meta.ApiMetadata;

public interface ApiSpecWriter {
	
	/**
	 * Returns the content type of output spec.
	 */
	String getContentType();

    /**
     * Writes the given api metadata to spec's format.
     */
    default void write(ApiMetadata m, Appendable out) throws IOException {
        write(ApiSpecContext.EMPTY, m, out);
    }
	
	/**
	 * Writes the given api metadata to spec's format.
	 */
	void write(ApiSpecContext context, ApiMetadata m, Appendable out) throws IOException;

    /**
     * Writes the given api models to spec's format.
     */
    default void writeModels(ApiMetadata m, Appendable out) throws IOException {
        writeModels(ApiSpecContext.EMPTY, m, out);
    }

    /**
     * Writes the given api models to spec's format.
     */
    void writeModels(ApiSpecContext context, ApiMetadata m, Appendable out) throws IOException;
}