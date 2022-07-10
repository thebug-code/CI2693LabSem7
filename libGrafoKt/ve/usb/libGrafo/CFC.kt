package ve.usb.libGrafo
import java.util.Stack
import java.util.LinkedList

/*
  Obtiene las componentes fuertementes conexas de un grafo 
  La componentes fuertementes conexas se determinan cuando 
  se crea un nuevo objeto de esta clase, es decir, en el constructor.
*/
public class CFC(val g: GrafoDirigido) {
    private var vVisitados : Array<Boolean>
    private var identificadores : Array<Int>
    private var c : Array<MutableSet<Int>>
    private var s : Stack<Int>
    private var digrafoInverso : GrafoDirigido
    private var nCFC : Int
    
    /**
     * Ejecutar algoritmo para obtener las componentes fuertementes conexas de un digrafo.
     * Precondición: [g] es un grafo dirigido.
     * Postcondición: cada Ci perteneciente a this.c es un subconjunto máximo de vertices donde
     * para todo par de vertices u,v contenidos en ellos se cumple que u es alcanzable desde v.
    */
    init {
        // Inicializar arreglos para los identificadores de las CFC y para almacenarlos
        this.identificadores = Array(g.obtenerNumeroDeVertices()) { -1 }
        this.c = Array(g.obtenerNumeroDeVertices()) { mutableSetOf() }

        // Inicializar pila y arreglo para el primer DFS
        s = Stack()
        vVisitados = Array(g.obtenerNumeroDeVertices()) { false } 

        // Obtener el orden topologico de los vertices del grafo
        for (v in 0 until g.obtenerNumeroDeVertices()) {
            if (!this.vVisitados[v]) ordenDescVertices(v)
        }

        // Obtener digrafo inverso
        digrafoInverso = digrafoInverso(g)

        // Inicializar arreglo para el segundo DFS
        vVisitados = Array(g.obtenerNumeroDeVertices()) { false }
        
        var vAux : Int
        this.nCFC = 0
        while (this.s.isNotEmpty()) {
            vAux = this.s.pop()
            if (!this.vVisitados[vAux]) {
                dfsCFC(digrafoInverso, vAux)
                nCFC++
            }
        }
    }
    
    /**
     * Indica si dos vertices están en las misma CFC, en caso afirmativo retorna true de lo
     * contrario false o lanza un RuntimeException si los vertices [v] o [u] no pertenecen al
     * grafo.
     * Precondición: [v] y [u] pertenecen al grafo.
     * Tiempo de la operación: O(1).
    */
    fun estanEnLaMismaCFC(v: Int, u: Int) : Boolean {
        if (!g.estaElVerticeEnElGrafo(v) || !g.estaElVerticeEnElGrafo(u)) {
            throw RuntimeException("Uno de los vertices dado no pertenece al grafo.")
        }

        return this.identificadores[v] == this.identificadores[u]
    }
    
    /**
     * Retorna el número de CFC de [g].
     * Tiempo de la Operación: O(1).
    */
    fun numeroDeCFC() : Int = this.nCFC
    
    /**
     * Retorna el identificador de la CFC donde está contenido el vertice [v] o lanza un 
     * RuntimeException si el vertice no pertenece al grafo.
     * Precondición: [v] pertenece al grafo.
     * Postcondición: el valor retornado esta en el intervalo [0, numeroDeCFC()-1].
     * Tiempo de la Operación: O(1).
    */
    fun obtenerIdentificadorCFC(v: Int) : Int {
        if (!g.estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vertice dado no pertenece al grafo.")
        }

        return this.identificadores[v]
    }
   
    /**
     * Retorna un obteto iterable que contiene las CFCs de [g]. El orden de las
     * CFCs corresponde al que se indica en el metodo obtenerIdentificadorCFC().
     * Tiempo de la operación: O(|numeroDeCFC|).
    */
    fun  obtenerCFC() : Iterable<MutableSet<Int>> {
        var r = LinkedList<MutableSet<Int>>()  	

        for (i in 0 until this.numeroDeCFC()) r.add(this.c[i])
        
        return r
    }

    /**
     * Retorna el grafo componente asociado a las CFCs de [g]; el identificador de los vertices
     * del grafo componente está asociado con el orden de las CFCs del objeto iterable que se obtiene
     * con el método obtenerCFC().
     * Tiempo de la operación: O(|E|+|V|).
    */
    fun obtenerGrafoComponente() : GrafoDirigido {
        this.vVisitados = Array(g.obtenerNumeroDeVertices()) { false }        	
        var sccDag : GrafoDirigido = GrafoDirigido(this.nCFC)
        
        for (i in 0 until this.numeroDeCFC()) {
            if (!this.vVisitados[i]) this.dfsCondensarCFC(i, sccDag)
        }

        return sccDag
    }

    /**
     * Implementación de dfsVisit(g) modificado que llena en orden topologico la pila this.s
     * Precondición: El vertice [u] pertenece al grafo.
     * Tiempo de la operación: O(|E|).
    */
    private fun ordenDescVertices(u: Int) {
        this.vVisitados[u] = true
        
        for (v in g.adyacentes(u)) {
            if (!this.vVisitados[v.b]) ordenDescVertices(v.b)
        }

        this.s.push(u)
    }
  
    /**
     * Implementacion de dfsVisit(g) modificado que recorre el grafo inverso de [g].
     * Tiempo de la operación: O(|E|).
    */
    private fun dfsCFC(gInv: GrafoDirigido, u: Int) {
        this.vVisitados[u] = true
        this.c[nCFC].add(u)
        this.identificadores[u] = this.nCFC

        for (v in gInv.adyacentes(u)) {
            if (!this.vVisitados[v.b]) dfsCFC(gInv, v.b)
        }
    }
   
   /**
    * Implementación de dfsVisit(g) modificado que condensa las CFCs de [g] en un solo vertice
    * y los agregas al gradoDirigido [sccDag].
    * Tiempo de la operación: O(|E|).
   */
    private fun dfsCondensarCFC(u: Int, sccDag: GrafoDirigido) {
        this.vVisitados[u] = true

        for (v in g.adyacentes(u)) {
            if (this.identificadores[u] != this.identificadores[v.b]) {
                sccDag.agregarArco(Arco(this.identificadores[u], this.identificadores[v.b])) 
            }
    
            if (!this.vVisitados[v.b]) dfsCondensarCFC(v.b, sccDag)
        }
    }
}
