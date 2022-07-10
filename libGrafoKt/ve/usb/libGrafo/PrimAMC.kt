package ve.usb.libGrafo

import java.util.PriorityQueue
import java.util.LinkedList


/**
 * Determina el árbol mínimo cobertor de un grafo no dirigido usando el algoritmo de Prim. Si el
 * grafo de entrada no es conexo, entonces se lanza un RuntineException.
 */
public class PrimAMC(val g: GrafoNoDirigidoCosto) {
    private var verticesGrafo: Array<VerticePrim>
    private var Q: PriorityQueue<VerticePrim>
    private var MST: LinkedList<Arista> = LinkedList()
    private val infinito = Double.POSITIVE_INFINITY
    private var costoTotal =  0.0

    /**
     * Ejecutar el algoritmo de Prim para hallar el MST de [g]
     * Precondición: [g] es un grafo conexo.
     * Postcondición: MST es un MST de [g].
     * Tiempo de la operación Prim: O(|E|lg|V|).
     * Tiempo de la operacion Chequear precondicion: O(|V|)
     * Tiempo de la operacion: O(|E|lg|V| + |V|)
     */
    init {
        verticesGrafo =
                Array(g.obtenerNumeroDeVertices()) { it ->
                    VerticePrim(it, null, infinito)
                }
        verticesGrafo[0].key = 0.0


        // Inicializar cola de prioridad
        Q = PriorityQueue(g.obtenerNumeroDeVertices())
        verticesGrafo.forEach { it -> Q.add(it) }

        //Hallar MST
        var u: VerticePrim
        while (!Q.isEmpty()) {
            u = Q.poll()
            for (v in g.adyacentes(u.v)) {
                if (Q.contains(verticesGrafo[v.y]) && v.costo < verticesGrafo[v.y].key) {
                    
                    if (verticesGrafo[v.y].key == infinito){
                        this.costoTotal += v.costo()
                    } else {

                        this.costoTotal -= verticesGrafo[v.y].key
                        this.costoTotal += v.costo()
                    }

                    //Actualizar cola
                    Q.remove(verticesGrafo[v.y])
                    verticesGrafo[v.y].pre = verticesGrafo[u.v]
                    verticesGrafo[v.y].key = v.costo()
                    Q.add(verticesGrafo[v.y])
                }
            }
        }

        //Verificar si el grafo es conexo
        if (!this.esConexo()) {
            throw RuntimeException("El grafo no es conexo")
        }
    }

    /** 
     * Retorna un objeto iterable que contiene los lados del árbol mínimo cobertor.
     * Precondición: true.
     * Postcondición: true.
     * Tiempo de la operación: O(1).
     */
    fun obtenerLados() : Iterable<Arista> = this.MST
        
    /** 
     * Retorna el costo del árbol mínimo cobertor. 
     * Precondición: true.
     * Postcondición: true.
     * Tiempo de la operación: O(1).
     */
    fun obtenerCosto() : Double = this.costoTotal

    /** 
     * Retorna true si el grafo [g] es conexo.
     * Se anaden tambien los lados a [MST] 
     * Precondición: true.
     * Postcondición: true.
     * Tiempo de la operación: O(|V|).
     */
    private fun esConexo() : Boolean {
        for (vertice in verticesGrafo) {
            if (vertice.key == infinito) return false
            if (vertice.pre == null) continue
            
            this.MST.add(AristaCosto(vertice.pre!!.v, vertice.v, vertice.key))
        }

        return true
    }
}
