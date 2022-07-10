package ve.usb.libGrafo

import java.io.File
import java.util.LinkedList

public class GrafoNoDirigido: Grafo {
    var numDeVertices : Int = 0
    var numDeLados : Int = 0
    var adjList : Array<LinkedList<Arista>>

    /**
     * Construye un grafo a partir del número de vertices [numDeVertices].
     * Precondición: numDeVertices > 0
     * Postcondición: adjList.size = numDeVertices
     * Tiempo de la operación: O(V).
    */
    constructor(numDeVertices: Int) {
        this.numDeVertices = numDeVertices
        this.adjList = Array(this.numDeVertices) { LinkedList() }
    }

    /**
     * Construye un grafo a partir de un archivo [nombreArchivo]. El formato es 
     * como sigue: la primera línea es el número de vértices y la
     * segunda línea es el número de lados. Las siguientes líneas
     * corresponden a los lados, con los vértices de un lado separados
     * por un espacio en blanco. Esta funcion asume que los datos del 
     * archivo están correctos, no los verifica.
     * Postcondición: adjList.size = numDeVertices && para cada adjList[i].size
     * donde 0 <= i < this.numDeVertices se tiene que adjList[i].size == gradoExterior(i).
     * Tiempo de la operación: O(|E|+|V|).
    */ 
    constructor(nombreArchivo: String) {
        val file = File(nombreArchivo)
        // Verificar que el archivo existe
        if (!file.exists()) {
            throw RuntimeException("El archivo dado no existe.")
        }

        val lines : List<String> = file.readLines()
        this.numDeVertices = lines[0].toInt()
        this.adjList = Array(this.numDeVertices) { LinkedList() }
        
        var ladoAgregar : List<String>
        for (lado in 2 until lines.size) { 
            ladoAgregar = lines[lado].split(" ")
            this.agregarArista(Arista(ladoAgregar[0].toInt(), ladoAgregar[1].toInt())) 
        }
    }

    /** 
     * Agrega el lado [a] al grafo; en caso de que lo agregue retorna true, o false si el lado ya
     * estaba presente en el grafo (y no lo agrega). Si uno de los vértices de [l] no pertenece al
     * grafo entonces se lanza un RuntimeException
     * Precondición: Los vértices de [a] pertenecen al grafo y son distintos.
     * Tiempo de la operación: O(1).
     */
    fun agregarArista(a: Arista) : Boolean {
        // Verificar que los vertices del lado pertenecen al grafo
        if (!this.estaElVerticeEnElGrafo(a.v) || !this.estaElVerticeEnElGrafo(a.u)) {
            throw RuntimeException("Uno de los vertices del lado a agregar no esta en el grafo.")
        } 
        // Verificar que los vertices de lado no son iguales
        if (a.v == a.u ) {
            throw RuntimeException("Los vertices del lado a agregar son iguales")
        }
        // Verificar que el lado a agregar este en el grafo
        if (estaElLadoEnElGrafo(a)) {
            return false
        }
    
        // Agregar el lado al grafo
        adjList[a.v].add(a)
        adjList[a.u].add(Arista(a.u, a.v))
        this.numDeLados++
        return true
    }

    /**
     * Retorna el número de lados del grafo.
     * Precondición: true.
     * Postcondición: el valor retornado es mayor o igual que cero.
     * Tiempo de la operación: O(1).
    */
    override fun obtenerNumeroDeLados() : Int = this.numDeLados

    /**
     * Retorna el número de vértices del grafo.
     * Precondición: true.
     * Postcondición: el valor retornado es mayor que cero.
     * Tiempo de la operación: O(1).
    */
    override fun obtenerNumeroDeVertices() : Int = this.numDeVertices

    /**
     * Retorna los lados adyacentes del vértice [v] del grafo.
     * Precondición: el vértice [v] pertenece al grafo.
     * Postcondición: uno de los vertices de cada lado de la lista es [v].
     * Tiempo de la Operación: O(1).
     */
    override fun adyacentes(v: Int) : Iterable<Arista> {
        // Verificar que el vertice pertenece al grafo
        if (!this.estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vertice dado no esta en el grafo.")
        }
        return this.adjList[v]
    }

