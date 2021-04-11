package com.geomaticaeambiente.klemgui.ui;

import com.vividsolutions.jump.workbench.model.Layerable;

/**
 *
 * @author AdL
 */
public interface LayersChangedListener {
    void layerablesChanged(Layerable[] newLayers);
}
