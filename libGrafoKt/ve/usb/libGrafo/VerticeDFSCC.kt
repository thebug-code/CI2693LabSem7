package ve.usb.libGrafo

public class VerticeDFSCC(
    var valor: Int,
    var ti: Int = 0,
    var tf: Int = 0,
    var color: Color,
    var cc: Int = -1,
    var pred: VerticeDFSCC?
)
