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
package leap.webunit.client;

import leap.lang.Arrays2;
import leap.lang.Exceptions;
import leap.lang.exception.NestedIOException;
import leap.lang.http.MimeType;
import leap.lang.http.MimeTypes;
import leap.lang.io.IO;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.IOException;
import java.io.InputStream;

class ApacheTHttpResponse extends THttpResponseBase<ApacheTHttpClient> {
	
	private final HttpRequestBase httpRequest;
	private final HttpResponse    httpResponse;
	
	protected ApacheTHttpResponse(ApacheTHttpRequest request, HttpRequestBase httpRequest, HttpResponse httpResponse){
        super(request);
		this.httpRequest  = httpRequest;
		this.httpResponse = httpResponse;
	}

	@Override
    public Integer getStatus(){
		StatusLine statusLine = httpResponse.getStatusLine();
		return null == statusLine ? null : statusLine.getStatusCode();
	}
	
	@Override
    public MimeType getContentType(){
		if(null == contentType){
			HttpEntity entity = httpResponse.getEntity();
			Header     header = null == entity ? null : entity.getContentType();
			if(null != header){
				contentType = MimeTypes.parse(header.getValue());
			}
		}

		return contentType;
	}
	
	@Override
    public long getContentLength(){
		HttpEntity entity = httpResponse.getEntity();
		return null == entity ? -1L : entity.getContentLength();
	}
	
	@Override
    public String getHeader(String name){
		Header[] headers = httpResponse.getHeaders(name);
		return Arrays2.isEmpty(headers) ? null : headers[0].getValue();
	}
	
	@Override
    public leap.lang.http.Header[] getHeaders(String name){
		Header[] headers = httpResponse.getHeaders(name);
		
		if(null == headers || headers.length == 0) {
			return EMPTY_HEADER_ARRAY;
		}
		
		leap.lang.http.Header[] array = new leap.lang.http.Header[headers.length];
		for(int i=0;i<array.length;i++) {
			Header header = headers[i];
			
			array[i] =  new leap.lang.http.Header(header.getName(), header.getValue());
		}
		
		return array;
	}
	
	@Override
    public InputStream getInputStream() throws NestedIOException {
		try {
	        HttpEntity entity = httpResponse.getEntity();
	        return null == entity ? null : entity.getContent();
        } catch (IOException e) {
        	throw Exceptions.wrap("Error reading response body", e);
        }
	}
	
}
