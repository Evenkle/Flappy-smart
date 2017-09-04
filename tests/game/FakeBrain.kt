package game

import smart.Brain

class FakeBrain:Brain {
    var bird:Bird? = null
    var willJump = false

    override fun setContext(body: Bird, game: Game) {
        this.bird = body
    }

    override fun thinksAboutJumping(): Boolean {
        return willJump
    }
}