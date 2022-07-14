import ve.usb.libGrafo.*

fun main(args: Array<String>) {
    val grafo = GrafoDirigidoCosto(args[0])
    //val bf = CCM_BellmanFord(grafo,0)
    //println(bf.costo(2))
    //println(bf.costo(1))
    //println(bf.costo(4))

    //println(bf.obtenerCicloNegativo())

    val ccm = CCM_DAG(grafo,1)
    println("Camino de costo mínimo hasta 5.")
    println(ccm.obtenerCaminoDeCostoMinimo(5))
}
