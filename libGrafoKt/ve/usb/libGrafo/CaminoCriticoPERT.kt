package ve.usb.libGrafo

import kotlin.Double.Companion.NEGATIVE_INFINITY as minusInf
import java.util.LinkedList

/*
 * Implementación del algoritmo para encontrar el
 * caminos crítico de un PERT, representado como un DAG con costos en los lados.
 * Si el digrafo de entrada no es DAG, entonces se lanza una RuntimeException.
*/
public class CaminoCriticoPERT(val g: GrafoDirigidoCosto, val s: Int) {
    private var vertices: Array<VerticeBF> = arrayOf()
    private var vVisitados = BooleanArray(g.obtenerNumeroDeVertices())
    private var topSort: LinkedList<Int> = LinkedList()

    /**
     * Ejecutar algoritmo para hallar caminos críticos del grafo PERT 
     * [g] con vértice fuente fijo [s].
     * Precondición: [g] es es un DAG.
     *               [s] es un vértice perteneciente a [g].
     * Postcondición: v.d = δ'([s], v) ∀ v∈ V ∧ el subgrafo predecesor G_π es un
     * arbol de caminos de costo máximo.
     * Tiempo de la operación: O(|V|+|E|).
     */
    init {
        if (!esDAG()) {
            throw RuntimeException("El grafo dado no es un DAG.")
        }

        // Obtener el orden topologico de los vértices del grafo
        for (v in 0 until g.obtenerNumeroDeVertices()) {
            if (!vVisitados[v]) dfsVisitTopSort(v)
        }

        inicializarFuenteFija(s)

        topSort.forEach { u -> g.adyacentes(u).forEach { v -> relajacion(v) } }
    }

    /**
     * Retorna la distancia del camino crítico desde el vértice fuente
     * fijo [s] hasta el vértice [v].
     * Si [v] no pertenece al grafo o no existe un camino crítico hasta 
     * [v] entonces se lanza un RuntimeException.
     * Precondición: [v] es un vértice perteneciente al grafo.
     *               Existe un camino crítico hasta [v].
     * Postcondición: [costo] = w(p) donde p es el camino de costo máximo.
     */
    fun costo(v: Int) : Double {
        if (!g.estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vértice $v no pertenece al grafo.")
        }
        if (!existeUnCamino(v)) {
            throw RuntimeException("No existe un camino desde $s hasta $v.")
        }

        return vertices[v].d
    }


    /**
     * Retorna los lados del camino de costo crítico hasta el vértice [v].
     * Si [v] no pertenece al grafo o no existe un camino crítico hasta [v]
     * desde [s] entonces se lanza un RuntimeException.
     * Precondición: [v] es un vértice perteneciente al grafo.
     *               Existe un camino crítico hasta [v] desde [s].
     * Postcondición: w[obtenerCaminoCritico] = δ'(s, [v]).
     * Tiempo de la operación: O(|E|).
    */
    fun obtenerCaminoCritico(v: Int) : Iterable<ArcoCosto> {
        if (!g.estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vértice $v no pertenece al grafo.")
        }
        if (!existeUnCamino(v)) {
            throw RuntimeException("No existe un camino desde $s hasta $v.")
        }

        val arcosCamino: LinkedList<ArcoCosto> = LinkedList()
        var y: VerticeBF? = vertices[v]

        while (y?.pred != null) {
            arcosCamino.addFirst(y.pred)
            y = vertices[y.pred!!.x]
        }

        return arcosCamino
    }

    /**
     * Inicializa los atributos v.d, v.pred y [s].d que corresponden a los vértices del grafo
     * [g] y el vértice fuente fijo respectivamente.
     * Precondición: [s] pertenece al grafo.
     * Postcondición: v.pred = null ∀ b∈ V ∧ s.d = 0 ∧ v.d = -∞ para v∈ V - {[s]}
     * Tiempo de la operación: O(V).
     */
    private fun inicializarFuenteFija(s: Int) {
        if (!g.estaElVerticeEnElGrafo(s)) {
            throw RuntimeException("El vértice $s no pertenece al grafo.")
        }

        vertices = Array(g.obtenerNumeroDeVertices()) { VerticeBF(it, minusInf) }
        vertices[s].d = 0.0
    }

    /**
     * Aplica el método de relación al lado [l] del grafo [g]
     * Precondición: (u, v) (l) es un lado perteneciente al grafo.
     * Postcondición: v.d ≥ u.d + w(u, v).
     * Tiempo de la operación: O(1).
    */
    private fun relajacion(l: ArcoCosto) {
        val u: Int = l.fuente()
        val v: Int = l.sumidero()

        if (vertices[v].d < vertices[u].d + l.costo()) {
            vertices[v].d = vertices[u].d + l.costo()
            vertices[v].pred = l
        }
    }

    /**
     * Ordena linealemente los vértices del grafo [g].
     * Precondición: El vertice [u] pertenece al grafo y [g] es un DAG.
     * Tiempo de la operación: O(|E|).
    */
    private fun dfsVisitTopSort(u: Int) {
        this.vVisitados[u] = true

        for (v in g.adyacentes(u)) {
            if (!this.vVisitados[v.b]) dfsVisitTopSort(v.b)
        }

        topSort.addFirst(u)
    }

    /**
     * Indica si hay un camino crítico desde el vértice fuente fijo [s] hasta [v].
     * Si [v] no pertenece al grafo entonces se lanza un RuntimeException.
     * Precondición: [v] es un vértice perteneciente al grafo.
     * Postcondición: [existeUnCamino] = true si hay un camino crítico
     * hasta [v] desde [s].
     *                [existeUnCamino] = false en caso contrario.
     */
    fun existeUnCamino(v: Int) : Boolean {
        if (!g.estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vértice $v no pertenece al grafo.")
        }
        return vertices[v].d != minusInf
    }

    /**
     * Indica si el grafo [g] es un DAG. En caso afirmativo retorna true, en caso contrario
     * retorna false.
     * Precondición: [g] es un grafo dirigido.
     * Postcondición: [esDAG()] = true si [g] es un DAG.
     *                [esDAG()] = false en caso contrario.
     * Tiempo de la operación: O(|E|+|V|).
     */
    private fun esDAG(): Boolean {
        try {
            DFS(g).ladosDeVuelta()
            return false
        } catch (e: RuntimeException) {
            return true
        }
    }
}
