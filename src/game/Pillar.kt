package game

class Pillar(xPos: Int) {
    // This is the right-hand side of the gap
    var xPos = xPos
    // This is the center of the gap
    var gapY = ((Game.HEIGHT / 4) + Math.random() * Game.HEIGHT / 2).toInt()
        private set

    fun recycle(newX:Int) {
        xPos = newX
        gapY = ((Game.HEIGHT / 4) + Math.random() * Game.HEIGHT / 2).toInt()
    }

    override fun toString(): String {
        return "pillar at $xPos, $gapY"
    }

    companion object {
        // This is the total height of the gap
        val gapSize = 150
        val width = 50
    }
}