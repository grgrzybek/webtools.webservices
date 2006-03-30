/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.edit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.common.core.search.pattern.QualifiedName;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.common.ui.internal.search.dialogs.IComponentList;
import org.eclipse.wst.common.ui.internal.search.dialogs.IComponentSearchListProvider;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.internal.impl.ImportImpl;

public abstract class WSDLBaseSearchListProvider implements IComponentSearchListProvider {
	protected void createWSDLComponentObjects(IComponentList list, List inputComponents, QualifiedName metaName) {
		Iterator it = inputComponents.iterator();
		while (it.hasNext()) {
			WSDLElement wsdlElement = (WSDLElement) it.next();
			String name = wsdlElement.getElement().getAttribute("name");
			String qualifier = wsdlElement.getEnclosingDefinition().getTargetNamespace();
			
			ComponentSpecification componentSpec = new ComponentSpecification();
			componentSpec.setMetaName(metaName);
			componentSpec.setName(name);
			componentSpec.setQualifier(qualifier);
			
			String location = wsdlElement.getEnclosingDefinition().getLocation();
			String platformResource = "platform:/resource";
			if (location != null && location.startsWith(platformResource)) {
				Path path = new Path(location.substring(platformResource.length()));
				IFile result = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				if (result != null) {
					componentSpec.setFile(result);
				}
			}
			
			list.add(componentSpec);
		}
	}
	
	protected List getWSDLFileImports(List wsdlImports) {
		List list = new ArrayList();
		Iterator it = wsdlImports.iterator();
		while (it.hasNext()) {
			ImportImpl importItem = (ImportImpl) it.next();
			importItem.importDefinitionOrSchema();          // Load if necessary
			if (importItem.getESchema() == null) {
				list.add(importItem);
			}
		}
		
		return list;
	}
}
