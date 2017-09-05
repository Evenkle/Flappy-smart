package game

class Pillar(xPos: Int) {
    // This is the right-hand side of the gap
    var xPos = xPos
    // This is the center of the gap
    val gapY = ((HEIGHT / 4) + Math.random() * HEIGHT / 2).toInt()
    // This is the total height of the gap
    val gapSize = 100
    val width = 50
}