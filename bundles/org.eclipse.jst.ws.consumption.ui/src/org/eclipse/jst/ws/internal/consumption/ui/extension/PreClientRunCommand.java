/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.extension;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.StartServerCommand;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceClient;

public class PreClientRunCommand extends EnvironmentalOperation 
{
  private IWebServiceClient webServiceClient_;

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    Environment environment = getEnvironment();
    
    System.out.println("In Pre client run command.");
    StartServerCommand command = new StartServerCommand();
    command.setServerInstanceId(webServiceClient_.getWebServiceClientInfo().getServerInstanceId());
    command.setEnvironment( environment );
    IStatus status = command.execute( null, null );
    if (status.getSeverity()==Status.ERROR)
    {
      environment.getStatusHandler().reportError( EnvironmentUtils.convertIStatusToStatus(status));       
    }     
    return status;    
  }

  public void setWebService( IWebServiceClient webServiceClient )
  {
    webServiceClient_ = webServiceClient;  
  }
}
