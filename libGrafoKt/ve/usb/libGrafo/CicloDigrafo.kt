package ve.usb.libGrafo
import java.util.LinkedList
import java.util.Stack

/* 
   Determina la existencia o no de un ciclo en un digrafo.
   En el momento de la creación de un objeto de este tipo,
   se ejecuta una versión de  DFS que determina la existencia 
   o no de un ciclo. Es decir, el algoritmo de detección de
   ciclo e ejecuta en el constructor. 
*/
public class CicloDigrafo(val g: GrafoDirigido) {
    private val visitado : Array<Color>
    private var s : Stack<Int>
    private var raizCiclo : Int?
    
    /**
     * Ejecutar el algoritmo de detección de ciclos de un grafo.
     * Precondición: [g] es un grafo dirigido.
     * Tiempo de la operación: O(|E|+|V|).
    */
    init {
        visitado = Array(g.obtenerNumeroDeVertices()) { Color.BLANCO }
        s = Stack()
        raizCiclo = null

        for (vertice in 0 until this.visitado.size) {
            if (visitado[vertice] == Color.BLANCO && this.raizCiclo == null) {
                s = Stack()
                s.push(vertice)
                visitado[vertice] = Color.GRIS
                dfsVisitCiclo()
            }
        }
    }

    /**
    * Indica si el digrafo [g] tiene un ciclo, en caso aformativo retorna true, o false
    * de lo contrario.
    * Precondición: [g] es un grafo dirigido.
    * Tiempo de la operación: O(1).
    */
    fun existeUnCiclo() : Boolean {
        if (this.raizCiclo != null) return true
        
        return false
    }

    /** 
     * Si el grafo [g] tiene un ciclo, entonces retorna la secuencia de vértices del ciclo,
     * y en caso contrario lanza un RuntimeException.
     * Precondición: [g] tiene un ciclo.
     * Tiempo de la Operación: 
    */
    fun cicloEncontrado() : Iterable<Int> {
        // Verificar que el grafo tiene al menos un ciclo
        if (raizCiclo == null) {
            throw RuntimeException("El grafo dado no tiene un ciclo.")
        }

        var ciclo : LinkedList<Int> = LinkedList()
        var sTemp : Stack<Int> = Stack()
        sTemp.push(s.pop())

        while(sTemp.peek() != this.raizCiclo) sTemp.push(s.pop())
       
        while(!sTemp.isEmpty()) ciclo.add(sTemp.pop())
        
        ciclo.add(this.raizCiclo!!)

        return ciclo
    }
    
    /**
     * Versión modificada de dfsVisit(g) que halla un ciclo en el grafo [g].
     * Tiempo de la Operación: O(|E|).
    */
    private fun dfsVisitCiclo() {
        for (v in g.adyacentes(s.peek())) {
            // Si el vertice v.b esta en la pila => hay un ciclo
            if (visitado[v.b] == Color.GRIS) {
                raizCiclo = v.b
                break
            } else if (visitado[v.b] == Color.BLANCO && this.raizCiclo == null) {
                s.push(v.b)
                visitado[v.b] = Color.GRIS
                dfsVisitCiclo()
            }
        }

        if (this.raizCiclo == null) visitado[s.pop()] = Color.NEGRO
    }
} 
