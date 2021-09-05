package com.geomaticaeambiente.klemgui.plugin;

import com.geomaticaeambiente.klemgui.utils.PluginUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.Extension;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 *
 * @author Geomatica
 */
public class KlemExtension extends Extension{

    private static final I18N I18N = com.vividsolutions.jump.I18N.getInstance("com.geomaticaeambiente.klemgui");

    public String getName() {
        return PluginUtils.plugInName;
    }

    @Override
    public String getVersion() {
      return PluginUtils.version + " " + PluginUtils.versionNumber;
    }

    @Override
    public void configure(PlugInContext context) throws Exception {
        new StartPlugIn().initialize(context);
    }

    /**
     * one method to translate them all :)
     */
    public static String i18n( String key, Object... o) {
      return I18N.get(key, o);
    }
}
