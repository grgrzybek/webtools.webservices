/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.command.internal.env.ui.widgets;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


/**
 * This Command can be used to get the current page.
 * Use of this class is not recommended.
 *
 */
public class CurrentPageCommand extends AbstractDataModelOperation
{
  private WizardPageManager pageManager_;
  
  public CurrentPageCommand( WizardPageManager pageManager )
  {
    pageManager_ = pageManager;
  }
  
  public IWizardPage getCurrentPage()
  {
    return pageManager_.getCurrentPage();
  }

  public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
  {
    return Status.OK_STATUS;
  }
}
