package org.codehaus.mojo.webstart.generator;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.project.MavenProject;
import org.apache.velocity.VelocityContext;
import org.codehaus.mojo.webstart.JarResource;
import org.codehaus.mojo.webstart.JnlpResource;
import org.codehaus.mojo.webstart.NativeDependency;
import org.codehaus.mojo.webstart.NativeResource;
import org.codehaus.plexus.util.StringUtils;

/**
 * Generates a JNLP deployment descriptor.
 * 
 * @author ngc
 * @author <a href="jerome@coffeebreaks.org">Jerome Lacoste</a>
 * @author Kevin Stembridge
 */
public class JarResourcesGenerator extends AbstractGenerator

{

	private final Collection<JarResource> jarResources;

	private String libPath;

	private final Collection<NativeDependency> nativeDependencies;

	/**
	 * Creates a new {@code JarResources}.
	 * 
     * @param mavenProject       The Maven project that this generator is being run within.
     * @param resourceLoaderPath used to find the template in conjunction to inputFileTemplatePath
	 * @param outputFile
     * @param templateFile       relative to resourceLoaderPath
     * @param jarResources       The collection of JarResources that will be output in the JNLP file.
     * @param mainClass          The fully qualified name of the application's main class.
     * @param libPath            The path where the libraries are placed within the jnlp structure
	 */
	public JarResourcesGenerator(MavenProject mavenProject,
			File resourceLoaderPath, String defaultTemplateResourceName,
			File outputFile, String templateFile, Collection<JarResource> jarResources,
			Collection<NativeDependency> nativeDependencies, String mainClass,
			String webstartJarURL, String libPath, String encoding) {
		super(mavenProject, resourceLoaderPath, defaultTemplateResourceName,
				outputFile, templateFile, mainClass, webstartJarURL, encoding);
		this.jarResources = jarResources;
		this.libPath = libPath;
		this.nativeDependencies = nativeDependencies;
	}

	/**
	 * {@inheritDoc}
	 */
	protected String getDependenciesText() {

		String jarResourcesText = "";

        if ( this.jarResources.size() != 0 )
        {
			final int multiplier = 100;
            StringBuffer buffer = new StringBuffer( multiplier * this.jarResources.size() );
            buffer.append( "\n" );

            for ( Iterator itr = this.jarResources.iterator(); itr.hasNext(); )
            {
				JarResource jarResource = (JarResource) itr.next();

				if (!jarResource.isIncludeInJnlp()
						|| isNativeDependency(jarResource)) {
					continue;
				}

				addJnlpResource(jarResource, buffer);
				if (itr.hasNext()) {
					buffer.append("\n");
				}
			}
			jarResourcesText = buffer.toString();
		}
		return jarResourcesText;
	}

	private void addJnlpResource(JnlpResource jarResource, StringBuffer buffer) {
		if (jarResource instanceof NativeResource) {
			buffer.append("<")
					.append(((NativeResource) jarResource).getResourceType())
					.append(" ");
		} else {
			buffer.append("<jar ");
		}
		buffer.append("href=\"");
		if (StringUtils.isNotEmpty(libPath)) {
			buffer.append(libPath);
			buffer.append('/');
		}
		buffer.append(jarResource.getHrefValue());
		buffer.append("\"");

		if (jarResource.isOutputVersion()) {
			buffer.append(" version=\"").append(jarResource.getVersion())
					.append("\"");
		}

		if (jarResource instanceof JarResource
				&& ((JarResource) jarResource).getMainClass() != null) {
			buffer.append(" main=\"true\"");
		}
		buffer.append("/>");

	}

	protected String getNativeDependenciesText() {
		if (nativeDependencies == null || nativeDependencies.isEmpty()) {
			return "";
		}
		final int multiplier = 100;
		StringBuffer buffer = new StringBuffer(multiplier
				* this.jarResources.size());
		buffer.append("\n");
		for (NativeDependency nativeDependency : nativeDependencies) {
			List<NativeResource> nativeResources = nativeDependency.getNativeResources();
			String[] architectureList = org.apache.commons.lang.StringUtils
					.split(nativeDependency.getArch(), ';');
			for (int i = 0; i < architectureList.length; i++) {
				buffer.append("<resources os=\"")
						.append(nativeDependency.getOs()).append("\" arch=\"")
						.append(architectureList[i]).append("\">");
				buffer.append("\n");
                
				for (NativeResource nativeResource : nativeResources) {
					addJnlpResource(nativeResource, buffer);
				}
                
				buffer.append("\n");
				buffer.append("</resources>").append("\n\n");
			}
		}
		return buffer.toString();
	}

	private boolean isNativeDependency(JarResource jarResource) {
		if (nativeDependencies == null || nativeDependencies.isEmpty()) {
			return false;
		}
		for (Iterator itr = nativeDependencies.iterator(); itr.hasNext();) {
			NativeDependency nativeDependency = (NativeDependency) itr.next();
			List nativeResources = nativeDependency.getNativeResources();
			if (nativeResources != null
					&& nativeResources.contains(jarResource)) {
				return true;
			}
		}
		return false;
	}

	protected VelocityContext createAndPopulateContext() {
		VelocityContext velocityContext = super.createAndPopulateContext();
		velocityContext.put("nativeDependencies", getNativeDependenciesText());
		return velocityContext;
	}

}
