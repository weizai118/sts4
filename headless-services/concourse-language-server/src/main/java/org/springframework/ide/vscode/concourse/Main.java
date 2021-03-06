/*******************************************************************************
 * Copyright (c) 2016-2017 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/

package org.springframework.ide.vscode.concourse;

import java.io.IOException;

import org.springframework.ide.vscode.commons.languageserver.LaunguageServerApp;
import org.springframework.ide.vscode.commons.util.LogRedirect;
import org.springframework.ide.vscode.commons.yaml.completion.YamlCompletionEngineOptions;
import org.springframework.ide.vscode.concourse.github.DefaultGithubInfoProvider;
import org.springframework.ide.vscode.concourse.github.GithubInfoProvider;

import static org.springframework.ide.vscode.commons.languageserver.LaunguageServerApp.STANDALONE_STARTUP;

public class Main {
	private static final YamlCompletionEngineOptions OPTIONS = YamlCompletionEngineOptions.DEFAULT;

	public static void main(String[] args) throws IOException, InterruptedException {
		String serverName = "concourse-language-server";
		if (!Boolean.getBoolean(STANDALONE_STARTUP)) {
			LogRedirect.redirectToFile(serverName);
		}
		LaunguageServerApp.start(serverName, () -> new ConcourseLanguageServer(OPTIONS, new DefaultGithubInfoProvider()));
	}
}
