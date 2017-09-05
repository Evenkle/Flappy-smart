package game

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BirdTest {
    @Test
    fun create() {
        val game = Game()
        val brain = FakeBrain()
        val bird = Bird(game, brain)

        assertEquals(50, bird.yPos)
        assertEquals(0, bird.xPos)

        game.tick()
        bird.tick()

        assertEquals(49, bird.yPos)
        assertEquals(1, bird.xPos)
        assertFalse(bird.isDead)

        for (i in 0..8) {
            game.tick()
            bird.tick()
        }

        assertTrue(bird.isDead)
        assertTrue(0 >= bird.yPos)
    }

    @Test
    fun fitness() {
        val game = Game()
        val brain = FakeBrain()
        val bird = Bird(game, brain)

        for (i in 0..5) {
            for (j in 0..10) {
                game.tick()
                bird.tick()
            }
            brain.willJump = !brain.willJump
        }

        assertTrue(bird.fitness > -200, bird.toString())
    }

}