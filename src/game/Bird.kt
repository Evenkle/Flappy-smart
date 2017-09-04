package game

import smart.Brain

private val gravity = -1

class Bird(private val game:Game, val brain: Brain) {
    val size = 10
    var isDead = false
        private set
    var xPos = 0
        private set // Can not be updated from outside this class
    var yPos = 50
        private set
    var ySpeed = 0
        private set

    init {
        brain.body = this
    }

    internal fun jump() {
        if (isDead) return
        ySpeed = 20
    }

    fun tick() {
        if (isDead) return
        // if (brain.thinksAboutJumping()) jump()
        xPos += 1
        yPos += ySpeed
        ySpeed += gravity
        isDead = game.willSurvive(this)
    }
}