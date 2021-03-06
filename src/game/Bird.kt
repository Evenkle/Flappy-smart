package game

import smart.Brain

/**
 * A square bird.
 */
class Bird(private val game: Game, val brain: Brain) : Comparable<Bird> {
    val size = SIZE
    var isDead = false
        private set // Can not be updated from outside this class

    // xPos and yPos is the bottom right corner of our bird
    var xPos = 0
        private set
    var yPos = 50
        private set

    // Negative => falling
    var ySpeed = 20
        private set

    val fitness: Int
        get() = xPos - (game.getNextPillar().xPos - xPos)
        //get() = xPos - Math.abs(yPos + (size/2) - game.getNextPillar().gapY)

    val score: Int
        get() = xPos / game.pillarInterval

    init {
        brain.setContext(this, game)
    }

    fun tick() {
        if (isDead) return
        xPos += 2
        ySpeed += gravity
        yPos += ySpeed
        if (yPos > Game.HEIGHT) yPos = Game.HEIGHT
        isDead = game.checkKilled(this)
        if (brain.thinksAboutJumping()) ySpeed = 12
    }

    override fun compareTo(other: Bird): Int {
        return other.fitness - this.fitness
    }

    override fun toString(): String {
        return "Bird at ($xPos, $yPos) fitness $fitness"
    }

    companion object {
        private val gravity = -1
        val SIZE = 30
    }
}