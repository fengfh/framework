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
package leap.htpl;

import leap.core.el.ExpressionLanguage;
import leap.lang.expression.Expression;
import leap.lang.expression.ExpressionException;

/**
 * The expression manager of htpl template engine.
 */
public interface HtplExpressionManager {

    /**
     * The interface for handling expression parseing.
     */
	interface ParseHandler {
		void textParsed(String text);
		void exprParsed(Expression expr);
	}

    /**
     * Returns the default expression language.
     */
	ExpressionLanguage getExpressionLanguage();

	Expression parseExpression(HtplEngine engine,String expression);

	Expression tryParseCompositeExpression(HtplEngine engine, String text);

	Expression parseCompositeExpression(HtplEngine engine,String text);
	
	void parseCompositeExpression(HtplEngine engine, String text, ParseHandler callback);
	
	Expression tryParseAttributeExpression(HtplEngine engine,String value);
	
	Expression parseAttributeExpression(HtplEngine engine,String value);
	
	void parseAttributeExpression(HtplEngine engine, String value, ParseHandler handler);
}