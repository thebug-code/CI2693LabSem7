package ve.usb.libGrafo

/*
 * Determina las componentes conexas de un grafo no dirigido usando
 * conjuntos disjuntos, representados como árboles. 
 * La componentes conexas se determinan cuando 
 * se crea un nuevo objeto de esta clase.
*/
public class ComponentesConexasCD(val g: GrafoNoDirigido) {
    var cc : ConjuntosDisjuntos
    var elementosReps : Array<ElementoCD?>
    
    /**
     * Determinar las componentes conexas del grafo [g] usando conjuntos disjuntos.
     * Tiempo de la operación: O(Vα(E))
     */
    init {
        cc = ConjuntosDisjuntos(g.obtenerNumeroDeVertices())
        for (lado in g) {
            if (cc.encontrarConjunto(lado.v) != cc.encontrarConjunto(lado.u)) {
                cc.union(lado.v, lado.u)
            }
        }
        // Agregar identificador a las componentes conexas
        var j : Int = 0
        elementosReps = Array(this.nCC()) { null }
        for (i in cc.elementosCD) {
            if (i.p == i.v) {
                i.label = j++
                elementosReps[i.label] = i
            }
        }
    }

    /**
     * Indica si los vertices [v] y [u] están en la misma componente conexa, en caso
     * afirmativo retorna true de lo contrario false. Si alguno de los vertices no perte-
     * necen al grafo se lanza un RuntimeException.
     * Precondición: [u] y [v] deben pertenecer al grafo.
     * Postcondición: 
     * Tiempo de la operación: O(1).
     */
    fun mismaComponente(v: Int, u: Int) : Boolean {	
        // Verificar que los vertices pertenecen al grafo
        if (!g.estaElVerticeEnElGrafo(v) || !g.estaElVerticeEnElGrafo(u)) {
            throw RuntimeException("Uno de los vertices dado no pertenece al grafo.")
        }
        return this.cc.encontrarConjunto(v) == this.cc.encontrarConjunto(u)
    }

    /**
     * Retorna el número de componentes conexas del grafo [g].
     * Precondición: true.
     * Postcondición: true.
     * Tiempo de la Operación: O(1).
    */
    fun nCC() : Int = cc.numConjuntosDisjuntos()

     /**
      * Retorna el identificador de la componente conexa donde está contenido el
      * vértice [v]. Si el vértice no pertenece al grafo se lanza un RuntimeException.
      * Precondición: [v] debe pertencer al grafo.
      * Postcondición: el valor retornado es un número en el intervalo [0, this.nCC()-1].
      * Tiempo de la operación: O(1).
      */
    fun obtenerComponente(v: Int) : Int {
        // Verificar que el vértice pertenece al grafo
        if (!g.estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vertice dado no pertenece al grafo.")
        }
        return cc.elementosCD[cc.encontrarConjunto(v)].label 
    }

     /** 
      * Retorna el número de vertices que conforman la componente conexa con identificador [compID].
      * Si el identificador dado no esta asociado a algun componente conexa del grafo se lanza un 
      * RuntimeException.
      * Precondición: [compId] debe esta asociado a alguna CC del grafo [g].
      * Postcondición: el valor retornado en un número en el intervalo [0, this.nCC()].
      * Tiempo de la operación: O(1).
      */
    fun numVerticesDeLaComponente(compID: Int) : Int {
        // Verificar que el identificar esta asociado a alguna CC
        if (compID < 0 || compID >= this.nCC()) {
            throw RuntimeException("El identificador dado no esta asociado a alguna CC del grafo.")
        }
        return this.elementosReps[compID]!!.n
    }

}
