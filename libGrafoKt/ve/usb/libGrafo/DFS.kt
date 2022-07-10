package ve.usb.libGrafo
import java.util.LinkedList

 /**
  * Implementación del algoritmo DFS.
  * Con la creación de la instancia, se ejecuta el algoritmo DFS
  * desde todos los vértices del grafo
 */
public class DFS(val g: Grafo) {
    private var vertices: Array<VerticeDFS> 
    private var tiempo: Int
    private var treeEdges: LinkedList<Lado> = LinkedList()
    private var backEdges: LinkedList<Lado> = LinkedList()
    private var forwardEdges: LinkedList<Lado> = LinkedList()
    private var crossEdges: LinkedList<Lado> = LinkedList()

    /**
     * Implementacion de algoritmo DFS
     * Precondicion: g es un grafo
     * Postcondicion: Se recorrieron todos los vertices del grafo.
     * Tiempo de complejidad: O(|V|+|E|).
     */
    init {
        vertices = Array(g.obtenerNumeroDeVertices()) { it -> VerticeDFS(it) }
        tiempo = 0

        for (u in vertices) {
            if (u.color == Color.BLANCO) dfsVisit(g, u.valor)
        }
    }

    /**
     * Implementación de algoritmo dfsVisit(g).
     * Precondicion: [g] es un grafo, [u] un vertice desde donde se comienza a recorrer.
     * Postcondicion: Se recorrieron todos los vertices alcanzables desde [u] en el grafo.
     * Tiempo de complejidad: O(|E|).
    */
    private fun dfsVisit(g: Grafo, u: Int) {
        tiempo++
        vertices[u].ti = tiempo
        vertices[u].color = Color.GRIS

        for (ady in g.adyacentes(u)) {
            // Si el color es blanco, pertenece al arbol
            if (vertices[ady.b].color == Color.BLANCO) {
                treeEdges.add(ady)
                vertices[ady.b].pred = vertices[u]
                dfsVisit(g, ady.b)
            } else if (vertices[ady.b].color == Color.GRIS) {
                backEdges.add(ady)
            // Si es negro, es lado Forward o cruzado
            } else {
                if (vertices[ady.a].ti < vertices[ady.b].ti) {
                    forwardEdges.add(ady)
                } else if (vertices[ady.a].ti > vertices[ady.b].ti) {
                    crossEdges.add(ady)
                }
            }
        }

        vertices[u].color = Color.NEGRO
        tiempo++
        vertices[u].tf = tiempo
    }


    /**
     * Retorna el predecesor de un vertice [v]. Si [v] no tiene predecesor se retorna null.
     * Precondicion: [v] pertenece al grafo.
     * Postcondicion: true.
     * Tiempo de complejidad: O(1).
    */
    fun obtenerPredecesor(v: Int): Int? {
        // Verificar que el vertice pertenece al grafo
        if (!estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vertice dado no pertenece al grafo.")
        }

        return vertices[v].pred?.valor
    }

    /**
     * Retorna un par con el tiempo inical y final de un vértice [v] despues de la ejecución de DFS.
     * Precondicion: [v] pertenece al grafo.
     * Postcondicion: true.
     * Tiempo de complejidad: O(1).
    */
    fun obtenerTiempos(v: Int): Pair<Int, Int> {
        // Verificar que el vertice pertenece al grafo
        if (!estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("El vertice dado no esta en el grafo.")
        }

        return Pair(vertices[v].ti, vertices[v].tf)
    }

