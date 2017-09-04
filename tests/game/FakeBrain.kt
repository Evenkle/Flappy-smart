package game

import smart.Brain

class FakeBrain:Brain {
    var bird:Bird? = null
    var willJump = false

    override fun setBody(body: Bird?) {
        this.bird = body
    }

    override fun thinksAboutJumping(): Boolean {
        return willJump
    }
}