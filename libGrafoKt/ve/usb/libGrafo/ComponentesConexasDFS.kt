package ve.usb.libGrafo

/*
  Determina las componentes conexas de un grafo no dirigido usando DFS. 
  La componentes conexas se determinan cuando 
  se crea un nuevo objeto de esta clase.
*/
public class ComponentesConexasDFS(val g: GrafoNoDirigido) {
    var contCC : Int
    var vertices: Array<VerticeDFSCC>
    var tiempo: Int

    init {
        vertices = Array(g.obtenerNumeroDeVertices()) { it -> VerticeDFSCC(it, 0, 0, Color.BLANCO, -1, null) }  
        tiempo = 0
        contCC = -1

        for (v in 0 until g.obtenerNumeroDeVertices()) {
            if (vertices[v].color == Color.BLANCO) {
                contCC++
                dfsVisitCC(v)
            }
        }
    }
    

    /**
     * Indica si los vertices [v] y [u] están en la misma componente conexa, en caso
     * afirmativo retorna true, de lo contrario false o lanza un RuntimeException si
     * alguno de los vertices no pertenece al grafo.
     * Precondicion: [u] y [y] pertenecen al grafo.
     * Tiempo de la Operación: O(1).
    */
    fun estanMismaComponente(v: Int, u: Int) : Boolean {
        // Verificar que los vertices pertenecen al grafo
        if (!this.estaElVerticeEnElGrafo(v) || !this.estaElVerticeEnElGrafo(u)) {
            throw RuntimeException("Uno de los vértices dado no pertenece al grafo.")
        }

        return this.vertices[v].cc == this.vertices[u].cc
    }

    /**
     * Retorna el número de componentes conexas del grafo [g].
     * Precondicion: true.
     * Postcondicion: true.
     * Tiempo de la operación: O(1).
    */
    fun nCC() : Int = this.contCC + 1


    /**
     * Retorna el identificador de la componente conexa donde está contenido el verti-
     * ce [v] o lanza un RuntimeException si [v] no pertenece al grafo.
     * Precondición: [v] pertenece al grafo.
     * Postcondición: el valor retornado es un número en el intervalo [0, this.nCC()-1].
     * Tiempo de la Operación: O(1).
    */
    fun obtenerComponente(v: Int) : Int {
        // Verificar que el vertice pertenecen al grafo
        if (!this.estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vertice dado no pertenece al grafo.")
        }

        return this.vertices[v].cc
    }
    

    /**
     * Retorna el número de vértices que conforman la componente conexa con identificador [compID]. Si
     * el identificador no pertenece a alguna CC de [g] se lanza un RuntimeException.
     * Precondición: [compID] esta asociado a alguna componente conexa del grafo.
     * Postcondicion: El valor retornado es un número mayor que cero.
     * Tiempo de la Operación: O(|V|).
    */
    fun numVerticesDeLaComponente(compID: Int) : Int {
        // Verificar que el identificador pertenece a una CC
        if (compID < 0 || compID >= this.nCC()) {
            throw RuntimeException("El identificador dado no esta asociado a alguna CC.")
        }
        
        var count : Int = 0
        for (v in this.vertices) {
            if (v.cc == compID) count++
        }

        return count
    }
    
    /**
     * dfsVisit(g) modificado para identificar las componentes conexas de una grafo no dirigido. 
     * Tiempo de la operación: O(|E|).
    */
    private fun dfsVisitCC(v: Int) {
        this.tiempo++
        this.vertices[v].cc = this.contCC
        this.vertices[v].ti = this.tiempo
        this.vertices[v].color = Color.GRIS

        for (u in g.adyacentes(v)) {
            if (this.vertices[u.b].color == Color.BLANCO) {
                this.vertices[u.b].pred = this.vertices[v]
                dfsVisitCC(u.b)
            }
        }

        this.vertices[v].color = Color.NEGRO
        this.tiempo++
        this.vertices[v].tf = this.tiempo
    }

    /**
     * Indica si el vertice [v] pertenece al grafo, en caso afirmativo retorna true de lo
     * contrario retorna false.
     * Precondicion: true.
     * Tiempo de la operación: O(1).
    */
    private fun estaElVerticeEnElGrafo(v: Int): Boolean = v >= 0 && v < g.obtenerNumeroDeVertices()
}
