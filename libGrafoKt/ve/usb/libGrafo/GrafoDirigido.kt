package ve.usb.libGrafo

import java.io.File
import java.util.LinkedList

public class GrafoDirigido : Grafo {
    var numDeVertices : Int = 0
    var numDeLados : Int = 0
    var adjList : Array<LinkedList<Arco>>
    
    /**
     * Construye un grafo a partir del número de vértices [numDeVertices].
     * Precondicion: [numDeVertices] > 0
     * Postcondicion: adjList.size = [numDeVertices]
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
     * Postcondicion: adjList.size = [this.numDeVertices] && para cada adjList[i].size
     * donde 0 <= i < this.numDeVertices se tiene que adjList[i].size == this.gradoExterior(i)
     * Tiempo de la operación: O(|E|+|V|).
     */  
    constructor(nombreArchivo: String)  {
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
            this.agregarArco(Arco(ladoAgregar[0].toInt(), ladoAgregar[1].toInt())) 
        }
    }

    /** 
     * Agrega el lado [a] al grafo dirigido, en caso de que lo agregue retorna true, o false
     * si el lado ya estaba en el grafo (y no lo agrega). Si uno de los vertices del lado no
     * pertenece al grafo entonces se lanza un RuntimeException.
     * Precondición: Los vertices de [a] pertenecen al grafo.
     * Tiempo de la operación: O(1).
     */
    fun agregarArco(a: Arco) : Boolean {
        // Verificar que los vertices del lado pertenecen al grafo
        if (!this.estaElVerticeEnElGrafo(a.inicio) || !this.estaElVerticeEnElGrafo(a.fin)) { 
            throw RuntimeException("Uno de los vertices del lado a agregar no esta en el grafo.")
        } 
        // Verificar que el lado este en el grafo
        if (estaElLadoEnElGrafo(a)) {
            return false
        }

        // Agregar el lado al grafo
        adjList[a.inicio].add(a)
        this.numDeLados++
        return true
    }
    
    /**
     * Retorna el grado del vértice [v].
     * Precondición: [v] pertenece al digrafo.
     * Postcondición: el valor retornado es mayor o igual que cero.
     * Tiempo de la operación: O(1).
    */
    override fun grado(v: Int) : Int {
        // Verificar que el vertice pertenece al grafo
        if (!this.estaElVerticeEnElGrafo(v)) { 
            throw RuntimeException("El vertice dado no esta en el grafo.")  
        }
        return gradoExterior(v) + gradoInterior(v)
    }

    /**
     * Retorna el grado exterior del vértice [v].
     * Precondición: [v] pertenece al digrafo.
     * Postcondición: el valor retornado es mayor o igual que cero.
     * Tiempo de la operación: O(1).
    */
    fun gradoExterior(v: Int) : Int {
        // Verificar que el vertice pertence al grafo
        if (!this.estaElVerticeEnElGrafo(v)) { 
            throw RuntimeException("El vertice dado no esta en el grafo.") 
        }
        return this.adjList[v].size
    }

    /**
     * Retorna el grado interior del vértice [v].
     * Precondición: [v] pertenece al digrafo.
     * Postcondición: el valor retornado es mayor o igual que cero.
     * Tiempo de la operación: O(|E|+|V|).
    */
    fun gradoInterior(v: Int) : Int {
         // Verificar que el vertice pertenece al grafo
        if (!this.estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vertice dado no esta en el grafo.")       
        }

        var count : Int = 0
        for (i in 0 until this.numDeVertices) {
            for (j in this.adjList[i]) {
                if (j.sumidero() == v) {
                    count++
                }
            }
        }
        return count
    }

    /**
     * Retorna el número de lados del grafo.
     * Precondición: true
     * Postcondición: el valor retornado es mayor o igual que cero.
     * Tiempo de la operación: O(1).
    */
    override fun obtenerNumeroDeLados() : Int = this.numDeLados

    /**
     * Retorna el número de vértices del digrafo.
     * Precondición: true.
     * Postcondición: el valor retornado es mayor que cero.
     * Tiempo de Complejidad: O(1).
    */
    override fun obtenerNumeroDeVertices() : Int = this.numDeVertices

    /** 
     * Retorna los lados adyacentes del vértice [v].
     * Precondición: [v] pertenece al digrafo.
     * Postcondición: el vértice fuente de cada cada lado de la lista es [v].
     * Tiempo de la operación: O(1).
     */
    override fun adyacentes(v: Int) : Iterable<Arco> {
         // Verificar que el vertice <v> este en el grafo
        if (!this.estaElVerticeEnElGrafo(v)) { 
            throw RuntimeException("El vertice dado no esta en el grafo.")  
        } 
        return this.adjList[v]
    }

    /** 
     * Retorna los lados adyacentes del lado [l]. 
     * Precondición: [l] pertenece al digrafo.
     * Postcondición: los vértice de cada lado de lista es [l].fuente() o [l].sumidero().
     * Tiempo de la operación: O(|E|+|V|).
     */
    fun ladosAdyacentes(l: Arco) : Iterable<Arco> {
        // Verificar que el lado pertenece al grafo
        if (!this.estaElLadoEnElGrafo(l)) {
            throw RuntimeException("El lado dado no esta en el grafo.")
        }

        var adjLadosL : MutableList<Arco> = mutableListOf()
        for (i in 0 until this.numDeVertices) {
            for (k in this.adjList[i]) {
                if ((i == l.fuente() || i == l.sumidero() || k.sumidero() == l.fuente() || k.sumidero() == l.sumidero()) &&
                    !((i == l.fuente() && k.sumidero() == l.sumidero()))) { 
                        adjLadosL.add(k) 
                }
            }
        }
         return adjLadosL
    }
    
    /**
     * Clase interna con la implementacion concreta de Grafo dirigido iterator.
    */
    inner class GrafoDirigidoIterador(g: GrafoDirigido) : Iterator<Arco> {
        var ll : MutableList<Arco> = mutableListOf()
        init {
            for (i in g.adjList) {
                ll.addAll(i)
            }
        }
        
        var x = 0
        var actual = ll[0]
        var llIterable = ll.listIterator()
        
        override fun hasNext(): Boolean = llIterable.hasNext()

        override fun next(): Arco {
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
     * Retorna un iterador de todos los lados del digrafo.
     * Precondición: true.
     * Poscondición: true.
     * Tiempo de la operación: O(|E|).
    */
    override operator fun iterator(): Iterator<Arco> = GrafoDirigidoIterador(this)
     
    /**
     * Retorna un string con la representacion del contenido del digrafo.
     * Precondición: true.
     * Poscondición: true.
     * Tiempo de la operación: O(|E|+|V|).
    */
    override fun toString(): String {
        var grafoEnString: String = "\n\tGrafo Dirigido\nVertices -> Adyacentes\n"
        for ((verticieInicial, arcosIncidentes) in adjList.withIndex()){
        
            // Se escribe el identificador del vertice inicial
            grafoEnString += "     |${verticieInicial}| "
        
            // se escribe los vertices finales y el peso de cada lado en el resto de la linea            
            for (arco in arcosIncidentes){
                grafoEnString += "-> [${arco.sumidero()}] "
            }
        
            // salto de linea
            grafoEnString += "\n"
        }
        return grafoEnString
    }

    /**
    * Indica si el lado [l] esta en el digrafo, en caso afirmativo retorna true en caso contrario false.
    * Precondición: El vertice fuente de [l] pertenece al digrafo.
    * Tiempo de la operación: O(|E|).
    */
     private fun estaElLadoEnElGrafo(l: Arco) : Boolean {
        // Verificar que el vertice fuente pertenece al grafo
        if (!this.estaElVerticeEnElGrafo(l.fuente())) { 
            throw RuntimeException("El vertice fuente no esta en el digrafo.")
        }
        for(k in this.adjList[l.inicio]) {
            if (k.sumidero() == l.sumidero()) {
                return true
            }
        }
        return false
     }

    /**
     * Indica si el vertice [v] pertenece al grafo, en caso afirmativo retorna true de lo
     * contrario retorna false.
     * Precondición: true.
     * Tiempo de la operación: O(1).
    */
    fun estaElVerticeEnElGrafo(v: Int): Boolean = v >= 0 && v < this.obtenerNumeroDeVertices()
}
