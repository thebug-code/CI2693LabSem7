package ve.usb.libGrafo

public open class Arco(val inicio: Int, val fin: Int) : Lado(inicio, fin) {

    /**
     * Retorna el vertice inicial del arco.
     * Tiempo de complejidad: O(1)
    */
    fun fuente() : Int {
        return this.inicio
    }

    /**
     * Retorna el vertice final del arco.
     * Tiempo de complejidad: O(1).
    */
    fun sumidero() : Int {
        return this.fin
    }

    /**
     * Retorna el contedido del arco como string.
     * Tiempo de complijidad: O(1).
    */
    override fun toString() : String {
        return "($inicio, $fin)"
     }
} 
