package ve.usb.libGrafo

import java.util.PriorityQueue
import java.util.LinkedList


/*
 Implementación del algoritmo de Dijkstra para encontrar los
 caminos de costo mínimo desde un vértice fuente s fijo. La
 implementación debe usar como cola de prioridad un min-heap o un fibonacci heap.
 Si el grafo de entrada tiene un lado con costo negativo,  entonces se retorna una RuntimeException.
 Si el vértice s no pertenece al grafo, entonces se retorna una RuntimeException.
*/
public class CCM_Dijkstra(val g: GrafoDirigidoCosto, val s: Int) {

    private var vertices: Array<VerticeDijkstra> = Array(g.obtenerNumeroDeVertices()) { VerticeDijkstra(it) }
    private var Q: PriorityQueue<VerticeDijkstra> = PriorityQueue(g.obtenerNumeroDeVertices())


    init {
        Q.addAll(vertices)
        inicializarFuenteFija()

        while (!Q.isEmpty()) {
            var u = Q.poll().valor
            for (ady in g.adyacentes(u)) {
                if (ady.costo() < 0) {
                    throw RuntimeException("Error. Lado negativo")
                }
                relajacion(ady)
            }
        }
    }

    // Retorna cierto si hay un camino desde s hasta el vértice v.
    // Si el vértice v no existe, se retorna un RuntimeException.
    fun existeUnCamino(v: Int): Boolean {
        if (!estaElVerticeEnElGrafo(s)) {
            throw RuntimeException("Vertice no pertenece al grafo")
        }

        return Double.POSITIVE_INFINITY != vertices[v].distancia
    }

    // Retorna la distancia del camino de costo mínimo desde s hasta el vértice v.
    // Si el vértice v no existe, se retorna un RuntimeException.
    fun costo(v: Int): Double {
        if (!estaElVerticeEnElGrafo(s)) {
            throw RuntimeException("Vertice fuente no pertenece al grafo")
        }

        return vertices[v].distancia
    }

    
    // Retorna los arcos del camino de costo mínimo hasta v.
    // Si el vértice v no existe, se retorna un RuntimeException.
    fun obtenerCaminoDeCostoMinimo(v: Int) : Iterable<ArcoCosto> { 
        var vertice = vertices[v]
        var camino: LinkedList<ArcoCosto> = LinkedList()

        if (!estaElVerticeEnElGrafo(s)) {
            throw RuntimeException("Vertice no pertenece al grafo")
        }
        
        while(vertice.pre != null) {
           var arco = ArcoCosto(vertice.pre!!.valor, vertice.valor, vertice.distancia - vertice.pre!!.distancia)
           camino.push(arco) 
           vertice = vertices[vertice.pre!!.valor]
        }
        return camino
    }
    
    private fun inicializarFuenteFija() {
        if (!estaElVerticeEnElGrafo(s)) {
            throw RuntimeException("Vertice fuente no pertenece al grafo")
        }

        vertices[s].distancia = 0.0
    }

    private fun relajacion(lado: ArcoCosto) {
        var fuente = lado.fuente()
        var fin = lado.sumidero()

        if (vertices[fin].distancia > vertices[fuente].distancia + lado.costo()) {
            this.Q.remove(vertices[fin])
            vertices[fin].distancia = vertices[fuente].distancia + lado.costo()
            vertices[fin].pre = vertices[fuente]
            this.Q.add(vertices[fin])


        }
    }

    private fun estaElVerticeEnElGrafo(v: Int): Boolean = v >= 0 && v < g.obtenerNumeroDeVertices()
}
