package ve.usb.libGrafo

public class VerticeDFS(
    var valor: Int,
    var ti: Int = 0,
    var tf: Int = 0,
    var color: Color = Color.BLANCO,
    var pred: VerticeDFS? = null
)
