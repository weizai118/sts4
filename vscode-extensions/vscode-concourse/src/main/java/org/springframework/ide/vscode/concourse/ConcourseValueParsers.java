/*******************************************************************************
 * Copyright (c) 2016 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.concourse;

import java.util.function.Function;

import org.springframework.ide.vscode.commons.util.RegexpParser;
import org.springframework.ide.vscode.commons.util.ValueParser;
import org.springframework.ide.vscode.commons.util.text.IDocument;
import org.springframework.ide.vscode.commons.yaml.schema.SchemaContextAware;

import com.google.common.collect.Multiset;

/**
 * Methods and constants to create/get parsers for some atomic types
 * used in manifest yml schema.
 *
 * @author Kris De Volder
 */
public class ConcourseValueParsers {

	public static final SchemaContextAware<ValueParser> resourceNameDef(ConcourseModel models) {
		return acceptOnlyUniqueNames(models::getResourceNames, "resource name");
	}

	public static final SchemaContextAware<ValueParser> jobNameDef(ConcourseModel models) {
		return acceptOnlyUniqueNames(models::getJobNames, "job name");
	}

	public static SchemaContextAware<ValueParser> acceptOnlyUniqueNames(
			Function<IDocument, Multiset<String>> getDefinedNameCounts,
			String typeName
	) {
		return (dc) -> {
			Multiset<String> resourceNames = getDefinedNameCounts.apply(dc.getDocument());
			return (String input) -> {
				if (resourceNames.count(input)<=1) {
					//okay
					return resourceNames;
				}
				throw new IllegalArgumentException("Duplicate "+typeName+" '"+input+"'");
			};
		};
	};

	public static ValueParser DURATION = new RegexpParser(
			"^(([0-9]+(.[0-9]+)?)(ns|us|µs|ms|s|h|m))+$",
			"Duration",
			" A duration string is a sequence of decimal numbers, each with "
					+ "optional fraction and a unit suffix, such as '300ms', '1.5h' or"
					+ " '2h45m'. Valid time units are 'ns', 'us' (or 'µs'), 'ms', 's', "
			+ "'m', 'h'."
	);




}