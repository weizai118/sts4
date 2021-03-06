/*******************************************************************************
 * Copyright (c) 2018 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.commons.java;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ide.vscode.commons.languageserver.jdt.ls.Classpath;
import org.springframework.ide.vscode.commons.languageserver.jdt.ls.Classpath.CPE;

import com.google.common.collect.ImmutableList;

public class IClasspathUtil {

	private static final Logger log = LoggerFactory.getLogger(IClasspath.class);

	public static CPE findEntryForBinaryRoot(IClasspath cp, File binaryClasspathtRoot) {
		try {
			for (CPE cpe : cp.getClasspathEntries()) {
				if (correspondsToBinaryLocation(cpe, binaryClasspathtRoot)) {
					return cpe;
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}

	public static List<File> getAllBinaryRoots(IClasspath cp) {
		return getBinaryRoots(cp, null);
	}

	public static List<File> getBinaryRoots(IClasspath cp, Predicate<CPE> filter) {
		ImmutableList.Builder<File> roots = ImmutableList.builder();
		try {
			for (CPE cpe : cp.getClasspathEntries()) {
				if (filter == null || filter.test(cpe)) {
					File loc = binaryLocation(cpe);
					if (loc!=null) {
						roots.add(loc);
					}
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return roots.build();
	}

	private static boolean correspondsToBinaryLocation(CPE cpe, File classpathEntryFile) {
		classpathEntryFile = canonicalFile(classpathEntryFile);
		File canonicalFile = binaryLocation(cpe);
		return Objects.equals(canonicalFile, classpathEntryFile);
	}

	private static File binaryLocation(CPE cpe) {
		switch (cpe.getKind()) {
		case Classpath.ENTRY_KIND_BINARY:
			return canonicalFile(cpe.getPath());
		case Classpath.ENTRY_KIND_SOURCE:
			return canonicalFile(cpe.getOutputFolder());
		default:
			throw new IllegalStateException("Missing switch case?");
		}
	}

	private static File canonicalFile(String _f) {
		if (_f!=null) {
			File f = new File(_f);
			return canonicalFile(f);
		}
		return null;
	}

	private static File canonicalFile(File f) {
		try {
			return f.getCanonicalFile();
		} catch (IOException e) {
			return f.getAbsoluteFile();
		}
	}

	public static Stream<File> getSourceFolders(IClasspath classpath) {
		try {
			if (classpath != null) {
				return classpath.getClasspathEntries().stream().filter(Classpath::isSource)
						.map(cpe -> new File(cpe.getPath()));
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return Stream.empty();
	}

	public static Stream<File> getOutputFolders(IClasspath classpath) {
		try {
			return classpath.getClasspathEntries().stream()
					.filter(Classpath::isSource)
					.map(cpe -> new File(cpe.getOutputFolder()));
		} catch (Exception e) {
			log.error("", e);
		}
		return Stream.empty();
	}
}
