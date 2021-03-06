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
package leap.htpl.processor.core;

import leap.htpl.HtplDocument;
import leap.htpl.HtplEngine;
import leap.htpl.ast.Attr;
import leap.htpl.ast.Element;
import leap.htpl.ast.Node;
import leap.htpl.ast.SetVariables;
import leap.htpl.parser.HtplParserBase;
import leap.htpl.processor.AbstractNamedAttrProcessor;
import leap.lang.Strings;

/**
 * Set variable
 */
public class SetVariablesAttrProcessor extends AbstractNamedAttrProcessor {
	
	public static final String ATTR_NAME = "set";

	public SetVariablesAttrProcessor() {
		super(ATTR_NAME);
    }

	@Override
	public Node processStartElement(HtplEngine engine, HtplDocument doc, Element e, Attr attr) {
		//set="name=expr "
		e.removeAttribute(attr);
		
		String content = attr.getString();
		if(!Strings.isEmpty(content)) {
			return new SetVariables(HtplParserBase.parseNamedExpressions(engine, content, ':'), e);
		}else{
			return e;
		}
	}

}