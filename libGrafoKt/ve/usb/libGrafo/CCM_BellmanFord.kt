package ve.usb.libGrafo

import kotlin.Double.Companion.POSITIVE_INFINITY as inf
import java.util.LinkedList

/*
 * Implementación del Algoritmo de Bellman-Ford para encontrar los
 * caminos de costo mínimo desde un vértice fuente s fijo.
 * Si el vértice s no existe en el grafo se retorna un RuntimeException.
 */
public class CCM_BellmanFord(val g: GrafoDirigidoCosto, val s: Int) {
    val V: Int = g.obtenerNumeroDeVertices()
    var vertices: Array<VerticeBF> = arrayOf()
    var cicloNegativo: LinkedList<ArcoCosto> = LinkedList()
    

    /**
     * Ejecutar algoritmo de Bellman-Ford para encontrar los caminos de costo
     * mínimo desde el vértice fuente fijo [s] hasta v∈ V - {[s]}.
     * Precondición: [g] es un grafo dirigido con costos en los lados.
     *               [s] pertenece al grafo.
     * Postcondición: v.d = δ([s], v) ∀ v alcanzable desde el vertice [s].
     * Tiempo de la operación: O(|V|*|E|).
     */
    init {
        inicializarFuenteFija(s)

        for (i in 0 until V - 1) {
            for (lado in g.iterator()) relajacion(lado)
        }

        /**
         * Verificar si el grafo tiene un ciclo negativo.
         * En caso afirmativo se recupera uno de los vértices del ciclo.
         */
        var v: Int = -1
        for (l in g.iterator()) {
            if (vertices[l.sumidero()].d > vertices[l.fuente()].d + l.costo()) {
                v = l.sumidero()
                break
            }
        }

        /**
         * Si hay ciclo negativo se "recuperan" los arcos del ciclo, recorriendo
         * los predecesores del vértice v hasta volver al mismo vertice v.
         */
        if (v != -1) {
            var y: VerticeBF = vertices[v]

            while(true) { 
                cicloNegativo.addFirst(y.pred)
                if (vertices[y.pred!!.fuente()].v == v && cicloNegativo.size > 1) break 
                y = vertices[y.pred!!.x]
            }
        }
    }

    /**
     * Indica si hay un ciclo con pesos negativos alcanzable desde el vértice fuente 
     * fijo [s]. En caso afirmativo retorna true, de lo contrario false.
     * Precondición: true.
     * Postcondición: [tieneCicloNegativo] = si hay un ciclo con pesos negativos alcanzable 
     * desde [s].
     *               [tieneCicloNegativo] = false en caso contrario.
     * Tiempo de la operación: O(1).
     */
    fun tieneCicloNegativo() : Boolean = cicloNegativo.size == 0

    /**
     * Retorna los arcos del ciclo negativo alcanzable desde el vértice fuente fijo [s],
     * con la forma <u, v>, <v, w>, ... ,<y, x>, <x, u>
     * Si 
     * Precondición: existe un ciclo negativo alcanzablde desde el vértice fuente fijo [s].
     * Postcondición: δ([s], k_i) = -∞, dónde k_i corresponde a los vértices del ciclo negativo, 
     * (vértices fuentes de de cada lado de [obtenerCicloNegativo]).
     * Tiempo de la operación: O(1).
     */
    fun obtenerCicloNegativo() : Iterable<ArcoCosto> = cicloNegativo

    /**
     * Indica si hay un camino desde el vértice fuente fijo [s] hasta [v].
     * Si [v] no pertenece al grafo entonces se lanza un RuntimeException.
     * Precondición: [v] es un vértice perteneciente al grafo.
     * Postcondición: [existeUnCamino] = true si hay un camino de costo
     * mínimo hasta [v] desde [s].
     *                [existeUnCamino] = false en caso contrario.
     */
    fun existeUnCamino(v: Int) : Boolean { 
        if (!g.estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vértice $v no pertenece al grafo.")
        }
        return vertices[v].d != inf
    }

    /**
     * Retorna la distancia del camino de costo mínimo desde el vértice fuente
     * fijo [s] hasta el vértice [v].    
     * Si [v] no pertenece al grafo o no existe un camino de costo mínimo
     * hasta [v] entonces se lanza un RuntimeException.
     * Precondición: [v] es un vértice perteneciente al grafo.
     *               Existe un camino de costo mínimo hasta [v].
     * Postcondición: [costo] = w(p) donde p es el camino de costo mínimo.
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
    * Retorna los lados del camino de costo mínimo hasta el vértice [v].
    * Si [v] no pertenece al grafo o no existe un camino de costo mínimo
    * hasta [v] desde [s] entonces se lanza un RuntimeException.
    * Precondición: [v] es un vértice perteneciente al grafo.
    *               Existe un camino de costo mínimo hasta [v] desde [s].
    * Postcondición: w[obtenerCaminoDeCostoMinimo] = δ(s, [v]).
    * Tiempo de la operación: O(|E|).
    */
    fun obtenerCaminoDeCostoMinimo(v: Int) : Iterable<ArcoCosto> {
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
     * Postcondición: v.pred = null ∀ b∈ V ∧ s.d = 0 ∧ v.d = ∞ para v∈ V - {[s]} 
     * Tiempo de la operación: O(V).
     */
    private fun inicializarFuenteFija(s: Int) {
        if (!g.estaElVerticeEnElGrafo(s)) {
            throw RuntimeException("El vértice $s no pertenece al grafo.")
        }

        vertices = Array(V) { VerticeBF(it) }
        vertices[s].d = 0.0
    }
    
    /**
     * Aplica el método de relación al lado [l] del grafo [g]
     * Precondición: (u, v) (l) es un lado perteneciente al grafo.
     * Postcondición: v.d ≤ u.d + w(u, v) ∧ v.d = δ(s, v). 
     * Tiempo de la operación: O(1).
     */
    private fun relajacion(l: ArcoCosto) {
        val u: Int = l.fuente()
        val v: Int = l.sumidero()

        if (vertices[v].d > vertices[u].d + l.costo()) {
            vertices[v].d = vertices[u].d + l.costo()
            vertices[v].pred = l
        }
    }
}
