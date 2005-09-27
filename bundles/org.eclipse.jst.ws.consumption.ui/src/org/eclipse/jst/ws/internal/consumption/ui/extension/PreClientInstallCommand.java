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
import org.eclipse.jst.ws.internal.consumption.command.common.AddModuleToServerCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateServerCommand;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceClient;

public class PreClientInstallCommand extends EnvironmentalOperation 
{
  private IWebServiceClient webServiceClient_;
  private String            project_;
  private String            module_;
  private String            earProject_;
  private String            ear_;
  
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
      Environment environment = getEnvironment();
      
      if (webServiceClient_.getWebServiceClientInfo().getServerInstanceId()==null)
      {
        CreateServerCommand createServerCommand = new CreateServerCommand();
        createServerCommand.setServerFactoryid(webServiceClient_.getWebServiceClientInfo().getServerFactoryId());
        createServerCommand.setEnvironment( environment );
        IStatus createServerStatus = createServerCommand.execute(null, null);
        if (createServerStatus.getSeverity()==Status.OK)
        {
          webServiceClient_.getWebServiceClientInfo().setServerInstanceId(createServerCommand.getServerInstanceId());
        }
        else
        {
          if (createServerStatus.getSeverity()==Status.ERROR)
          {
            environment.getStatusHandler().reportError(EnvironmentUtils.convertIStatusToStatus(createServerStatus));
          }               
          return createServerStatus;
        }
      }
        
      
      
      AddModuleToServerCommand command = new AddModuleToServerCommand();
      command.setServerInstanceId(webServiceClient_.getWebServiceClientInfo().getServerInstanceId());
      if (earProject_ != null && earProject_.length()>0 && ear_!= null && ear_.length()>0)
      {
        command.setProject(earProject_);
        command.setModule(ear_);
      }
      else
      {
        command.setProject(project_);
        command.setModule(module_);       
      }

      command.setEnvironment( environment );
      IStatus status = command.execute( null, null );
      if (status.getSeverity()==Status.ERROR)
      {
        environment.getStatusHandler().reportError( EnvironmentUtils.convertIStatusToStatus(status));
      }     
      return status;
	  }

    public void setProject( String project )
    {
      project_ = project;
    }
      
    public void setModule( String module )
    {
      module_ = module;
    } 
    
    public void setEarProject( String earProject )
    {
      earProject_ = earProject;
    }
    
    public void setEar( String ear )
    {
      ear_ = ear;  
    }
    
    public void setWebService( IWebServiceClient webServiceClient )
    {
      webServiceClient_ = webServiceClient;  
    }    
}
