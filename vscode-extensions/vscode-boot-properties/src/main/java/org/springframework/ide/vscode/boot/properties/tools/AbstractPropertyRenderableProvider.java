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
package org.springframework.ide.vscode.boot.properties.tools;

import static org.springframework.ide.vscode.commons.util.Renderables.*;

import java.util.Collection;

import org.springframework.ide.vscode.commons.java.IJavaProject;
import org.springframework.ide.vscode.commons.util.Renderable;
import org.springframework.ide.vscode.commons.util.StringUtil;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public abstract class AbstractPropertyRenderableProvider {
		
	/**
	 * Fake host name that is used in 'action link' urls so that we can
	 * recognize them as such.
	 */
	private static final String ACTION_HOST = "action";
	
	public Renderable getRenderable() {
		Builder<Renderable> renderableBuilder = ImmutableList.builder();

		renderId(renderableBuilder);
		
		String type = getType();
		if (type==null) {
			type = Object.class.getName();
		}
		actionLink(renderableBuilder, type);
		
		String deflt = formatDefaultValue(getDefaultValue());
		if (deflt!=null) {
			defaultValueRenderable(renderableBuilder, deflt);
		}
		
		if (isDeprecated()) {
			depreactionRenderable(renderableBuilder);
		}
		
		Renderable description = getDescription();
		if (description!=null) {
			descriptionRenderable(renderableBuilder, description);
		}
		
		return concat(renderableBuilder.build());
	}
	
	final protected void renderId(Builder<Renderable> renderableBuilder) {
		if (isDeprecated()) {
			renderableBuilder.add(strikeThrough(text(getId())));
			String replacement = getDeprecationReplacement();
			if (StringUtil.hasText(replacement)) {
				renderableBuilder.add(text(" -> " + replacement));
			}
		} else {
			renderableBuilder.add(bold(text(getId())));
		}
	}
	
	protected abstract Object getDefaultValue();
	protected abstract IJavaProject getJavaProject();
	protected abstract Renderable getDescription();
	protected abstract String getType();
	protected abstract String getDeprecationReason();
	protected abstract String getId();
	protected abstract String getDeprecationReplacement();
	protected abstract boolean isDeprecated();

	public static String formatDefaultValue(Object defaultValue) {
		if (defaultValue!=null) {
			if (defaultValue instanceof String) {
				return (String) defaultValue;
			} else if (defaultValue instanceof Number) {
				return ((Number)defaultValue).toString();
			} else if (defaultValue instanceof Boolean) {
				return Boolean.toString((Boolean) defaultValue);
			} else if (defaultValue instanceof Object[]) {
				return StringUtil.arrayToCommaDelimitedString((Object[]) defaultValue);
			} else if (defaultValue instanceof Collection<?>) {
				return StringUtil.collectionToCommaDelimitedString((Collection<?>) defaultValue);
			} else {
				//no idea what it is but try 'toString' and hope for the best
				return defaultValue.toString();
			}
		}
		return null;
	}

	/**
	 * Creates an 'action' link and adds it to the html buffer. When the user clicks the given
	 * link then the provided runnable is to be executed.
	 */
	public void actionLink(Builder<Renderable> renderableBuilder, String displayString) {
		String url = "http://"+ACTION_HOST+"/action-id";
		renderableBuilder.add(lineBreak());
		renderableBuilder.add(link(displayString, url));
	}
	
	private void defaultValueRenderable(Builder<Renderable> renderableBuilder, String defaultValue) {
		renderableBuilder.add(lineBreak());
		renderableBuilder.add(lineBreak());
		renderableBuilder.add(text("Default: "));
		renderableBuilder.add(italic(text(defaultValue)));
	}
	
	private void depreactionRenderable(Builder<Renderable> renderableBuilder) {
		renderableBuilder.add(lineBreak());
		renderableBuilder.add(lineBreak());
		String reason = getDeprecationReason();
		if (StringUtil.hasText(reason)) {
			renderableBuilder.add(bold(text("Deprecated: ")));
			renderableBuilder.add(text(reason));
		} else {
			renderableBuilder.add(bold(text("Deprecated!")));
		}		
	}
	
	private void descriptionRenderable(Builder<Renderable> renderableBuilder, Renderable description) {
		renderableBuilder.add(lineBreak());
		renderableBuilder.add(paragraph(description));
	}

}