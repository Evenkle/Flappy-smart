package game

class Pillar(val xPos:Int) {
    val gapY = ((HEIGHT / 4) + Math.random() * HEIGHT / 2).toInt()
    val gapSize = 100
    val width = 50
}