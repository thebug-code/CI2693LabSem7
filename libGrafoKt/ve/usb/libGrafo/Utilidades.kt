package ve.usb.libGrafo

/**
 * Retorna el digrafo inverso del digrafo @g.
 * Precondición: el grafo dado debe ser un grafo dirigido.
 * Tiempo de la operación: O(|V|+|E|)
*/
fun digrafoInverso(g: GrafoDirigido) : GrafoDirigido {
    var grafoInverso = GrafoDirigido(g.obtenerNumeroDeVertices())
    
    for (u in 0 until g.obtenerNumeroDeVertices()) {
        for (v in g.adyacentes(u)) {
            grafoInverso.agregarArco(Arco(v.b, u))
        }
    }
    return grafoInverso
}
