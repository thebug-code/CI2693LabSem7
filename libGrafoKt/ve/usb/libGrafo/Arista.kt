package ve.usb.libGrafo

public open class Arista(val v: Int, val u: Int) : Lado(v, u) {

    /**
     * Retorna el contenido del arco como string.
     * Tiempo de complijidad: O(1).
    */
    override fun toString() : String = "($v, $u)"
} 
