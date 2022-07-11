KC=	kotlinc
KFLAG=	-cp
GLIB=	ve/usb/libGrafo

all:	libGrafoKt.jar

libGrafoKt.jar: $(GLIB)/Color.class\
		$(GLIB)/Lado.class\
		$(GLIB)/Arco.class\
		$(GLIB)/ArcoCosto.class\
		$(GLIB)/Arista.class\
		$(GLIB)/AristaCosto.class\
		$(GLIB)/Grafo.class\
		$(GLIB)/GrafoDirigido.class\
		$(GLIB)/GrafoNoDirigido.class\
		$(GLIB)/GrafoDirigidoCosto.class\
		$(GLIB)/GrafoNoDirigidoCosto.class\
		$(GLIB)/VerticeTp.class\
		$(GLIB)/VerticeDFS.class\
		$(GLIB)/VerticeBFS.class\
		$(GLIB)/BFS.class\
		$(GLIB)/DFS.class\
		$(GLIB)/OrdenamientoTopologico.class\
		$(GLIB)/CicloDigrafo.class\
		$(GLIB)/ParticionNiveles.class\
		$(GLIB)/Utilidades.class\
		$(GLIB)/CFC.class\
		$(GLIB)/VerticeDFSCC.class\
		$(GLIB)/ComponentesConexasDFS.class\
		$(GLIB)/ElementoCD.class\
		$(GLIB)/ConjuntosDisjuntos.class\
		$(GLIB)/ComponentesConexasCD.class\
		$(GLIB)/KruskalAMC.class\
		$(GLIB)/VerticePrim.class\
		$(GLIB)/PrimAMC.class\
		$(GLIB)/TablaGrafoDirigido.class\
		$(GLIB)/TablaGrafoNoDirigido.class\
		$(GLIB)/CCM_BellmanFord.class\
		$(GLIB)/VerticeDijkstra.class\
		$(GLIB)/CCM_Dijkstra.class


	jar -cvf $@ $(GLIB)/*.class 

$(GLIB)/Color.class: $(GLIB)/Color.kt
	$(KC) $(GLIB)/Color.kt

$(GLIB)/Lado.class: $(GLIB)/Lado.kt
	$(KC) $(GLIB)/Lado.kt

$(GLIB)/Arco.class: $(GLIB)/Arco.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/Arco.kt

$(GLIB)/ArcoCosto.class: $(GLIB)/ArcoCosto.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/ArcoCosto.kt

$(GLIB)/Arista.class: $(GLIB)/Arista.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/Arista.kt

$(GLIB)/AristaCosto.class: $(GLIB)/AristaCosto.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/AristaCosto.kt

$(GLIB)/Grafo.class: $(GLIB)/Grafo.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/Grafo.kt

$(GLIB)/GrafoDirigido.class: $(GLIB)/GrafoDirigido.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/GrafoDirigido.kt

$(GLIB)/GrafoDirigidoCosto.class: $(GLIB)/GrafoDirigidoCosto.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/GrafoDirigidoCosto.kt

$(GLIB)/GrafoNoDirigido.class: $(GLIB)/GrafoNoDirigido.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/GrafoNoDirigido.kt

$(GLIB)/GrafoNoDirigidoCosto.class: $(GLIB)/GrafoNoDirigidoCosto.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/GrafoNoDirigidoCosto.kt

$(GLIB)/BFS.class: $(GLIB)/BFS.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/BFS.kt

$(GLIB)/DFS.class: $(GLIB)/DFS.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/DFS.kt

$(GLIB)/VerticeTp.class: $(GLIB)/VerticeTp.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/VerticeTp.kt

$(GLIB)/OrdenamientoTopologico.class: $(GLIB)/OrdenamientoTopologico.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/OrdenamientoTopologico.kt

$(GLIB)/VerticeDFS.class: $(GLIB)/VerticeDFS.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/VerticeDFS.kt

$(GLIB)/VerticeBFS.class: $(GLIB)/VerticeBFS.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/VerticeBFS.kt

$(GLIB)/CicloDigrafo.class: $(GLIB)/CicloDigrafo.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/CicloDigrafo.kt

$(GLIB)/ParticionNiveles.class: $(GLIB)/ParticionNiveles.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/ParticionNiveles.kt

$(GLIB)/Utilidades.class: $(GLIB)/Utilidades.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/Utilidades.kt

$(GLIB)/CFC.class: $(GLIB)/CFC.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/CFC.kt

$(GLIB)/ComponentesConexasDFS.class: $(GLIB)/ComponentesConexasDFS.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/ComponentesConexasDFS.kt

$(GLIB)/VerticeDFSCC.class: $(GLIB)/VerticeDFSCC.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/VerticeDFSCC.kt

$(GLIB)/ElementoCD.class: $(GLIB)/ElementoCD.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/ElementoCD.kt

$(GLIB)/ConjuntosDisjuntos.class: $(GLIB)/ConjuntosDisjuntos.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/ConjuntosDisjuntos.kt

$(GLIB)/ComponentesConexasCD.class: $(GLIB)/ComponentesConexasCD.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/ComponentesConexasCD.kt

$(GLIB)/KruskalAMC.class: $(GLIB)/KruskalAMC.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/KruskalAMC.kt

$(GLIB)/VerticePrim.class: $(GLIB)/VerticePrim.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/VerticePrim.kt

$(GLIB)/PrimAMC.class: $(GLIB)/PrimAMC.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/PrimAMC.kt

$(GLIB)/TablaGrafoDirigido.class: $(GLIB)/TablaGrafoDirigido.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/TablaGrafoDirigido.kt

$(GLIB)/TablaGrafoNoDirigido.class: $(GLIB)/TablaGrafoNoDirigido.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/TablaGrafoNoDirigido.kt

$(GLIB)/CCM_BellmanFord.class: $(GLIB)/CCM_BellmanFord.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/CCM_BellmanFord.kt

$(GLIB)/VerticeDijkstra.class: $(GLIB)/VerticeDijkstra.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/VerticeDijkstra.kt

$(GLIB)/CCM_Dijkstra.class: $(GLIB)/CCM_Dijkstra.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/CCM_Dijkstra.kt

clean:
	rm -rf libGrafoKt.jar META-INF $(GLIB)/*.class
