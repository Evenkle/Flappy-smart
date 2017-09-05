package game

class Game {
    var currentX = -50
        private set

    val pillars = List(PILLAR_COUNT, { Pillar((it + 2) * WIDTH / PILLAR_COUNT) })

    fun reset() {
        currentX = -50
        pillars.forEachIndexed({ index, pillar -> pillar.recycle((index + 2) * WIDTH / PILLAR_COUNT) })
    }

    fun checkKilled(bird: Bird): Boolean {
        if (bird.yPos <= 0) {
            println("$bird got grounded")
            return true
        }
        if (bird.yPos + bird.size >= HEIGHT) {
            println("$bird hit the roof")
            return true
        }
        // Check if we crashed into any pillar whatsoever
        val pillar = getNextPillar()
        val afterStart = bird.xPos > pillar.xPos - pillar.width
        val beforeEnd = bird.xPos - bird.size < pillar.xPos
        var i = pillar.gapY - bird.yPos - (bird.size / 2)
        val outsideOpening = Math.abs(i) > (pillar.gapSize - bird.size) / 2
        if (afterStart && beforeEnd && outsideOpening) {
            println("$bird crashed into $pillar")
            return true
        }
        return false
    }

    fun tick() {
        currentX += 1

        pillars.forEach {
            if (it.xPos < currentX) {
                it.recycle(currentX + WIDTH + it.width)
            }
        }
        /*
        if (!pillars.removeIf({ it.xPos + it.width < currentX }))
            return // No pillars removed, so return
        val lastPillarX = pillars.maxBy { it.xPos }
        pillars.addAll(
                List(PILLAR_COUNT - pillars.size, // How many pillars we add
                        { Pillar(lastPillarX + (it + 1) * WIDTH / PILLAR_COUNT) }) // Where we add them
        )
        */
    }

    /**
     * @return the first pillar you can still crash into
     */
    fun getNextPillar(): Pillar {
        return pillars.reduce({ pillar1, pillar2 ->
            return when {
                pillar1.xPos < currentX -> pillar2
                pillar2.xPos < currentX -> pillar1
                pillar1.xPos < pillar2.xPos -> pillar1
                else -> pillar2
            }
        })
    }

    companion object {
        val WIDTH = 300
        val HEIGHT = 500
        val PILLAR_COUNT = 2
    }
}
