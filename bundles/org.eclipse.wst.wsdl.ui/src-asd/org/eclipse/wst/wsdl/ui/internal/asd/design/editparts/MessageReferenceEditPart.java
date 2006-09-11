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
package org.eclipse.wst.wsdl.ui.internal.asd.design.editparts;

import java.util.List;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicsConstants;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDDragAndDropEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDGraphNodeDragTracker;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDSelectionEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.ListFigure;
import org.eclipse.wst.wsdl.ui.internal.asd.design.layouts.RowLayout;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;

public class MessageReferenceEditPart extends BaseEditPart implements IFeedbackHandler
{   
  protected Figure contentPane;  
  protected Label label;
  protected RowLayout rowLayout;
  
  protected ASDSelectionEditPolicy selectionHandlesEditPolicy = new ASDSelectionEditPolicy();
  
  protected IFigure createFigure()
  {
    ListFigure figure = new ListFigure(true);
    figure.setOpaque(true);
    figure.setPaintFirstLine(false);
    figure.setBackgroundColor(ColorConstants.tooltipBackground);

    rowLayout = new RowLayout();
    figure.setLayoutManager(rowLayout); 
    label = new Label();
    label.setLabelAlignment(Label.LEFT);
    //label.setFont(DesignViewGraphicsConstants.mediumFont); 
    label.setBorder(new MarginBorder(2, 16, 2 ,10));
    figure.add(label);  
    
    /*
    RectangleFigure partsList = new RectangleFigure();
    partsList.setLayoutManager(new ToolbarLayout(false));
    partsList.add(new Label("parts1"));
    partsList.add(new Label("parst2"));
    figure.add(partsList);
    */
    contentPane = new ListFigure();
    //contentPane.setForegroundColor(ColorConstants.lightGray);
    ((ListFigure)contentPane).setOpaque(true);
    contentPane.setBackgroundColor(ColorConstants.listBackground);
    ToolbarLayout toolbarLayout = new ToolbarLayout(false);
    
    /*
    ToolbarLayout toolbarLayout = new ToolbarLayout(false)
    {
       // TODO (cs) consider minor tweak here to ensure that the row fills up the available space             
       // vertically... without this a bit of trim is visible at the bottom of the list
       // when the param labels are given a margin width top and bottom <  '4' 
       public void layout(IFigure parent)
       {
         super.layout(parent);
         if (children.size() == 1)
         {
            Figure child = (Figure)children.get(0);
            //child.getBounds().height = parent.getClientArea().height;
         }  
       }
    };     
     */
    toolbarLayout.setStretchMinorAxis(true);
    contentPane.setLayoutManager(toolbarLayout);
    figure.add(contentPane);
    
    rowLayout.setConstraint(label, "MessageLabel"); //$NON-NLS-1$
    //rowLayout.setConstraint(partsList, "PartsList");
    rowLayout.setConstraint(contentPane, "MessageContentPane"); //$NON-NLS-1$
   
    // rmah: The block of code below has been moved from refreshVisuals().  We're
    // assuming the read-only state of the EditPart will never change once the
    // EditPart has been created.
    if (isReadOnly()) 
    {
      label.setForegroundColor(DesignViewGraphicsConstants.readOnlyLabelColor);
      label.getParent().setBackgroundColor(DesignViewGraphicsConstants.readOnlyMessageRefHeadingColor);
    }
    else
    {
      label.setForegroundColor(ColorConstants.black);
      label.getParent().setBackgroundColor(ColorConstants.tooltipBackground);
    }
    
    return figure;
  }
  
  protected void createEditPolicies()
  {
      super.createEditPolicies();  
	  if (!isReadOnly()) {
		  installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new ASDDragAndDropEditPolicy(getViewer(), selectionHandlesEditPolicy));
	  }
	  installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, selectionHandlesEditPolicy);
  }
  
  public DragTracker getDragTracker(Request request)
  {
    return new ASDGraphNodeDragTracker((EditPart)this);
  }
  
  public IFigure getContentPane()
  {
    return contentPane;
  }
  
  protected void refreshVisuals()
  {   
    super.refreshVisuals();
    IMessageReference message = (IMessageReference)getModel();    
    label.setText(message.getText());
    label.setIcon(message.getImage()); 
    
    // Resize column widths.  Sizes may have shrunk.
    rowLayout.getColumnData().clearColumnWidths();
    for (EditPart parent = getParent(); parent != null; parent = parent.getParent())
    {
      if (parent instanceof InterfaceEditPart)
      { 
        ((GraphicalEditPart)parent).getFigure().invalidateTree();
        break;
      }
    }
  }
  
  private Label messageLabel;
  
  protected void refreshChildren() {
	  super.refreshChildren();
	  
	  if (getModelChildren().size() > 0) {
		  if (messageLabel != null) {
			  contentPane.remove(messageLabel);
			  messageLabel = null;
		  }
	  }
	  else if (messageLabel == null){
		  messageLabel = new Label();
		  messageLabel.setText(((IMessageReference)getModel()).getPreview());
		  contentPane.add(messageLabel);
	  }
  }

  protected List getModelChildren()
  {
    IMessageReference theMessage = (IMessageReference)getModel();
    return theMessage.getParameters();   
  }

  public void addNotify()
  {
    InterfaceEditPart.attachToInterfaceEditPart(this, rowLayout);
    super.addNotify();    
  }
  
  public void addFeedback() {
	  label.getParent().setBackgroundColor(DesignViewGraphicsConstants.tableCellSelectionColor);
  }

  public void removeFeedback() {
	  if (isReadOnly()) {
	    label.getParent().setBackgroundColor(DesignViewGraphicsConstants.readOnlyMessageRefHeadingColor);
	  }
	  else {
		  label.getParent().setBackgroundColor(ColorConstants.tooltipBackground);
	  }
  }
  
  public EditPart getRelativeEditPart(int direction)
  {  
    EditPart editPart = super.getRelativeEditPart(direction);
    if (direction == PositionConstants.SOUTH && editPart == null)
    {
      editPart = EditPartNavigationHandlerUtil.getNextInterface(this);
    }        
    return editPart;
  }
}