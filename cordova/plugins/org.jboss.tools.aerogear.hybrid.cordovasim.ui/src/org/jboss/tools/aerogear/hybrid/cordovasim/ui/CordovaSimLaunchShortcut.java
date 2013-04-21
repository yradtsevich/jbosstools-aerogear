/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *       Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.aerogear.hybrid.cordovasim.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ide.ResourceUtil;
import org.jboss.tools.vpe.cordovasim.eclipse.launch.CordovaSimLaunchConstants;

public class CordovaSimLaunchShortcut implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof StructuredSelection) {
			StructuredSelection structuredSelection = (StructuredSelection) selection;
			Object firstElement = structuredSelection.getFirstElement();
			IResource resource = ResourceUtil.getResource(firstElement);
			if (resource != null) {
				IProject project = resource.getProject();
				launch(project, mode);
			}
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		IEditorInput input = editor.getEditorInput();
		IFile file = ResourceUtil.getFile(input);
		if (file != null) {
			IProject project = file.getProject();
			launch(project, mode);
		}
	}
	
	private void launch(IProject project, String mode) {
		//TODO: draft implementation. fill launch parameters with non-hard-coded values, etc.
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType launchConfigurationType 
				= launchManager.getLaunchConfigurationType(CordovaSimLaunchConstants.LAUNCH_CONFIGURATION_ID);
		ILaunchConfigurationWorkingCopy launchConfiguration;
		try {
			launchConfiguration = launchConfigurationType.newInstance(
					null, launchManager.generateLaunchConfigurationName(project.getName()));
			launchConfiguration.setAttribute(CordovaSimLaunchConstants.ROOT_FOLDER,
					project.getFolder("www").getFullPath().toString());
			launchConfiguration.setAttribute(CordovaSimLaunchConstants.START_PAGE, "index.html");
			DebugUITools.launch(launchConfiguration, mode);
		} catch (CoreException e) {
			Plugin.logError(e.getMessage(), e);
		}
	}
}
