package ve.usb.libGrafo

import java.io.File

/**
 * Esta clase recibe como entrada un archivo que contiene un grafo no
 * dirigido, en donde los vértices están identificados con un String, en 
 * lugar de un entero. Se crea un objeto que tiene una estructura de datos
 * que permite asociar cada nombre de vértice, con un índice que es un número entero. 
 * Si |V| = n, entonces a cada vértice se le asigna un índice que es número entero
 * en el intervalo [0, n-1]. También se crea una estructura que dado un índice de un 
 * vértice, permite obtener el nombre del vértice. Por último, cuando 
 * se llama al constructor, Se crea un grafo no dirigido en el que los
 * vértices están identificados con los índices de los vértices del grafo. 
*/
public class TablaGrafoNoDirigido(val nombreArchivo: String) {
    private var verticeToString : HashMap<Int, String>
    private var stringToVertice : HashMap<String, Int>
    private var g : GrafoNoDirigido
    
    init {
        val file = File(nombreArchivo)
        if (!file.exists()) {
            throw RuntimeException("El archivo dado no existe.")
        }
        
        val lines : List<String> = file.readLines()
        var nombresVertices : List<String> = lines[2].split('\t')
        verticeToString = HashMap(lines[0].toInt())
        stringToVertice = HashMap(lines[0].toInt())

        for (i in 0 until lines[0].toInt()) {
            verticeToString.put(i, nombresVertices[i])
            stringToVertice.put(nombresVertices[i], i)
        }
        
        g = GrafoNoDirigido(lines[0].toInt())
        var a : List<String>
        for (lado in 3 until lines[1].toInt() + 3) {
            a = lines[lado].split('\t')
            g.agregarArista(Arista(stringToVertice[a[0]]!!, stringToVertice[a[1]]!!))
        }
    }

    /**
     * Indica si el indice [v] de un vértice pertenece al digrafo. En caso
     * afirmativo retorna true, de lo contrario false.
     * Tiempo de la operación: O(1).
    */
    fun contieneVertice(v: Int) : Boolean = v >= 0 && v < g.obtenerNumeroDeVertices()

     /**
      * Retorna el índice asociado al vertice de nombre [nombre]. Si no existe ningun vértice
      * asociado con ese nombre se lanza un RuntimeException.
      * Precondición: nombre tiene un vértice del grafo asociado.
      * Tiempo de la operación: O(1).
     */
    fun indiceVertice(nombre: String) : Int {
        // Verificar si el nombre tiene un vertice asociado
        if (stringToVertice[nombre] == null) {
            throw RuntimeException("No existe un vertice asociado con el nombre dado.")
        }
        
        return this.stringToVertice[nombre]!!
    }


    /**
     * Retorna el nombre asociado del vertice con índice [v]. Si [v] no corresponde a un índice
     * asociado con un vertice del grafo entonces se lanza un RuntimeException.
     * Precondición: [v] corresponde a un indice con un vértice del grafo asociado.
     * Tiempo de la operación: O(1).
    */
    fun nombreVertice(v: Int) : String {
        // Verificar si el indice dado tiene un nombre asociado
        if (v < 0 || v >= g.obtenerNumeroDeVertices()) {
            throw RuntimeException("No existe un nombre asociado con el indice dado.")
        }

        return this.verticeToString[v]!!
    }

    /**
     * Retorna el grafo no dirigido asociado al grafo con vértices con nombre, indicado en el archivo
     * de entrada.
     * Tiempo de la operación: O(1).
    */
    fun obtenerGrafoNoDirigido() : GrafoNoDirigido = this.g
    
}