    /** 
     * Retorna los lados adyacentes del lado [l]. 
     * Precondición: [l] pertenece al grafo.
     * Postcondición: no hay lados repetidos en el Iterable y uno de los vertices. 
     * de cada lado es [l].v o [l].u
     * Tiempo de la operación: O(|E|+|V|).
     */ 
    fun ladosAdyacentes(l: Arista) : Iterable<Arista> {
        // Verificar que los vertices del lado <l> pertenecen al grafo
        if (!this.estaElVerticeEnElGrafo(l.v) || !this.estaElVerticeEnElGrafo(l.u)) { 
            throw RuntimeException("Uno de los vertices del lado dado no pertenece al grafo.")
        } 
        // Verificar que el lado <l> esta en el grafo
        if (!estaElLadoEnElGrafo(l))
            throw RuntimeException("El lado dado no esta en el grafo.")

        var adjLadosL : MutableList<Arista> = mutableListOf()
        for (i in 0 until this.numDeVertices) {
            for (k in this.adjList[i]) {
                if ((l.v == i && l.u != k.u) || (l.v == k.u && l.u != i) || 
                    (l.u == i && l.v != k.u) || (l.u == k.u && l.v != i)) {
                        if (k.u < i) {
                            continue
                        } else {
                            adjLadosL.add(k)
                        }
                }
            }
        }
        return adjLadosL
    }
   
    /**
     * Clase interna con la implementacion concreta de Grafo no dirigido iterator.
    */
    inner class GrafoNoDirigidoIterador(g: GrafoNoDirigido) : Iterator<Arista> {
        var ll : MutableList<Arista> = mutableListOf()

        init {
            for (i in 0 until g.adjList.size - 1) {
                for (j in g.adjList[i]) {
                    if (j.u >= i) ll.add(Arista(i, j.u))
                }
            }
        }
        
        var x = 0
        var actual = ll[0]
        var llIterable = ll.listIterator()
        
        override fun hasNext(): Boolean = llIterable.hasNext()

        override fun next(): Arista {
            if (!llIterable.hasNext()) {     
                throw RuntimeException("No hay más elementos que iterar.")  
            }

            var y = actual
            if (x + 1 < ll.size) {
                actual = ll[x+1]
		        x++
            }

            llIterable.next()
            return y
        }
    }   

    /** 
     * Retorna un iterador de todos los lados del grafo.
     * Precondición: true.
     * Poscondición: no hay lados repetidos en el iterador.
     * Tiempo de la operación: O(|E|).
    */
    override operator fun iterator(): Iterator<Arista> = GrafoNoDirigidoIterador(this)

    /**
     * Retorna el grado del vertice [v].
     * Precondición: [v] pertenece al grafo.
     * Postcondición: el valor retornado es mayor o igual que cero.
     * Tiempo de la operación: O(1).
    */
    override fun grado(v: Int) : Int {
        if (!this.estaElVerticeEnElGrafo(v)){
            throw RuntimeException("El vertice dado no esta en el grafo.")
        }
        return this.adjList[v].size
    }

    /**
     * Retorna un string con la representacion del contenido del grafo.
     * Postcondición: true.
     * Tiempo de la operación: O(|E|).
    */
    override fun toString(): String {
        var grafoEnString: String = "\n\tGrafo No Dirigido\nVertices -> Adyacentes\n"
        for ((verticieInicial, aristaIncidente) in adjList.withIndex()){
        
            // Se escribe el identificador del vertice inicial
            grafoEnString += "     |${verticieInicial}| "
        
            // se escribe los vertices finales y el peso de cada lado en el resto de la linea            
            for (arista in aristaIncidente){
                grafoEnString += "-> [${arista.u}] "
            }
        
            // salto de linea
            grafoEnString += "\n"
        }
        return grafoEnString
    }
    
   /**
    * Indica si el lado [l] esta en el grafo, en caso afirmativo retorna true de lo
    * contrario false.
    * Precondición: El vertice fuente de [l] pertenece al grafo.
    * Tiempo de la operación: O(E) 
    */
    private fun estaElLadoEnElGrafo(l: Arista) : Boolean {
        for (i in this.adjList[l.v]) { 
            if (i.u == l.u) {
                return true 
            }
        }
        return false
    }

    /**
     * Determina si un vertice pertenece al grafo, en caso afirmativo retorna true de lo
     * contrario retorna false.
     * Precondición: true.
     * Tiempo de la operación: O(1).
    */
    fun estaElVerticeEnElGrafo(v: Int): Boolean = v >= 0 && v < obtenerNumeroDeVertices()
}
