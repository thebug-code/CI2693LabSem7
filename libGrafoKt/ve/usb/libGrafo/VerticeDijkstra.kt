package ve.usb.libGrafo

public class VerticeDijkstra(var valor: Int, var pre: VerticeDijkstra? = null, var distancia: Double = Double.POSITIVE_INFINITY) : Comparable<VerticeDijkstra> {
    override fun compareTo(other: VerticeDijkstra) : Int {
        when {
            this.distancia > other.distancia -> return 1
            this.distancia < other.distancia -> return -1
            else -> return 0
        }
    }
    
    override fun equals(other: Any?) : Boolean {
        if (other == null || other !is VerticeDijkstra) return false
        return this.distancia == other.distancia && this.valor == other.valor
    }

    override fun toString() : String = "($valor, $distancia, ${pre?.valor})"
}
