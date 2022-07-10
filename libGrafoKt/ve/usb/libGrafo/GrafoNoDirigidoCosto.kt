package ve.usb.libGrafo

import java.io.File
import java.util.LinkedList

public class GrafoNoDirigidoCosto: Grafo {
    var numDeVertices : Int = 0
    var numDeLados : Int = 0
    var adjList : Array<LinkedList<AristaCosto>>

    /**
     * Construye un grafo a partir del número de vertices [numDeVertices].
     * Precondición: [numDeVertices] > 0
     * Postcondición: [adjList.size] = [numDeVertices]
     * Tiempo de la operación: O(|V|).
    */
    constructor(numDeVertices: Int) {
        this.numDeVertices = numDeVertices   
        adjList = Array(this.numDeVertices) { LinkedList() }
    }

    /**
     * Construye un grafo a partir de un archivo [nombreArchivo]. El formato es
     * como sigue: la primera línea es el número de vértices y la
     * segunda línea es el número de lados. Las siguientes líneas
     * corresponden a los lados y su costo, con los vértices de un 
     * lado separados por un espacio en blanco. Esta funcion asume
     * que los datos del archivo están correctos, no los verifica.
     * Poscondicion: adjList.size = numDeVertices && para cada adjList[i].size
     * donde 0 <= i < this.numDeVertices se tiene que adjList[i].size == gradoExterior(i)
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
        
        var ladoAux : List<String>
        for (lado in 2 until lines.size) { 
            ladoAux = lines[lado].split(" ")
            this.agregarAristaCosto(AristaCosto(ladoAux[0].toInt(), ladoAux[1].toInt(),
                ladoAux[2].toDouble())) 
        }
    }

    /**
     * Agrega el lado [a] al grafo; en el caso de que lo agrega retorna true, o false si el lado ya
     * ya estaba presente en el grafo (y no lo agrega). Si uno (o ambos) de los vértices de [a] no per-
     * tenecen al grafo entonces se lanza un RuntimeException.
     * Precondición: Los vertices de [a] partenecen al grafo y son distintos.
     * Tiempo de la operación: O(1).
     */ 
     fun agregarAristaCosto(a: AristaCosto) : Boolean {
        // Verificar que los vertices del lado pertenecen al grafo
        if (!estaElVerticeEnElGrafo(a.x) || !estaElVerticeEnElGrafo(a.y)) {
            throw RuntimeException("Uno de los vertices del lado a agregar no esta en el grafo.")
        }
        // Verificar que el lado a agregar esta en el grafo
        if (estaElLadoEnElGrafo(a)) {
            return false
        }
        // Verificar que los vertices de lado no son iguales
        if (a.x == a.y) {
            throw RuntimeException("Los vertices del lado a agregar son iguales")
        }

        // Agregar el lado al grafo
        this.adjList[a.x].add(a)
        this.adjList[a.y].add(AristaCosto(a.y, a.x, a.costo))
        this.numDeLados++
        return true
    }

    /**
     * Retorna el número de lados del grafo.
     * Precondición: true
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
     * Tiempo de la operación: O(1).
    */
    override fun adyacentes(v: Int) : Iterable<AristaCosto> {
        // Verificar que el vertice pertenece al grafo
        if (!estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vertice dado no esta en el grafo.")
        }
        return this.adjList[v]
    }

    /** Retorna los lados adyacentes del lado [l]. 
     *  Precondición: [l] está en el grafo.
     *  Postcondición: no hay lados repetidos en la lista y uno de los vertices 
     *  de cada lado de la lista es [l.x] o [l.y].
     *  Tiempo de la operación: O(|E|+|V|).
    */
    fun ladosAdyacentes(l: AristaCosto) : Iterable<AristaCosto> {
        // Verificar que los vertices del lado pertenecen al grafo
        if (!estaElVerticeEnElGrafo(l.v) || !estaElVerticeEnElGrafo(l.u)) { 
            throw RuntimeException("Uno de los vertices del lado dado no pertenece al grafo.")
        }
        // Verificar que el lado <l> esta en el grafo 
        if (!estaElLadoEnElGrafo(l)) {
            throw RuntimeException("El lado dado no esta en el grafo.")
        }

        var adjLadosL : MutableList<AristaCosto> = mutableListOf()
        for (i in 0 until this.numDeVertices) {
            for (k in this.adjList[i]) {
                if ((l.x == i && l.y != k.y) || (l.x == k.y && l.y != i) ||
                (l.y == i && l.x != k.y) || (l.y == k.y && l.x != i)) {
                        adjLadosL.add(k)
                }
            }
        }
        return adjLadosL
    }


