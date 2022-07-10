package ve.usb.libGrafo

public class AristaCosto(val x: Int,
			 val y: Int,
			 val costo: Double) : Comparable<AristaCosto>, Arista(x, y) {

    /**
     * Retorna el costo asociado a la arista.
     * Tiempo de complijidad: O(1).
    */
    fun costo() : Double = this.costo

    /**
     * Retorna el contenido de la arista como un string.
     * Tiempo de complijidad: O(1).
    */
    override fun toString() : String = "($v, $u, $costo)"
    
    /**
     * Compara dos aristas respecto a sus costos asociados, retorna 1 si this.obtenerCosto 
     * es mayor que [other].obtenerCosto, o -1 si this.obtenerCosto es menor que [other].obtenerCosto, 
     * o 0 si this.obtenerCosto es igual a [other].obtenerCosto.
     * Tiempo de Complejidad: O(1).
     */
     override fun compareTo(other: AristaCosto): Int {
        when {
            this.costo > other.costo() -> return 1
            this.costo < other.costo() -> return -1
            else -> return 0
        }
    }
   
    /**
     * Retorna false si [other] no es de tipo AristaCosto o si es null, o true si [other] es igual a
     * this.costo
     * Tiempo de la operación: O(1).
    */
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is AristaCosto) {
            return false;
        } else if (this.costo == other.costo) {
            return true
        } else {
            return false
        }
    }
} 