    /**
     * Indica si hay camino desde el vértice inicial [u] hasta el vértice [v].
     * Si el camino existe retorna true, de lo contrario false.
     * Precondicion: vertices [u], [v] pertenecen al grafo
     * Postcondicion: true.
     * Tiempo de complejidad: O(|E|).
    */
    fun hayCamino(u: Int, v: Int): Boolean {
        // Verificar que los vertices del lado pertenecen al grafo
        if (!estaElVerticeEnElGrafo(u) || !estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("Uno de los vertices no pertenece al grafo.")
        }

        // Se verifica si [u] es alcanzable por [v] en el bosque
        if (descendienteBosque(u, v) || 
            ((g is GrafoNoDirigidoCosto || g is GrafoNoDirigido) && descendienteBosque(v, u))) {
            return true
        }

        var verticesAux: Array<VerticeDFS> = Array(g.obtenerNumeroDeVertices()) { it -> VerticeDFS(it) }
        var camino: LinkedList<Int> = LinkedList()

        // Se ejecuta una version modificada de Dfs para buscar el camino desde u hasta v
        dfsVisitMod(g, u, v, verticesAux, camino)

        if (camino.size != 0) return true
        else return false
    }

   /**
     * Retorna el camino desde el vértice [u] hasta el vértice [v].
     * Precondicion: vertices [u], [v] pertenecen al grafo.
     * Postcondicion: [camino] es igual al camino desde [u] hasta [v].
     * Tiempo de complejidad: O(|V|+|E|).
    */
    fun caminoDesdeHasta(u: Int, v: Int): Iterable<Int> {
        // Verificar que los vertices del lado pertenecen al grafo
        if (!estaElVerticeEnElGrafo(u) || !estaElVerticeEnElGrafo(v)) {
            throw RuntimeException("Uno de los vertices no esta en el grafo.")
        }

        var camino: LinkedList<Int> = LinkedList()

        //Si @v es descendiente de @u en el bosque, obtenemos los predecesores de @v
        if (descendienteBosque(u, v)) {
            camino.addFirst(v)
            var aux = vertices[v].pred?.valor
            while (aux != null) {
                camino.addFirst(aux)
                aux = vertices[aux].pred?.valor
            }
            
        } else if ((g is GrafoNoDirigidoCosto || g is GrafoNoDirigido) && descendienteBosque(v, u)) {
            //Buscar camino equivalente en grafos no dirigidos
            camino.add(u)
            var aux = vertices[u].pred?.valor

            while (aux != null) {
                camino.add(aux)
                aux = vertices[aux].pred?.valor
            }
        }
        
        //Se consiguio camino en el bosque
        if (camino.size != 0) return camino
        
        //En este punto, el camino no se consiguio en el bosque, entonces buscamos el camino con dfsVisit modificado
        var verticesAux: Array<VerticeDFS> = Array(g.obtenerNumeroDeVertices()) { it -> VerticeDFS(it) }
        dfsVisitMod(g, u, v, verticesAux, camino)
        
        if (camino.size == 0) throw RuntimeException("No se consiguio camino")
        
        return camino
    }

   /**
     * Indica si hay hay tree edges, en caso afirmativo retorna true o false en caso contrario.
     * Si no existen ese tipo de lados, entonces se lanza una RuntimeException.
     * Precondicion: True.
     * Postcondicion: True.
     * Tiempo de complejidad: O(1).
    */
    fun hayLadosDeBosque(): Boolean = treeEdges.size != 0
    
    /**
     * Retorna los tree edges .
     * Si no existen ese tipo de lados, entonces se lanza una RuntimeException.
     * Precondicion: True
     * Postcondicion: True
     * Tiempo de complejidad: O(1)
     */
    fun ladosDeBosque(): Iterator<Lado> {
        if (treeEdges.size == 0) {
            throw RuntimeException("No hay lados de Bosque")
        }

        return treeEdges.iterator()
    }

   /**
     * Retorna true si hay forward edges, o false en caso contrario.
     * Precondicion: True.
     * Postcondicion: True.
     * Tiempo de complejidad: O(1).
    */
    fun hayLadosDeIda(): Boolean = forwardEdges.size != 0

    /**
     * Retorna los forward edges. Si no existen ese tipo de lados, entonces se lanza 
     * un RuntimeException.
     * Precondicion: True.
     * Postcondicion: True.
     * Tiempo de complejidad: O(1).
    */
    fun ladosDeIda(): Iterator<Lado> {
        if (forwardEdges.size == 0) {
            throw RuntimeException("No hay forward edges")
        }

        return forwardEdges.iterator()
    }

