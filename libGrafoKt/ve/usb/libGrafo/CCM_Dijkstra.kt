package ve.usb.libGrafo

import java.util.LinkedList
import java.util.PriorityQueue

/**
 * Implementación del algoritmo de Dijkstra para encontrar los
 * caminos de costo mínimo desde un vértice fuente s fijo.
 */
public class CCM_Dijkstra(val g: GrafoDirigidoCosto, val s: Int) {

    private var vertices = Array(g.obtenerNumeroDeVertices()) { VerticeDijkstra(it) }
    private var Q: PriorityQueue<VerticeDijkstra> = PriorityQueue(g.obtenerNumeroDeVertices())

    /**
     * Implementacion de algoritmo de Dijkstra para obtener caminos de costo minimo desde un vertice
     * fuente [s] fijo en un grafo [g] 
     * Precondicion: [s] pertenece al grafo
     * Postcondicion: para cada vertice [v], [v.distancia] almacena el costo del camino de costo 
     * minimo desde [s] hasta [v]
     * Tiempo de la operacion: O(|E|log|V|)
     */
    init {
        // Precondicion
        if (!estaElVerticeEnElGrafo(s)) {
            throw RuntimeException("Vertice fuente no pertenece al grafo")
        }

        //Inicializar fuente fija
        vertices[s].distancia = 0.0

        // Inicializar cola de prioridad
        Q.addAll(vertices)

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

    /**
     * Retorna true si hay un camino desde el vertice [s] hasta el vértice [v]. 
     * false en caso contrario 
     * Precondicion: [v] y [s] pertenecen al grafo [g] 
     * Postcondicion: True 
     * Tiempo de la operación: O(1)
     */
    fun existeUnCamino(v: Int): Boolean {
        if (!estaElVerticeEnElGrafo(s)) {
            throw RuntimeException("Vertice no pertenece al grafo")
        }

        return Double.POSITIVE_INFINITY != vertices[v].distancia
    }

    /**
     * Retorna la distancia del camino de costo mínimo desde [s] hasta el vértice [v].
     * Precondicion: [v] pertenece al grafo
     * Postcondicion: true
     */
    fun costo(v: Int): Double {
        if (!estaElVerticeEnElGrafo(s)) {
            throw RuntimeException("Vertice fuente no pertenece al grafo")
        }

        return vertices[v].distancia
    }

    /**
     * Retorna los arcos del camino de costo mínimo hasta [v].
     * Precondicion: [v] pertenece al grafo
     * Postcondicion: [camino] es un camino simple hasta [v]
     * Tiempo de la operacion: O(|E|)
     */
    fun obtenerCaminoDeCostoMinimo(v: Int): Iterable<ArcoCosto> {
        var vertice = vertices[v]
        var camino: LinkedList<ArcoCosto> = LinkedList()

        if (!estaElVerticeEnElGrafo(s)) {
            throw RuntimeException("Vertice no pertenece al grafo")
        }

        while (vertice.pre != null) {
            var arco =
                    ArcoCosto(
                            vertice.pre!!.valor,
                            vertice.valor,
                            vertice.distancia - vertice.pre!!.distancia
                    )
            camino.push(arco)
            vertice = vertices[vertice.pre!!.valor]
        }
        return camino
    }

    /**
     * Relaja el lado [lado], implicitamente actualiza la cola [Q]
     * Precondicion: [lado] pertenece al grafo
     * Postcondicion: True
     * Tiempo de la operacion: O(|V|log|V|)
     */
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

    /**
     * Indica si el vertice [v] pertenece al grafo, en caso afirmativo retorna true de lo
     * contrario retorna false.
     * Precondicion: true.
     * Tiempo de la operación: O(1).
     */
    private fun estaElVerticeEnElGrafo(v: Int): Boolean = v >= 0 && v < g.obtenerNumeroDeVertices()
}
