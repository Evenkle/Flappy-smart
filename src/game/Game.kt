package game

val WIDTH = 300
val HEIGHT = 600
val PILLAR_COUNT = 5

class Game {
    var currentX = 0
        private set

    val pillars = MutableList(PILLAR_COUNT, { Pillar(it * WIDTH / PILLAR_COUNT) })

    fun willSurvive(bird: Bird): Boolean {
        // Check if we crashed into any pillar whatsoever
        return pillars.any {
            bird.xPos - bird.size > it.xPos - it.width &&
                    bird.xPos < it.xPos + it.width &&
                    bird.yPos - bird.size > it.gapY - it.gapSize / 2 &&
                    bird.yPos < it.gapY + it.gapSize / 2
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