    /**
     * Retorna true si hay back edges o false en caso contrario.
     * Precondicion: True.
     * Postcondicion: True.
     * Tiempo de complejidad: O(1).
    */
    fun hayLadosDeVuelta(): Boolean = backEdges.size != 0
    

   /**
     * Retorna los back edges. Si no existen ese tipo de lados, entonces se lanza un 
     * RuntimeException.
     * Precondicion: True.
     * Postcondicion: True.
     * Tiempo de complejidad: O(1).
    */
    fun ladosDeVuelta(): Iterator<Lado> {
        if (backEdges.size == 0) {
            throw RuntimeException("No hay back edges")
        }

        return backEdges.iterator()
    }

    
    /**
     * Retorna true si hay cross edges o false en caso contrario.
     * Precondicion: True.
     * Postcondicion: True.
     * Tiempo de complejidad: O(1).
    */
    fun hayLadosCruzados(): Boolean = crossEdges.size != 0
    

    /**
     * Retorna los cross edges del bosque obtenido por DFS. Si no existen ese tipo de lados, entonces se lanza un 
     * RuntimeException.
     * Precondicion: True.
     * Postcondicion: True.
     * Tiempo de complejidad: O(1).
     */
    fun ladosCruzados(): Iterator<Lado> {
        if (crossEdges.size == 0) {
            throw RuntimeException("No hay cross edges")
        }

        return crossEdges.iterator()
    }

    /**
     * Imprime por la salida estándar el depth-first forest.
     * Precondicion: True
     * Postcondicion: True
     * Tiempo de complejudad: O(1)
     */
    fun mostrarBosqueDFS() = println(treeEdges)

   /**
     * Indica si el vertice [v] pertenece al grafo. En caso afirmativo retorna true de lo
     * contrario false.
     * Precondicion: True.
     * Postcondicion: True.
     * Tiempo de complejidad: O(1).
    */
    fun estaElVerticeEnElGrafo(v: Int): Boolean = v >= 0 && v < g.obtenerNumeroDeVertices()

    /**
     * Retorna True si el vertice [u] es ancestro del vertice [v], false en caso contrario.
     * Precondicion: [u] y [v] pertenecen al grafo.
     * Postcondicion: true.
     * Tiempo de complejidad: O(1).
    */
    private fun descendienteBosque(u: Int, v: Int): Boolean = vertices[u].ti < vertices[v].ti && vertices[v].tf < vertices[u].tf

    /**
     * Version modificada de dfsVisit
     * Se busca un vertice [v] partiendo desde [u]. Se va construyendo el camino. Cuando consigue a [u] retorna true
     * Sino lo consigue, retorna falso
     * Precondicion: [g] es un grafo, 
     *               [u], [v] son vertices, 
     *               [verticesAux] estan almacenados los atributos de los vertices, 
     * Postcondicion: [camino] almacena el camino desde [v] hasta [u]
     * Tiempo de complejidad: O(|E|)
    */
    private fun dfsVisitMod(
            g: Grafo,
            u: Int,
            v: Int,
            verticesAux: Array<VerticeDFS>,
            camino: LinkedList<Int>,
    ): Boolean {
        verticesAux[u].color = Color.GRIS

        //Anadir vertice al posible camino
        camino.add(u)

        //Si se consigue el vertice buscado, retornar
        if (u == v) return true
        
        for (ady in g.adyacentes(u)) {
            if (verticesAux[ady.b].color == Color.BLANCO) {
                verticesAux[ady.b].pred = verticesAux[u]
                //Si se consiguio el vertice en la llamada recursiva anterior, retornar
                if (dfsVisitMod(g, ady.b, v, verticesAux, camino)) return true
            }
        }

        verticesAux[u].color = Color.NEGRO
        //En este punto, se busco por todos los adyacentes y no se consiguio, removemos del camino
        camino.removeLast()

        return false
    }
}
