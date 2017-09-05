package ui

import game.Game
import game.HEIGHT
import game.WIDTH
import genetics.Population
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import java.util.*

val POPULATION_SIZE = 10

class Controller {
    @FXML
    var gameContainer: Pane? = null

    val game = Game()
    var population = Population(POPULATION_SIZE, true, game)
    private var timer = Timer()

    val birdViews = List(POPULATION_SIZE, {
        val imageView = ImageView("img/birdFrame0.png")
        imageView.x = (WIDTH / 2).toDouble()
        imageView.opacity = 0.5
        imageView
    })

    @FXML
    fun initialize() {
        gameContainer?.children?.addAll(birdViews)
    }

    @FXML
    fun onStartClicked(action: ActionEvent) {
        println("Starting game!")
        timer.cancel()
        timer.purge()

        (action.source as Button).isVisible = false

        timer = Timer()
        timer.scheduleAtFixedRate(ticker, 0, 1000 / 60)
    }

    private val ticker
        get() = object : TimerTask() {
            override fun run() {
                println("Tick!")
                game.tick()
                population.birds.forEach { it.tick() }
                updateUI()
                if (population.birds.all { it.isDead }) {
                    println("All dead?!")
                    timer.cancel()
                }
            }
        }

    private fun updateUI() {
        birdViews.forEachIndexed { index, imageView ->
            val bird = population.birds[index]
            imageView.y = (HEIGHT - bird.yPos).toDouble()
            println(bird)
        }
    }

}
