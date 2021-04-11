# openklem-extension
OpenKLEM is an extension of OpenJUMP for hydrological analysis

Code has been refactored to be integrated in OpenJUMP 2.
It is now split into 3 modules :
- **openklem-core** : describes the model used by algo. No dependency.
- **openklem-algo** : a collection of algorithms related to hydrology.
  Depends on openklem-core, jts and jep (an expression parser)
- **openklem-openjump** : gui interfaces and openjump plugins. Depends on
  other openklem modules, openjump-2, jts-1.18+, jfreechart, 
  commons-imaging and for some export capabilities, odftoolkit.

Openklem has been mavenized. As some modules depends on others, you may have 
to compile/install it in your local .m2 repository step by step :
1. compile openklem-core and install it locally (mvn install)
2. compile openklem-algo and install it locally (mvn install)
3. compile openklem-openjump and install it locally (mvn install)

