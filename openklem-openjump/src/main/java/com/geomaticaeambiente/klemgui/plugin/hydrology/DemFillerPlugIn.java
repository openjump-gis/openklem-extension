package com.geomaticaeambiente.klemgui.plugin.hydrology;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.openjump.core.rasterimage.RasterImageLayer;
import org.openjump.core.ui.plugin.AbstractThreadedUiPlugIn;

import com.geomaticaeambiente.klemgui.ui.CustomComboBox;
import com.geomaticaeambiente.klemgui.ui.GUIUtils;
import com.geomaticaeambiente.klemgui.ui.InitialDialog;
import com.geomaticaeambiente.klemgui.ui.LayerablesList;
import com.geomaticaeambiente.klemgui.ui.MainPanel;
import com.geomaticaeambiente.klemgui.utils.AbstractInputKlemPlugin;
import com.geomaticaeambiente.klemgui.utils.ActionObject;
import com.geomaticaeambiente.klemgui.utils.ComponentsTreeMap;
import com.geomaticaeambiente.klemgui.utils.InitialData;
import com.geomaticaeambiente.klemgui.utils.PluginUtils;
import com.geomaticaeambiente.klemgui.utils.RasterUtils;
import com.geomaticaeambiente.openjump.klem.fill.DemFiller;
import com.geomaticaeambiente.openjump.klem.grid.DoubleBasicGrid;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.Logger;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;

/**
 * Plugin for fill the DEM. The raster must be loaded on OJ TOC. The output
 * raster will be displayed on OJ.
 *
 * @author Geomatica
 */
public class DemFillerPlugIn extends AbstractInputKlemPlugin {

    public DemFillerPlugIn(PlugInContext context, InitialDialog initialDialog,
            LayerablesList rasterLayersList) {
        super(context, initialDialog);
        this.context = context;
        layerablesList = rasterLayersList;
    }

    @Override
    public InitialData setInitialData() {

        //        rasterImageLayers = PluginUtils.getRasterImageLayers(layerablesList.getLayerables());

        final InitialData initialData = new InitialData();
        //input data
        initialData
                .setParam_Label_TextBox(GUIUtils.setGUILabel(RASTER_DEM),
                        PluginUtils.getRasterImageLayers(layerablesList
                                .getLayerables()), GUIUtils.INPUT);

        //output data
        initialData.setParam_Label_TextBox_Button(
                GUIUtils.setGUILabel(RASTER_PITFILLEDDEM), "",
                new ActionObject(""), GUIUtils.OUTPUT);

        return initialData;

    }

    @Override
    public ComponentsTreeMap setComponentsActions(
            ComponentsTreeMap personalTreeMap) {

        final JTextField outputTextField = (JTextField) personalTreeMap
                .getComponent("00", GUIUtils.OUTPUT, 1);
        final JButton outputButton = (JButton) personalTreeMap.getComponent(
                "00", GUIUtils.OUTPUT, 2);
        outputButton.setIcon(PluginUtils.getFolderIcon());
        outputButton.addActionListener(GUIUtils
                .setSaveRasterTif(outputTextField));

        return personalTreeMap;
    }

    public void demFillerCommand(final ComponentsTreeMap componentsWithActions)
            throws Exception {
        //get input raster names
        final String rasterSelected1 = GUIUtils
                .getStringValue(componentsWithActions.getComponent("00",
                        GUIUtils.INPUT, 1));

        //get output raster name
        final String outRasterName = GUIUtils
                .getStringValue(componentsWithActions.getComponent("00",
                        GUIUtils.OUTPUT, 1));

        //check input values
        GUIUtils.checkStringValue(rasterSelected1, RASTER_DEM);
        GUIUtils.checkFileValue(outRasterName, GUIUtils.getOutputRasterString());

        //get input raster as rasterImageLayer from string
        final RasterImageLayer inputRasterSelected = PluginUtils
                .getRasterImageLayerSelected((CustomComboBox.RasterComboBox) componentsWithActions
                        .getComponent("00", GUIUtils.INPUT, 1));
        final DoubleBasicGrid demGrid = RasterUtils
                .getDoubleBasicGrid(inputRasterSelected);

        //Fill...
        final DemFiller demFiller = new DemFiller(demGrid);
        final DoubleBasicGrid demFilled = demFiller.calculate();

        //Save grid as tiff
        RasterUtils.saveOutputRasterAsTiff(demFilled, new File(outRasterName));
        //Display raster on OJ from file                
        RasterUtils.displayRasterFileOnOJ(context.getWorkbenchContext(),
                new File(outRasterName), null);

        JOptionPane.showMessageDialog(super.getInitialDialog(), PluginUtils.i18n("SetWorkspacePlugin.Done.message"),
                PluginUtils.plugInName, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public JPanel buildPluginPanel(final ComponentsTreeMap componentsWithActions) {

        if (mainPanel != null) {
            return mainPanel;
        }
        mainPanel = new MainPanel(super.getInitialDialog(),
                componentsWithActions, false, false, true, PluginUtils.i18n(
                                "MainPanel.ExecuteButton.text"), layerablesList) {

            /**
                     * 
                     */
            private static final long serialVersionUID = 1L;

            @Override
            public void rightButton() {
                try {

                    AbstractPlugIn.toActionListener(
                            new AbstractThreadedUiPlugIn() {
                                @Override
                                public String getName() {
                                    return null;
                                }

                                @Override
                                public boolean execute(PlugInContext context) {
                                    return true;
                                }

                                @Override
                                public void run(TaskMonitor monitor,
                                        PlugInContext context) throws Exception {
                                    monitor.report(PluginUtils.i18n(
                                                    "OpenKlem.executing-process"));
                                    reportNothingToUndoYet(context);
                                    monitor.allowCancellationRequests();
                                    demFillerCommand(componentsWithActions);

                                }
                            }, context.getWorkbenchContext(),
                            new TaskMonitorManager()).actionPerformed(null);

                } catch (final Exception ex) {
                    ErrorDialog.show(super.getInitialDialog(),
                            PluginUtils.plugInName, ex.toString(),
                            StringUtil.stackTrace(ex));
                    Logger.error(PluginUtils.plugInName, ex);
                }
            }

            @Override
            public void leftButton() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void centerButton() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        return mainPanel;
    }

    @Override
    public String toString() {
        return PluginUtils.i18n(
                "DemFillerPlugIn.PlugInName.label");
    }

    private MainPanel mainPanel;
    private final PlugInContext context;
    private final String RASTER_DEM = PluginUtils.i18n(
            "KlemGUI.InputDem.label");
    private final String RASTER_PITFILLEDDEM = PluginUtils.i18n("KlemGUI.InputFilledDem.label");
    private final LayerablesList layerablesList;

}
