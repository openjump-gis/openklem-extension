package com.geomaticaeambiente.klemgui.utils;

import java.awt.Cursor;
import java.awt.Shape;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.locationtech.jts.geom.Coordinate;

import com.geomaticaeambiente.klemgui.exceptions.WarningException;
import com.geomaticaeambiente.klemgui.plugin.hydrology.hydrographs.klem.WatershedInformation;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.cursortool.NClickTool;

public class WatershedTool extends NClickTool {
    
   
       
    public WatershedTool(PlugInContext context, WatershedInformation wi) {
        super(1);
        
        this.context = context;
        this.watershedInfo = wi;
    }
    
    @Override
    public Icon getIcon() {
        
        return null;
    }
    
    @Override
    protected Shape getShape() {
        //Don't want anything to show up when the user drags. [Jon Aquino]
        return null;
    }
    
    @Override
    public Cursor getCursor() {
        
        return Cursor.getDefaultCursor();
    }
    
    @Override
    protected void gestureFinished() throws Exception {
        reportNothingToUndoYet();
        try {
            display(getCoordinates());
        } catch(WarningException ex) {
            JOptionPane.showMessageDialog(
                    context.getWorkbenchFrame(),
                    ex.getMessage(),
                    PluginUtils.plugInName,
                    JOptionPane.WARNING_MESSAGE);            
        }
    }
    
    private void display(List coordinates) throws Exception {

        if (coordinates.size() > 0) {            
            Coordinate coord = (Coordinate)coordinates.get(0);
            
            DecimalFormat df = (DecimalFormat)DecimalFormat.getInstance();
            df.applyPattern("0.000");
            
            watershedInfo.setCoordinate(coord);
            watershedInfo.setCoordinateText();
            
            if(watershedInfo.isCalcArea()==true) watershedInfo.setAreaText();
            
            if(watershedInfo.isCalcElevation()==true) watershedInfo.setElevationText();
            
        }
    }
   
    private final PlugInContext context;
    private final WatershedInformation watershedInfo;
    
}

