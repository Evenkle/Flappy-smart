package genetics

import game.Bird
import game.FakeBrain
import game.Game
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

internal class GeneticAlgorithmTest {

    @Test
    fun doesWork() {
        runSimulation(null, 10)
    }

    private fun runSimulation(incoming: Population? = null, count:Int) {
        val game = Game()

        val oldPopulation = incoming ?: Population(10, true, game, 0)

        var i = 0
        while (oldPopulation.birds.any { !it.isDead } && i < 500000) {
            game.tick()
            oldPopulation.birds.forEach { it.tick() }
            i++
        }
        game.reset()

        val newPopulation = GeneticAlgorithm.evolvePopulation(oldPopulation)

        val startingPosition = Bird(game, FakeBrain()).xPos

        assertEquals(oldPopulation.birds.size, newPopulation.birds.size)

        newPopulation.birds.forEach { bird ->
            if (oldPopulation.birds.contains(bird)) {
                fail("$bird is in both new and old population! This will cause UI glitches!")
            }
            assertEquals(startingPosition, bird.xPos, "Bird was not at starting position")
        }

        println("Best bird of $count was ${oldPopulation.sortedBirds[0].xPos}")
        if (count > 0)
            runSimulation(newPopulation, count - 1)
    }

}