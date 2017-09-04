package game

import smart.Brain

private val gravity = -1

/**
 * A square bird.
 */
class Bird(private val game: Game, val brain: Brain) {
    val size = 10
    var isDead = false
        private set // Can not be updated from outside this class

    // xPos and yPos is the top left corner of our bird
    var xPos = 0
        private set
    var yPos = 50
        private set

    var ySpeed = 0
        private set

    init {
        brain.setContext(this, game)
    }

    fun tick() {
        if (isDead) return
        xPos += 1
        yPos += ySpeed
        ySpeed += gravity
        isDead = game.willSurvive(this)
        if (brain.thinksAboutJumping()) ySpeed = 20
    }
}