# openklem-extension
OpenKlem is an extension of OpenJUMP for hydrological analysis, it includes also a module for simulating flood hydrograph (Kinematik Local Excess Model, KLEM).The plugin is included into OpenJUMP Plus edition.

Openklem has been mavenized and Sources refactored to be integrated in OpenJUMP 2.
It is now split into 3 modules :
- **openklem-core** : describes the model used by algo. No dependency.
- **openklem-algo** : a collection of algorithms related to hydrology.
  Depends on openklem-core, jts and jep (an expression parser)
- **openklem-openjump** : gui interfaces and openjump plugins/extension.
  Depends on
  other openklem modules, openjump-2, jts-1.18+, jfreechart, 
  commons-imaging and for some export capabilities, odftoolkit.

Generally you can just clone the git repo and run `mvn package` in the checked out folder. Dependencies within the modules ahould be resolved properly.

If that fails you may compile/install them locally in your .m2 repository. step by step:
1. chdir into `openklem-core/` and install it locally (`mvn install`)
2. chdir into `openklem-algo/` and install it locally (`mvn install`)
3. compile/package openklem-openjump should work now

