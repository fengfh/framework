/*
 * Copyright 2014 the original author or authors.
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
package app.controllers;

import leap.lang.logging.Log;
import leap.lang.logging.LogFactory;
import leap.web.Request;
import leap.web.security.annotation.AllowAnonymous;

public class HomeController {
	
	private static final Log log = LogFactory.get(HomeController.class);
	
	public void index() {
		
	}
	
	public String anonymous() {
		return "Hello";
	}

	@AllowAnonymous
	public String anonymous1() {
		return "Hello1";
	}
	
	public void login(){
		log.info("Goto login view");
	}
}