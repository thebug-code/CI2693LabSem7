package ve.usb.libGrafo
import java.util.LinkedList
import java.util.Queue

/* 
   Implementación del algoritmo BFS. 
   Con la creación de la instancia, se ejecuta el algoritmo BFS
   desde el vértice s
*/
public class BFS(v: Int, val g: Grafo, val s: Int) {
    private val vertices : Array<VerticeBFS>
    
    /**
    * Ejecutar algoritmo BFS
    * Precondición: g es un grafo y s es un vertice que pertenece al grafo.
    * Postcondición: todos los vertices en G alcanzablies desde s fueron visitados.
    * Tiempo de la operación: O(|E|+|V|).
    */
    init {
        this.vertices = Array<VerticeBFS>(g.obtenerNumeroDeVertices()) { it -> VerticeBFS(it, Int.MAX_VALUE, Color.BLANCO, null) }    
        vertices[s] = VerticeBFS(s, 0, Color.GRIS, null)
        val q : Queue<VerticeBFS> = LinkedList<VerticeBFS>()
        q.add(vertices[s])

        var u: VerticeBFS
        while (!q.isEmpty()) {
            u = q.poll()
            
            var vAux : Int
            for (lado in g.adyacentes(u.v)) {
                vAux = lado.elOtroVertice(u.v)
                if (vertices[vAux].color == Color.BLANCO) {
                    vertices[vAux].color = Color.GRIS
                    vertices[vAux].d = vertices[u.v].d + 1
                    vertices[vAux].pred = vertices[u.v]
                    q.add(vertices[vAux])
                }
            }

            vertices[u.v].color = Color.NEGRO
        }
    }

    /**
     * Retorna el predecesor del vertice [v], o null si el vertice no tiene predecesor. 
     * Precondición: el vertice [v] pertenece al grafo.
     * Tiempo de la operación: O(1).
     */
    fun obtenerPredecesor(v: Int) : Int? {
        // Verificar que el vertice pertenece al grafo
        if (!this.estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vertice dado no existe en el grafo.")
        }

        return this.vertices[v].pred?.v
    }

    /**
     * Retorna la distancia del camino obtenido por BFS desde el vertice [v] hasta 
     * el vertice [v], o -1 si [v] no es alcanzable desde [s].
     * Precondición: [v] pertenece al grafo.
     * Postcondición: El valor retornado es la menor distancia posible para ir desde [s] hasta [v].
     * Tiempo de la operación: O(1).
     */
    fun obtenerDistancia(v: Int) : Int {
        // Verificar que el vertice pertenece al grafo
        if (!this.estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vertice dado no existe en el grafo.")
        }
        
        if (!hayCaminoHasta(v)) return -1

        return this.vertices[v].d
    }

    /**
     * Indica si hay camino desde el vértice raiz [s] hasta el vértice [v], en caso afirmativo
     * retorna true de lo contrario retorna false.
     * Precondición: [v] pertenece al grafo.
     * Tiempo de la operación: O(1).
     */ 
    fun hayCaminoHasta(v: Int) : Boolean {
        // Verificar que el vertice pertenece al grafo
        if (!this.estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vertice dado no existe en el grafo.")
        }

        return this.vertices[v].d != Int.MAX_VALUE
    }

    /**
     * Retorna el camino con menos lados, obtenido por BFS, desde el vértice raiz [s] 
     * hasta el un vértice [v].
     * Precondición: [v] pertenece al grafo y ser alcanzable desde [s].
    */ 
    fun caminoHasta(v: Int) : Iterable<Int> {
        // Verificar que el vertice pertenece al grafo
        if (!this.estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vertice dado no existe en el grafo.")
        }
        // Verificar que existe un camino desde s hasta <v>
        if (!this.hayCaminoHasta(v)) {
            throw RuntimeException("El vertice dado no es alcanzable desde el vertice raiz.")
        }
        
        val camino : LinkedList<Int> = LinkedList()
        var x : VerticeBFS = vertices[v]
        camino.addFirst(v)

        while (x.pred!!.v != s) {
            camino.addFirst(x.pred!!.v)
            x = x.pred!!
        }

        camino.addFirst(s)
        return camino

    }

     /** Imprime por la salida estándar el breadth-first tree.
      *  Precondición: true
      *  Postcodición: true
      *  Tiempo de la operación: O(|V|+|E|).
     */
    fun mostrarArbolBFS() {
        var visitados = Array<Boolean>(g.obtenerNumeroDeVertices()) { false }

        visitados[s] = true
        val q : Queue<Int> = LinkedList<Int>()
        q.add(s)

        var u : Int
        while(!q.isEmpty()) {
            u = q.poll()
            print("$u ")

            var i : Iterator<Lado> = g.adyacentes(u).iterator()
            var n : Lado
            while(i.hasNext()) {
                n = i.next()
                if (!visitados[n.elOtroVertice(u)]) {
                    visitados[n.elOtroVertice(u)] = true
                    q.add(n.elOtroVertice(u))
                }
            }
        }
        print("\n")
    }

    /**
     * Determina si un vertice pertenece al grafo, en caso afirmativo retorna true de lo
     * contrario retorna false.
     * Precondición: true.
     * Tiempo de la operación: O(1).
    */
    private fun estaElVerticeEnElGrafo(v: Int): Boolean = v >= 0 && v < g.obtenerNumeroDeVertices()
}
