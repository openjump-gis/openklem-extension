package com.geomaticaeambiente.klemgui.plugin.rastertools;

import java.awt.Color;
import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.openjump.core.ui.plugin.AbstractThreadedUiPlugIn;

import com.geomaticaeambiente.klemgui.ui.GUIUtils;
import com.geomaticaeambiente.klemgui.ui.InitialDialog;
import com.geomaticaeambiente.klemgui.ui.LayerablesList;
import com.geomaticaeambiente.klemgui.ui.MainPanel;
import com.geomaticaeambiente.klemgui.ui.PersonalTableComponents;
import com.geomaticaeambiente.klemgui.ui.YourTableCellRenderer1;
import com.geomaticaeambiente.klemgui.utils.AbstractInputKlemPlugin;
import com.geomaticaeambiente.klemgui.utils.ActionObject;
import com.geomaticaeambiente.klemgui.utils.ComponentsTreeMap;
import com.geomaticaeambiente.klemgui.utils.InitialData;
import com.geomaticaeambiente.klemgui.utils.PersonalTable;
import com.geomaticaeambiente.klemgui.utils.PluginUtils;
import com.geomaticaeambiente.klemgui.utils.RasterUtils;
import com.geomaticaeambiente.openjump.klem.grid.DoubleBasicGrid;
import com.geomaticaeambiente.openjump.klem.rastertools.RasterAggregator;
import com.geomaticaeambiente.openjump.klem.rastertools.RasterAggregator.AggregationMethod;
import com.geomaticaeambiente.openjump.klem.rastertools.RasterAggregator.OverlayMethod;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.Logger;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;

/**
 *
 * @author Geomatica
 */
public class AggregateRastersPlugin extends AbstractInputKlemPlugin {

    public AggregateRastersPlugin(PlugInContext context,
            InitialDialog initialDialog, LayerablesList layerablesList) {
        super(context, initialDialog);
        this.context = context;
        this.layerablesList = layerablesList;
    }

    @Override
    public InitialData setInitialData() {

        aggregMethods = new String[] { AGG_MIN, AGG_MAX, AGG_MEAN, AGG_SUM,
                AGG_MAJORITY };

        overlayMethods = new String[] { OVER_MAX, OVER_MIN, OVER_MEAN,
                OVER_MOST_SIGNIFICANT_FIRST, OVER_MOST_SIGNIFICANT_LAST };

        final PersonalTable personalTable = new PersonalTable(listTableModel(),
                null, false, false, true, true, true, "Raster files",
                new String[] { "asc", "flt", "grd", "tiff", "tif" }, true);
        final InitialData initialData = new InitialData();

        //input data
        initialData.setParam_PersonalTable(personalTable, null, GUIUtils.INPUT);//table
        initialData.setParam_Label_TextBox(
                GUIUtils.setGUILabel(AGGREGATION_LABEL), aggregMethods,
                GUIUtils.INPUT);
        initialData.setParam_Label_TextBox(GUIUtils.setGUILabel(OVERLAY_LABEL),
                overlayMethods, GUIUtils.INPUT);

        initialData.setParam_Label_TextBox(
                GUIUtils.setGUILabel(CELLSIZE_LABEL), "", GUIUtils.INPUT);//interval  

        //output data
        initialData.setParam_Label_TextBox_Button(
                GUIUtils.setGUILabel(GUIUtils.getOutputRasterLabel()), "",
                new ActionObject(""), GUIUtils.OUTPUT);

        return initialData;

    }