    /**
     * Clase interna con la implementacion concreta de Grafo no dirigido iterator.
    */
    inner class GrafoNoDirigidoIterador(g: GrafoNoDirigidoCosto) : Iterator<AristaCosto> {
        var ll : MutableList<AristaCosto> = mutableListOf()
        init {
            for (i in 0 until g.adjList.size - 1) {
                for (j in g.adjList[i]) {
                    if (j.y >= i) ll.add(AristaCosto(i, j.y, j.costo))
                }
            }
        }
        
        var x = 0
        var actual = ll[0]
        var llIterable = ll.listIterator()
        
        override fun hasNext(): Boolean = llIterable.hasNext()

        override fun next(): AristaCosto {
            if (!llIterable.hasNext()) {     
                throw RuntimeException("No hay más elementos que iterar.")  
            }

            var y = actual
            if (x + 1 < ll.size) {
                actual = ll[x + 1]
                x++
            }
            llIterable.next()
            return y
    }
}

   /** 
     * Retorna un iterador de todos los lados del grafo.
     * Precondición: true.
     * Postcondición: no hay lados repetidos en el iterador.
     * Tiempo de la operación O(|E|).
    */
    override operator fun iterator(): Iterator<AristaCosto> = GrafoNoDirigidoIterador(this) 
    
    /**
     * Retorna el grado de un vertice [v] del grafo.
     * Precondicion: el vertice [v] pertenece al grafo.
     * Postcondicion: el valor retornado es mayor o igual que cero.
     * Tiempo de Complejidad: O(1).
    */
    override fun grado(v: Int) : Int {
        if (!estaElVerticeEnElGrafo(v)){
            throw RuntimeException("El vertice dado no esta en el grafo.")
        }
        return this.adjList[v].size
    }

    /**
     * Retorna un string con la representacion del contenido del grafo.
     * Precondición: true.
     * Postcondición: true.
     * Tiempo de la operación: O(|E|).
    */
    override fun toString(): String {
        var grafoEnString: String = "\n\tGrafo No Dirigido Costo\nVertices -> Adyacentes|Costo\n"
        for ((verticieInicial, aristaIncidente) in adjList.withIndex()){
        
            // Se escribe el identificador del vertice inicial
            grafoEnString += "     |${verticieInicial}| "
        
            // se escribe los vertices finales y el peso de cada lado en el resto de la linea            
            for (arista in aristaIncidente){
                grafoEnString += "-> [${arista.u}|${arista.costo()}] "
            }
        
            // salto de linea
            grafoEnString += "\n"
        }
        return grafoEnString
    }

    /**
     * Indica si un vértice [v] pertenece al grafo.
     * Precondición: true.
     * Tiempo de la operación: O(1).
    */
    fun estaElVerticeEnElGrafo(v: Int) : Boolean = v >= 0 && v < this.numDeVertices

   /**
    * Determina si un lado [l] está en el grafo.
    * Retorna true si [l] está en el grafo, falso en caso contrario.
    * Precondición: El vértice fuente de [l] pertenece al grafo.
    * Tiempo de la operación: O(|gradoExternoDe l.x|).
    */
    private fun estaElLadoEnElGrafo(l: AristaCosto) : Boolean {
        for (i in this.adjList[l.x]) {
            if (i.y == l.y) {
                return true
            }
        }
        return false
    }
}
