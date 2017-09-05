package game

val WIDTH = 300
val HEIGHT = 600
val PILLAR_COUNT = 3

class Game {
    var currentX = 0
        private set

    val pillars = List(PILLAR_COUNT, { Pillar((it + 1) * WIDTH / PILLAR_COUNT) })

    fun reset() {
        currentX = 0
        pillars.forEachIndexed({ index, pillar -> pillar.xPos = (index + 1) * WIDTH / PILLAR_COUNT })
    }

    fun checkKilled(bird: Bird): Boolean {
        if (bird.yPos <= 0) return true
        // Check if we crashed into any pillar whatsoever
        return pillars.any {
            val afterStart = bird.xPos > it.xPos - it.width
            val beforeEnd = bird.xPos - bird.size < it.xPos
            val belowGap = bird.yPos < it.gapY - it.gapSize / 2
            val aboveGap = bird.yPos + bird.size > it.gapY + it.gapSize / 2
            afterStart && beforeEnd && (belowGap || aboveGap)
        }
    }

    fun tick() {
        currentX += 1

        pillars.forEach {
            if (it.xPos + it.width < currentX) {
                it.xPos = currentX + WIDTH
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

}
