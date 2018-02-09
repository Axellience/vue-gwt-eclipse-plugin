package com.axellience.vuegwt.eclipse.templatewatcher;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class RefreshVueTemplateJob extends WorkspaceJob {
	private final Set<IFile> templatesToRefresh;

	public RefreshVueTemplateJob(Set<IFile> templatesToRefresh) {
		super("Vue GWT Refresh");
		this.templatesToRefresh = new HashSet<>(templatesToRefresh);
	}

	@Override
	public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
		this.templatesToRefresh.stream().forEach(this::touchFile);
		return Status.OK_STATUS;
	}
	
	public void touchFile(IFile javaFile) {
		new File(javaFile.getFullPath().toString()).setLastModified(System.currentTimeMillis());
		try {
			javaFile.touch(null);
		} catch (CoreException e) {
		}
	}
}
