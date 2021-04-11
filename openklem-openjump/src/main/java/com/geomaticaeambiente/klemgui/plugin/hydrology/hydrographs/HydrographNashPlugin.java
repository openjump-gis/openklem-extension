package com.geomaticaeambiente.klemgui.plugin.hydrology.hydrographs;

import com.geomaticaeambiente.klemgui.utils.PluginUtils;
import com.geomaticaeambiente.klemgui.ui.CommonHydroPanel;
import com.geomaticaeambiente.klemgui.ui.GUIUtils;
import com.geomaticaeambiente.klemgui.ui.InitialDialog;
import com.geomaticaeambiente.klemgui.ui.MainPanel;
import com.geomaticaeambiente.klemgui.ui.LayerablesList;
import com.geomaticaeambiente.klemgui.utils.AbstractInputKlemPlugin;
import com.geomaticaeambiente.klemgui.utils.ActionObject;
import com.geomaticaeambiente.klemgui.utils.CommonHydrographData;
import com.geomaticaeambiente.klemgui.utils.ComponentsTreeMap;
import com.geomaticaeambiente.klemgui.utils.HydroUtils;
import com.geomaticaeambiente.klemgui.utils.InitialData;
import com.geomaticaeambiente.openjump.klem.hydrology.Hydrology;
import com.geomaticaeambiente.openjump.klem.hydrology.UnitHydrograph;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import it.geomaticaeambiente.klem.Hyetograph;
import it.geomaticaeambiente.klem.TimeInterval;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author Geomatica
 */
public class HydrographNashPlugin extends AbstractInputKlemPlugin {

    public HydrographNashPlugin(PlugInContext context, InitialDialog initialDialog, LayerablesList layerablesList) {
        super(context, initialDialog);
        this.context = context;
        this.layerablesList = layerablesList;
    }

    @Override
    public InitialData setInitialData() {

        InitialData initialData = new InitialData();

        //Input
        CommonHydrographData common = new CommonHydrographData();
        initialData.setParam_CommonHydroData(common, GUIUtils.INPUT);

        //Other
        initialData.setParam_Label_TextBox(GUIUtils.setGUILabel(BASE_FLOW), "", GUIUtils.OTHER);
        initialData.setParam_Label_TextBox(GUIUtils.setGUILabel(SHAPE_PARAM), "", GUIUtils.OTHER);
        initialData.setParam_Label_TextBox(GUIUtils.setGUILabel(SCALE_PARAM), "", GUIUtils.OTHER);

        //graphic unit
        initialData.setParam_Labels(new String[]{GUIUtils.setGUILabel(PluginUtils.getResources().getString("HyetographPlugIn.XUnits.label"))}, GUIUtils.OTHER); //label x unit
        initialData.setParam_Action(new ActionObject(
                new String[]{PluginUtils.getResources().getString("KlemGUI.hours.label"),
                    PluginUtils.getResources().getString("KlemGUI.minutes.label")}), GUIUtils.OTHER);//radio buttons: hours and minutes

        return initialData;

    }

    @Override
    public ComponentsTreeMap setComponentsActions(ComponentsTreeMap personalTreeMap) {
        //set radiobutton selection
        JRadioButton jradioButton_Units = (JRadioButton) personalTreeMap.getComponent("04", GUIUtils.OTHER, 0);
        jradioButton_Units.setSelected(true);

        return personalTreeMap;
    }

    @Override
    public JPanel buildPluginPanel(final ComponentsTreeMap componentsWithActions) {
        if(this.mainPanel != null) {
            return this.mainPanel;
        }
        this.mainPanel = new MainPanel(super.getInitialDialog(), componentsWithActions, false, false, true,
                PluginUtils.getResources().getString("MainPanel.ExecuteButton.text"), layerablesList) {

            @Override
            public void rightButton() {
                try {

                    //input values
                    CommonHydrographData hydroData = GUIUtils.getCommonHydroDataFromPanel((CommonHydroPanel) componentsWithActions.getComponent("00", GUIUtils.INPUT, 0));

                    double watershedArea = hydroData.getWatershedArea();
                    hyetograph = hydroData.getHyetograph();
                    effectiveRainfall = hydroData.getEffectiveRainfall();
                    double baseFlow = GUIUtils.getDoubleValue(componentsWithActions.getComponent("00", GUIUtils.OTHER, 1));
                    double shapeParam = GUIUtils.getDoubleValue(componentsWithActions.getComponent("01", GUIUtils.OTHER, 1));
                    double scaleParam = GUIUtils.getDoubleValue(componentsWithActions.getComponent("02", GUIUtils.OTHER, 1));

                    checksParameters(baseFlow, shapeParam, scaleParam);

                    int units = GUIUtils.getSelectedJRadioButton((JRadioButton) componentsWithActions.getComponent("04", GUIUtils.OTHER, 0), // units: hours
                            (JRadioButton) componentsWithActions.getComponent("04", GUIUtils.OTHER, 1)); //units: minutes

                    if (units == 0) {
                        xUnit = TimeInterval.TimeIntervalUnit.HOUR;
                    } else if (units == 1) {
                        xUnit = TimeInterval.TimeIntervalUnit.MINUTE;
                    }

                    timeStep = effectiveRainfall.getEffectiveRainfall()[0][1]
                            - effectiveRainfall.getEffectiveRainfall()[0][0];

                    unitHydrograph = Hydrology.calcIuhNash(timeStep, watershedArea, baseFlow,
                            effectiveRainfall, shapeParam, scaleParam);

                    HydroUtils.addOuputPanel(
                            context, super.getInitialDialog(),
                            hyetograph,
                            effectiveRainfall,
                            unitHydrograph,
                            xUnit,
                            HydroUtils.HydroType.NASH,
                            layerablesList);

                } catch (Exception ex) {
                    ErrorDialog.show(
                            super.getInitialDialog(),
                            PluginUtils.plugInName,
                            ex.toString(),
                            StringUtil.stackTrace(ex));
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
        return this.mainPanel;
    }

    @Override
    public String toString() {
        return PluginUtils.getResources().getString("HydrographNashPlugin.PluginName.label");
    }

    private void checksParameters(double baseFlow, double shapeParam, double scaleParam) {

        HydroUtils.checkHydroValue(BASE_FLOW, baseFlow, 0d, Double.MAX_VALUE);
        HydroUtils.checkHydroValue(SHAPE_PARAM, shapeParam, 0d, Double.MAX_VALUE);
        HydroUtils.checkHydroValue(SCALE_PARAM, scaleParam, 0d, Double.MAX_VALUE);

    }

    private PlugInContext context;
    private MainPanel mainPanel;
    private Hyetograph hyetograph;
    private Hydrology.EffectiveRainfall effectiveRainfall;
    private TimeInterval.TimeIntervalUnit xUnit;
    private double timeStep;
    private UnitHydrograph unitHydrograph;
    private final String BASE_FLOW = PluginUtils.getResources().getString("HydrographCommonLabels.BaseFlow.label");
    private final String SHAPE_PARAM = PluginUtils.getResources().getString("HydrographNashPlugin.ShapeParam.label");
    private final String SCALE_PARAM = PluginUtils.getResources().getString("HydrographNashPlugin.ScaleParam.label");

    private final LayerablesList layerablesList;
}
