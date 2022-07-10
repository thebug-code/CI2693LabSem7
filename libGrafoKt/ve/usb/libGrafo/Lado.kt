package ve.usb.libGrafo
import kotlin.random.Random

abstract class Lado(val a: Int, val b: Int) {

    /**
     * Retorna cualquiera de los vertices del lado.
     * @return cualquiera de los vertices del lado
     * Precondicion: true
     * Postcondicion: true.
     * Tiempo de Complejidad: O(1)
    */
    fun cualquieraDeLosVertices() : Int {
        val r = if (Random.nextBoolean()) this.a else this.b
        return r 
    }
    
    /**
     * Retorna el vertice que es diferente al vertice dado.
     * @w vertice al cual se le quiere comparar.
     * @return si @w == a entonces retorna b, de lo contrario si @w == b  entonces retorna a 
     * y si w no es igual a a ni a b, entonces se lanza una RuntimeExpception.
     * Precondicion: true
     * Poscondicion: true
     * Tiempo de Complejidad: O(1)
    */
    fun elOtroVertice(w: Int) : Int {
        when (w) {
            this.a -> return this.b
            this.b -> return this.a
            else -> {
                throw RuntimeException("El vértice dado no esta en el lado.")
            }
        }
    }
}
