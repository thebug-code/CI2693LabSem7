package ve.usb.libGrafo

public class VerticePrim(var v: Int, var pre: VerticePrim?, var key: Double) : Comparable<VerticePrim> {
    override fun compareTo(other: VerticePrim) : Int {
        when {
            this.key > other.key -> return 1
            this.key < other.key -> return -1
            else -> return 0
        }
    }
    
    override fun equals(other: Any?) : Boolean {
        if (other == null || other !is VerticePrim) return false
        return this.key == other.key && this.v == other.v
    }

    override fun toString() : String = "($v, $key)"
}