    @Override
    public ComponentsTreeMap setComponentsActions(
            ComponentsTreeMap personalTreeMap) {

        //        JButton jButton_findValue = (JButton) personalTreeMap.getComponent("01", GUIUtils.INPUT, 2); //Find values button  // 01 input= row   2=column

        //        final CustomComboBox.RasterComboBox raster = (CustomComboBox.RasterComboBox) (JComboBox) personalTreeMap.getComponent("00", GUIUtils.INPUT, 1);//raster in combobox action
        //        raster.addActionListener(new ActionListener() {
        //
        //            @Override
        //            public void actionPerformed(ActionEvent e) {
        //                rasterChanged = true;
        //            }
        //        });

        //        final PersonalTableComponents personalComp_Table =
        //                (PersonalTableComponents) personalTreeMap.getComponent("00", GUIUtils.INPUT, 0); //table 
        //        final JComboBox jComboBox_Aggregation = (JComboBox) personalTreeMap.getComponent("01", GUIUtils.INPUT, 1); // aggregation methods
        //        final JComboBox jComboBox_Overlay = (JComboBox) personalTreeMap.getComponent("02", GUIUtils.INPUT, 1); // overlay methods 
        //        final JTextField jTextField_CellSize = (JTextField) personalTreeMap.getComponent("03", GUIUtils.INPUT, 1);
        //           
        //        final JLabel jLabel_Aggregation = (JLabel) personalTreeMap.getComponent("01", GUIUtils.INPUT, 0);
        //        final JLabel jLabel_Overlay = (JLabel) personalTreeMap.getComponent("02", GUIUtils.INPUT, 0);

        //output
        final JTextField outputTextField = (JTextField) personalTreeMap
                .getComponent("00", GUIUtils.OUTPUT, 1);
        final JButton outputButton = (JButton) personalTreeMap.getComponent(
                "00", GUIUtils.OUTPUT, 2);
        outputButton.setIcon(PluginUtils.getFolderIcon());
        outputButton.addActionListener(GUIUtils
                .setSaveRasterTif(outputTextField));

        return personalTreeMap;
    }

    public void aggregateRastersCommand(
            final ComponentsTreeMap componentsWithActions) throws Exception {
        //input values 
        final String newValuesTable = GUIUtils
                .getStringValue(componentsWithActions.getComponent("00",
                        GUIUtils.INPUT, 0)); //TODO: CHECK
        final String[] fileNames = newValuesTable.split("_;");

        final String aggregMethod = GUIUtils
                .getStringValue(componentsWithActions.getComponent("01",
                        GUIUtils.INPUT, 1));
        final String overlayMethod = GUIUtils
                .getStringValue(componentsWithActions.getComponent("02",
                        GUIUtils.INPUT, 1));
        final String cellSize = GUIUtils.getStringValue(componentsWithActions
                .getComponent("03", GUIUtils.INPUT, 1));

        //get output raster name
        final String outRasterName = GUIUtils
                .getStringValue(componentsWithActions.getComponent("00",
                        GUIUtils.OUTPUT, 1));

        //check input and output values
        checkValues(fileNames, aggregMethod, overlayMethod, cellSize,
                outRasterName);

        //extract raster selected from combobox
        super.getInitialDialog().setCursor(new Cursor(Cursor.WAIT_CURSOR));

        final List<DoubleBasicGrid> inputGrids_l = new ArrayList<>();
        for (final String fileName : fileNames) {
            inputGrids_l.add(RasterUtils.getDoubleBasicGridFromFile(new File(
                    fileName)));
        }

        final DoubleBasicGrid outputGrid = RasterAggregator.aggregateRasters(
                inputGrids_l.toArray(new DoubleBasicGrid[0]),
                Double.parseDouble(cellSize),
                AggregationMethod.valueOf(aggregMethod),
                OverlayMethod.valueOf(overlayMethod));

        //extract values from table     

        //        //exceute reclassification

        //        ///Create the output rasterImageLayer and display on OJ    
        //Save grid as tiff
        RasterUtils.saveOutputRasterAsTiff(outputGrid, new File(outRasterName));
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
                                    aggregateRastersCommand(componentsWithActions);
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

            }

