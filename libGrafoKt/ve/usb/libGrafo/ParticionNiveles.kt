package ve.usb.libGrafo

/**
 * Esta clase al inicializarse obtiene una partición por niveles, de los vértices
 * de un digrafo acíclico (DAG). Si el grafo de entrada es un DAG, el algoritmo encuentra
 * para cada nivel un conjunto de vértices y se indica que no hay ciclo. Si el grafo
 * de entrada no es un DAG, entonces se indica que hay ciclo. 
 */
public class ParticionNiveles(val g: GrafoDirigido) {
    private val particiones : Array<MutableSet<Int>>
    private var gradosInteriores : Array<Int>
    private var hayCiclo : Boolean
    private var nVert : Int
    private var nivel : Int
    
    /**
     * Ejecutar algoritmo de particionado en niveles de un digrafo acíclico (DAG).
     * Tiempo de la operación: O(máx(|V|, |E|)).
     * Postcondición: Para cada v\in V_i se tiene que el camino más largo que termina
     * en v es de logitud i donde 0 <= i <= nivel.
    */
    init {
        particiones = Array<MutableSet<Int>>(g.obtenerNumeroDeVertices()) { mutableSetOf() }   
        gradosInteriores = Array<Int>(g.obtenerNumeroDeVertices()) { it -> g.gradoInterior(it) }
        hayCiclo = false
        nVert = 0
        nivel = 0

        for (u in 0 until g.obtenerNumeroDeVertices()) {
            if (gradosInteriores[u] == 0) {
                particiones[nivel].add(u)
                nVert++
            }
        }

        while(nVert < g.obtenerNumeroDeVertices() && particiones[nivel].isNotEmpty()) {
            for (u in particiones[nivel]) {
                for (v in g.adyacentes(u)) {
                    gradosInteriores[v.b]--  
                    if (gradosInteriores[v.b] == 0) {
                        particiones[nivel+1].add(v.b)
                        nVert++
                    }
                }
            }
            nivel++
        }

        println(nVert)
        hayCiclo = nVert < g.obtenerNumeroDeVertices()
    }

    /** 
     * Retorna las particiones de niveles de los vértices del grafo [g].
     * Precondición: [g] es un DAG.
     * Tiempo de la operación: O(1).
    */
    fun obtenerParticiones() : Array<MutableSet<Int>> {	
        if (this.hayCiclo()) {
            throw RuntimeException("El grafo dado es un DAG.")
        }
        return this.particiones
    }
    
    /**
     * Indica si el grafo [g] tiene un ciclo, en caso afirmativo retorna true, de lo contario
     * retorn false.
     * Tiempo de la operación O(1).
    */
    fun hayCiclo() : Boolean = this.hayCiclo
}
