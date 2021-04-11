## OpenKLEM core

#### 2018-13-07
* Class: com.geomaticaeambiente.openjump.klem.slope.SlopeStripe
  Added radians output
* General Added gpl_2 and ChangeLog files

#### 2018-07-26  Ricompiled the libraries:
* Enabled project specific setting on Project/Java compiler/Error-Warning  
  to deactivated some warning on compiling
* Delate class it.geomaticaambiente.klem.klemDA as creating error on compiling

## OpenKLEM-OpenJUMP

#### 2018-06-18
* General. Changed icons to famfamfam or OJ ones. Added gpl_2 file
* General. Optimized icon view on Windows.
* Kinematic Local Excess Model - Output. Added option to export tables to
  .csv file
* Kinematic Local Excess Model - Output.
  Option "Export to .ods" (to libreoffice) activated only if OpenDoc libs
  are located into OJ/LIB/EXT
* Kinematic Local Excess Model - Output.
  Output is controlled by embedded OJ libs so that the process can
  be stopped if it takes a long time

#### 2018-07-12 Giuseppe Aruta. Kinematic Local Excess Model.
* Option: "Basin area km2", substituted scientific notation (2E-3) with
  numeric one (0.002)
* Sub-basin warning now shows also the range of area within the algorithm can
  be applied
* Substituted [m s.m.m.] with:  [m s.l.m]-Italian (metri sul livello marino)
  and [m a.s.l.]-English (metre above sea level)

#### 2018-07-26 Giuseppe Aruta. PluginUtils class - Kinematic Local Excess Model.
* Loading project JFileChooser: added method oiption to load project raster files
  into OpenJUMP view when loading project file
* Added ChangeLog file.