            @Override
            public void centerButton() {

            }

        };

        return mainPanel;

    }

    @Override
    public String toString() {
        return PluginUtils.i18n(
                "AggregateRastersPlugin.PlugInName.text");
    }

    private DefaultTableModel listTableModel() {

        final String col[] = { FILE_LABEL };

        final String data[][] = { { null } };

        final DefaultTableModel dtm = new DefaultTableModel(data, col);

        // Empty table
        for (int r = dtm.getRowCount() - 1; r >= 0; --r) {
            boolean toRemove = true;
            for (int c = 0; c < dtm.getColumnCount(); c++) {
                final Object cellCont = dtm.getValueAt(r, c);
                if (cellCont != null) {
                    toRemove = false;
                    break;
                }
            }
            if (toRemove) {
                dtm.removeRow(r);
            }
        }

        return dtm;

    }

    private static void setCellRenderer(int row, PersonalTableComponents ptc) {

        final YourTableCellRenderer1 ytcr = new YourTableCellRenderer1();
        ytcr.setRow(row);
        ytcr.setForeColour(Color.BLUE);
        for (int c = 0; c < ptc.getTabel().getColumnCount(); c++) {
            ptc.getTabel().getColumnModel().getColumn(c).setCellRenderer(ytcr);
        }

    }

    private void enableElement(JComboBox method, JLabel classesLabel,
            JComboBox classes, JLabel stdevLabel, JComboBox stDev,
            JLabel intervalLabel, JTextField intervals) {
        switch (method.getSelectedIndex()) {

        /* Standard deviations */
        case 1:

            classesLabel.setEnabled(false);
            classes.setEnabled(false);
            stdevLabel.setEnabled(true);
            stDev.setEnabled(true);
            intervalLabel.setEnabled(false);
            intervals.setEnabled(false);

            break;
        /* Equal Interval */
        case 2:

            classesLabel.setEnabled(false);
            classes.setEnabled(false);
            stdevLabel.setEnabled(false);
            stDev.setEnabled(false);
            intervalLabel.setEnabled(true);
            intervals.setEnabled(true);

            break;

        /* Intevals */
        case 3:

            classesLabel.setEnabled(true);
            classes.setEnabled(true);
            stdevLabel.setEnabled(false);
            stDev.setEnabled(false);
            intervalLabel.setEnabled(false);
            intervals.setEnabled(false);

            break;
        /* manual */
        case 4:
            classesLabel.setEnabled(false);
            classes.setEnabled(false);
            stdevLabel.setEnabled(false);
            stDev.setEnabled(false);
            intervalLabel.setEnabled(false);
            intervals.setEnabled(false);
            break;

        }
    }

    private void checkValues(String[] rasters, String aggMethos,
            String overlayMethod, String cellSize, String rasterOut)
            throws Exception {

        for (final String raster : rasters) {
            GUIUtils.checkStringValue(raster, RASTER_IN_LABEL);
        }
        GUIUtils.checkStringValue(aggMethos, AGGREGATION_LABEL);
        GUIUtils.checkStringValue(overlayMethod, OVERLAY_LABEL);

        try {
            final double cellSizeVal = Double.parseDouble(cellSize);
            if (cellSizeVal < 0) {
                throw new IOException(PluginUtils.i18n("Check.CheckStringValue.message")
                        .concat(CELLSIZE_LABEL));
            }
        } catch (final NumberFormatException ex) {
            throw new IOException(PluginUtils.i18n("Check.CheckStringValue.message")
                    .concat(CELLSIZE_LABEL));
        } catch (final IOException ex) {
            throw new IOException(PluginUtils.i18n("Check.CheckStringValue.message")
                    .concat(CELLSIZE_LABEL));
        }

        if (rasterOut != null) {
            GUIUtils.checkFileValue(rasterOut, GUIUtils.getOutputRasterString());
        }

    }

    //    private RasterImageLayer[] rasterImageLayers;
    private final PlugInContext context;
    private MainPanel mainPanel;
    private final String AGG_MIN = "MIN"; //PluginUtils.i18n("ReclassRasterPlugin.StDev.text");
    private final String AGG_MAX = "MAX";
    private final String AGG_MEAN = "MEAN";
    private final String AGG_SUM = "SUM";
    private final String AGG_MAJORITY = "MAJORITY";

    private final String OVER_MOST_SIGNIFICANT_FIRST = "MOST_SIGNIFICANT_FIRST"; //PluginUtils.i18n("ReclassRasterPlugin.StDev.text");
    private final String OVER_MOST_SIGNIFICANT_LAST = "MOST_SIGNIFICANT_LAST";
    private final String OVER_MEAN = "MEAN";
    private final String OVER_MAX = "MAX";
    private final String OVER_MIN = "MIN";

    private final String RASTER_IN_LABEL = PluginUtils.i18n("KlemGUI.InputRaster.label");
    private final String AGGREGATION_LABEL = "Aggregation method";
    private final String OVERLAY_LABEL = "Overlay method";
    private final String CELLSIZE_LABEL = "Output cell size";
    private final String FILE_LABEL = "File";

    private String[] aggregMethods;
    private String[] overlayMethods;

    private final LayerablesList layerablesList;

}
