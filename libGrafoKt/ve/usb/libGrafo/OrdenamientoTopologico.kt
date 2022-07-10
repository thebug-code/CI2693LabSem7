package ve.usb.libGrafo
import java.util.LinkedList

/**
 * Determina el orden topológico de un DAG. El ordenamiento topológico
 * se determina en el constructor de la clase.  
*/
public class OrdenamientoTopologico(val g: GrafoDirigido) {
    private var vertices : Array<VerticeTp>
    private var l : LinkedList<Int>
        
    /**
     * Ejecutar algoritmo de ordenamiento topologico para un grafo dirigido acíclico.
     * Precondición: [g] es un grafo dirigido.
     * Postcondicón: Para cada arco (u,v) que pertenece a this.l se tiene que el vertice u es pre
     * decesor de v.
     * Tiempo de la Operación: O(|E|+|V|).
    */
    init {
        vertices = Array(g.obtenerNumeroDeVertices()) { vi -> VerticeTp(vi, Color.BLANCO) }
        l = LinkedList()

        for (vertice in this.vertices) {
            if (vertice.color == Color.BLANCO) dfsVisitTp(vertice.v)
        }
    }

    /** 
     * Indica si el grafo [g] es un digrafo acíclico (DAG), en caso afirmativo retorna true, de
     * lo contrario retorna false.
     * Precondición: [g] es un grafo dirigido.
     * Tiempo de la Operación: O(1).
    */
    fun esDAG() : Boolean {
        var dfsG = DFS(g)

        try {
            dfsG.ladosDeVuelta()
            return false
        } catch (e: RuntimeException) {
            return true
        }
    }

    /**
     * Retorna el ordenamiento topológico del grafo [g]. Si el grafo [g] no es DAG, entonces
     * se lanza una RuntimeException.
     * Precondición: [g] es un grafo directo.
     * Tiempo de la Operación: O(|V|+|E|).
    */
    fun obtenerOrdenTopologico() : Iterable<Int> {
        //Verificar que el grafo dado es DAG
        if (!this.esDAG()) {
            throw RuntimeException("El grafo dado no es un DAG.")
        }

        return this.l
    }
    
    /**
     * Versión modificada de dfsVisit(G) que agrega al frente de una lista anlazada
     * una vértice una vez que este es coloreado de negro.
     * Tiempo de la operación: O(|E|).
    */
    private fun dfsVisitTp(u: Int) {
        vertices[u].color = Color.GRIS

        for (v in g.adyacentes(u)) {
            if (vertices[v.b].color == Color.BLANCO) {
                dfsVisitTp(v.b)
            }
        }

        vertices[u].color = Color.NEGRO
        l.addFirst(u)
    }
}
