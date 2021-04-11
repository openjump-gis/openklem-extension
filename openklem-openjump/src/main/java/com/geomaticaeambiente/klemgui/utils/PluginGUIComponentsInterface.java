package com.geomaticaeambiente.klemgui.utils;

import javax.swing.JScrollPane;

/**
 * Interface class to create the extension's plugin classes.
 * @author Geomatica
 */
public interface PluginGUIComponentsInterface {
    
    ComponentsTreeMap setComponetsTreeMap(InitialData initialtData) throws Exception;
    
    JScrollPane getTabPluginComponents() throws Exception; //ex input tab
    
}
