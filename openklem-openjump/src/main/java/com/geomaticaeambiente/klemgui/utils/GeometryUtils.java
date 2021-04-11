package com.geomaticaeambiente.klemgui.utils;

 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;

import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;

/**
 *
 * @author AdL
 */
public class GeometryUtils {
    
    public static LineString[] getLineStringsFromFeatures(FeatureCollection featColl) {
        
        List<LineString> lineStrings_l = new ArrayList<>();
        Iterator iter = featColl.iterator();
        while(iter.hasNext()) {
            Feature feature = (Feature) iter.next();
            Geometry geom = feature.getGeometry();
            if(geom.getGeometryType().toUpperCase().equals("LINESTRING")) {
                if(!geom.isEmpty()) {
                    lineStrings_l.add((LineString) geom);
                }
            } else if(geom.getGeometryType().toUpperCase().equals("MULTILINESTRING")) {
                for(int l=0; l<geom.getNumGeometries(); l++) {
                    Geometry geom1 = geom.getGeometryN(l);
                    if(!geom.isEmpty()) {
                        lineStrings_l.add((LineString) geom1);
                    }
                }
            }
        }
        return lineStrings_l.toArray(new LineString[0]);

    }
    
    
}
