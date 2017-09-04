package game

val WIDTH = 300
val HEIGHT = 600
val PILLAR_COUNT = 5

class Game {
    var currentX = 0
        private set

    val pillars = MutableList(PILLAR_COUNT, { Pillar((it + 1) * WIDTH / PILLAR_COUNT) })

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

        pillars.forEachIndexed { index, pillar ->
            if (pillar.xPos + pillar.width < currentX) {
                pillars[index] = Pillar(pillar.xPos + WIDTH)
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

}
