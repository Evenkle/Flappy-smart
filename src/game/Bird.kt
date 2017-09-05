package game

import smart.Brain

private val gravity = -1

/**
 * A square bird.
 */
class Bird(private val game: Game, val brain: Brain) : Comparable<Bird> {
    val size = 10
    var isDead = false
        private set // Can not be updated from outside this class

    // xPos and yPos is the bottom right corner of our bird
    var xPos = 0
        private set
    var yPos = 50
        private set

    // Negative => falling
    var ySpeed = 0
        private set

    val fitness: Int
        get() = xPos - Math.abs(yPos - game.getNextPillar().gapY)

    init {
        brain.setContext(this, game)
    }

    fun tick() {
        if (isDead) return
        xPos += 1
        ySpeed += gravity
        yPos += ySpeed
        if (yPos > HEIGHT) yPos = HEIGHT
        isDead = game.checkKilled(this)
        if (brain.thinksAboutJumping()) ySpeed = 20
    }

    override fun compareTo(other: Bird): Int {
        return other.fitness - this.fitness
    }

    override fun toString(): String {
        return "Bird at ($xPos, $yPos) fitness $fitness"
    }
}