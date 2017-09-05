package genetics

import game.Bird
import game.FakeBrain
import game.Game
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.fail

internal class GeneticAlgorithmTest {
    @Test
    fun doesMakeNewBirds() {
        val game = Game()
        val firstPopulation = Population(5, true, game)
        while (firstPopulation.birds.any { !it.isDead }) {
            game.tick()
            firstPopulation.birds.forEach { it.tick() }
        }
        game.reset()

        val newPopulation = GeneticAlgorithm.evolvePopulation(firstPopulation)

        val startingPosition = Bird(game, FakeBrain()).xPos

        newPopulation.birds.forEach { bird ->
            if (firstPopulation.birds.contains(bird)) {
                fail("$bird is in both new and old population! This will cause UI glitches!")
            }
            assertEquals(startingPosition, bird.xPos, "Bird was not at starting position")
        }
    }

}