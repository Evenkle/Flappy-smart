package game

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class BirdTest {
    @Test
    fun create() {
        val game = Game()
        val brain = FakeBrain()
        val bird = Bird(game, brain)
        assertEquals(50, bird.yPos)
        assertEquals(0, bird.xPos)
    }

}