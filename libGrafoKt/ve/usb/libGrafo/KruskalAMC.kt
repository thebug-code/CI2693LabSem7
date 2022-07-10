package ve.usb.libGrafo

/**
 * Determina el árbol mínimo cobertor de un grafo no dirigido usando el algoritmo de Kruskal.
 * Si el grafo de entrada no es conexo, entonces se lanza un RuntineException.
 * El algoritmo de Kruskal debe estar basado en la clase ConjuntosDisjuntos de esta librería.
 */
public class KruskalAMC(val g: GrafoNoDirigidoCosto) {
    private var CD : ConjuntosDisjuntos
    private var aristasGrafo : Array<AristaCosto?>
    private var A : MutableSet<Arista> = mutableSetOf()
    private var cost : Double = 0.0
    
    /**
     * Ejecutar el algoritmo de Kruskal para hallar el MST de [g].
     * Precondición: [g] es un grafo conexo.
     * Postcondición: A es un MST de [g].
     * Tiempo de la operación: O(|E|lg|V|).
     */
    init {
        // Inicializar conjuntos
        CD = ConjuntosDisjuntos(g.obtenerNumeroDeVertices())

        // Obtener lados del grafo
        aristasGrafo = ladosGrafo()

        // Ordenar los lados del grafo en orden creciente por los pesos
        mergeSort(0, aristasGrafo.size - 1)

        // Hallar supuesto MST
        for (lado in aristasGrafo) {
            if (CD.encontrarConjunto(lado!!.x) != CD.encontrarConjunto(lado.y)) {
                A.add(Arista(lado.x,lado.y))
                cost += lado.costo
                CD.union(lado.x, lado.y)
            }
        }

        // Verificar si el grafo es conexo
        if (CD.numConjuntosDisjuntos() != 1) {
            throw RuntimeException("El grafo dado no es conexo.")
        }
    }

    /** 
     * Retorna un objeto iterable que contiene los lados del árbol mínimo cobertor.
     * Precondición: true.
     * Postcondición: true.
     * Tiempo de la operación: O(1).
     */
    fun obtenerLados() : Iterable<Arista> = this.A
    
    /** 
     * Retorna el costo del árbol mínimo cobertor. 
     * Precondición: true.
     * Postcondición: true.
     * Tiempo de la operación: O(1).
     */
    fun obtenerCosto() : Double = this.cost
    
    /** 
     * Ordena los lados del grafo [g] por sus pesos usando el algoritmo merge-sort
     * Postcondición: los lados de grafo [g] estan ordenados en orden creciente por los pesos.
     * Tiempo de la operacion: O(|E|logn|E|).
    */
    private fun mergeSort(l: Int, r: Int) {
        if (l < r) {
            var m : Int = l + (r - l) / 2

            this.mergeSort(l, m)
            this.mergeSort(m + 1, r)
            this.merge(l, m, r)
        }
    }
    
    private fun merge(l: Int, m: Int, r: Int) {
        var n1 : Int = m - l + 1
        var n2 : Int = r - m

        var L : Array<AristaCosto?> = Array(n1) { null }
        var R : Array<AristaCosto?> = Array(n2) { null }

        for (i in 0 until n1) {
            L[i] = this.aristasGrafo[l + i]
        }
        for (j in 0 until n2) {
            R[j] = this.aristasGrafo[m + 1 + j]
        }

        var i : Int = 0
        var j : Int = 0
        var k : Int = l

        while (i < n1 && j < n2) {
            if (L[i]!! <= R[j]!!) {
                this.aristasGrafo[k] = L[i]
                i++
            } else {
                this.aristasGrafo[k] = R[j]
                j++
            }
            k++
        }

        while (i < n1) {
            this.aristasGrafo[k] = L[i]
            i++
            k++
        }

        while (j < n2) {
            this.aristasGrafo[k] = R[j]
            j++
            k++
        }
    }
    
    /** 
     * Retorna un arreglo con los lados del grafo [g].
     * Tiempo de la operacion: O(|E|)
   */
    private fun ladosGrafo() : Array<AristaCosto?> {
        var ladosGrafo : Array<AristaCosto?> = Array(g.obtenerNumeroDeLados()) { null }
        var iter = g.iterator()
        var i = 0

        while (iter.hasNext()) {
            ladosGrafo[i] = iter.next()
            i++
        }

        return ladosGrafo
    }
}
