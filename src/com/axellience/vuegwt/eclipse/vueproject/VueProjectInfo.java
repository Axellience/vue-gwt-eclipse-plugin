package com.axellience.vuegwt.eclipse.vueproject;

import static com.axellience.vuegwt.eclipse.Startup.PLUGIN_ID;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

public class VueProjectInfo {
	private final ILog LOGGER = Platform.getLog(Platform.getBundle(PLUGIN_ID));
	
	private final IJavaProject javaProject;

	VueProjectInfo(IJavaProject javaProject) {
		this.javaProject = javaProject;
	}

	public Optional<IPath> getResourceSourcePath(IResource resource) {
		return getSourcePaths()
				.stream()
				.filter(sourcePath -> sourcePath.isPrefixOf(resource.getFullPath()))
				.findFirst();
	}

	public List<IPath> getSourcePaths() {
		try {
			return Stream
					.of(javaProject.getResolvedClasspath(true))
					.filter(entry -> entry.getContentKind() == IPackageFragmentRoot.K_SOURCE)
					.map(IClasspathEntry::getPath)
					.collect(Collectors.toList());
		} catch (JavaModelException e) {
			LOGGER.log(new Status(IStatus.WARNING, PLUGIN_ID, "Error while finding Project source paths for project: " + javaProject, e));
		}

		return Collections.emptyList();
	}
}