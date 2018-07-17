/*******************************************************************************
 * Copyright (c) 2018 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.kubernetes;

import io.fabric8.kubernetes.api.model.ServicePort;

public class DeploymentDefinition {

	private String serviceAnnotations;
	private boolean isHostNetwork;
	private Integer containerPort;
	private String[] envVars;
	private DockerImage image;
	private String podAnnotations;
	private ServicePort servicePort;
	private boolean nodePort;
	
	// Default 1 replica
	private int replicaCount = 1;
	private String imagePullPolicy;
	private String appName;
	private String cpu;
	private String memory;
	private boolean hostNetwork = false;
	
	public DeploymentDefinition() {
		this.envVars = new String[0];
	}

	public void setServiceAnnotations(String serviceAnnotations) {
		this.serviceAnnotations = serviceAnnotations;
	}

	public void setContainerPort(Integer containerPort) {
		this.containerPort = containerPort;
	}

	public void setEnvVars(String[] envVars) {
		this.envVars = envVars;
	}

	public void setDockerImage(DockerImage image) {
		this.image = image;
	}

	public void setPodAnnotations(String podAnnotations) {
		this.podAnnotations = podAnnotations;
	}

	public void setServicePort(ServicePort servicePort) {
		this.servicePort = servicePort;
	}

	public void setNodePort(boolean nodePort) {
		this.nodePort = nodePort;
	}

	public void setReplicaCount(int replicaCount) {
		this.replicaCount = replicaCount;
	}

	public void setImagePullPolicy(String imagePullPolicy) {
		this.imagePullPolicy = imagePullPolicy;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public void setHostNetwork(boolean hostNetwork) {
		this.hostNetwork = hostNetwork;
	}

	public DockerImage getDockerImage() {
		return image;
	}

	public String[] getEnvironmentVariables() {
		return envVars;
	}

	public Integer getContainerPort() {
		return containerPort;
	}

	public boolean isHostNetwork() {
		return isHostNetwork;
	}

	public String getServiceAnnotations() {
		return serviceAnnotations;
	}

	public String getPodAnnotations() {
		return podAnnotations;
	}

	public ServicePort getServicePort() {
		return servicePort;
	}

	public boolean createNodePort() {
		return nodePort;
	}

	public int getReplicaCount() {
		return replicaCount;
	}

	public String getImagePullPolicy() {
		return imagePullPolicy;
	}

	public String getAppName() {
		return appName;
	}

	public String getCpu() {
		return cpu;
	}

	public String getMemory() {
		return memory;
	}

	public boolean getHostNetwork() {
		return hostNetwork;
	}

}
