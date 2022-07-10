package ve.usb.libGrafo

/**
 * Implementación de las estructuras de datos para conjuntos disjuntos.
 * Los conjuntos disjuntos son representado como árboles.
 * El constructor recibe como entrada en número elementos que van a conformar los cojuntos disjuntos. 
 * Los elementos de los conjuntos disjuntos están identificados en el intervalo [0 .. n-1]. 
 * Cuando se ejecuta el constructor, se crean n conjuntos disjuntos iniciales, es decir,
 * se debe ejecutar make-set(i) para todo i en el intervalo [0 .. n-1]. 
*/
public class ConjuntosDisjuntos(val n: Int) {
    var elementosCD: Array<ElementoCD>
    var numCC : Int
    
    /**
     * Ejecutar make-set(i), i pertenece al intervalo [0, n).
    */
    init {
        numCC = n
        elementosCD = Array(n) { it -> ElementoCD(it, 0, it) }
    }

     /**
      * Realiza la unión de las dos componentes conexas donde están contenidos los elementos [v] y [u].
      * En caso de realizar tal acción retorna true, de lo contrario ([u] y [v] están en la misma CC) 
      * retorna false. Si alguno de los elementos no pertenece al grafo se lanza un RuntimeException.
      * Precondición: [v] y [u] pertenecen a algún conjunto.
      * Tiempo de la operación: O(1).
     */
    fun union(v: Int, u: Int) : Boolean {
        // Verificar que los elementos v y u pertenecen a algún CD
        if (!this.estaEnAlgunC(v) || !this.estaEnAlgunC(u)) {
            throw RuntimeException("Uno de los elementos dado no pertenece a algún conjunto.")
        }

        // Buscar rep de [u] y [v]
        var repV : Int = this.encontrarConjunto(v)
        var repU : Int = this.encontrarConjunto(u)

        // Verificar que [u] y [v] están en el mismo CD
        if (repV == repU) {
            return false
        }

        // Unir los conjuntos
        this.numCC--
        if (this.elementosCD[repV].rank > this.elementosCD[repU].rank) {
            this.elementosCD[repU].p = repV
            this.elementosCD[repV].n += this.elementosCD[repU].n
        } else {
            this.elementosCD[repV].p = repU
            this.elementosCD[repU].n += this.elementosCD[repV].n 
            if (this.elementosCD[repV].rank == this.elementosCD[repU].rank) {
                this.elementosCD[repU].rank++
            }
        }

        return true
    }

     /**
      * Retorna el elemento que representa al conjunto disjunto donde esta contenido el elemento [v].
      * Si el elemento [v] no pertenece a algún conjunto se lanza un RuntimeException.
      * Precondición: [v] pertenece a algún conjunto.
     */
    fun encontrarConjunto(v: Int) : Int {
        // Verificar que el elemento pertenece a algún conjuto
        if (!this.estaEnAlgunC(v)) {
            throw RuntimeException("El elemento dado no pertenece a algún conjunto disjunto.")
        }

        if (this.elementosCD[v] != this.elementosCD[this.elementosCD[v].p]) {
            this.elementosCD[v].p = encontrarConjunto(this.elementosCD[v].p) 
        }

        return this.elementosCD[v].p
    }

    /**
     * Retorna el número de conjuntos disjuntos que tiene la actual estructura.
     * Precondición: true.
     * Postcondicion: true.
     * Tiempo de la operación: O(1).
    */
    fun numConjuntosDisjuntos() : Int = this.numCC
    
    /**
     * Indica si el elemento [u] pertenece a algún conjunto, en caso afirmativo retorna
     * true, de lo contrario false.
     * Precondición: true
     * Tiempo de la operación: O(1).
    */
    private fun estaEnAlgunC(u: Int): Boolean = u >= 0 && u < n
}
