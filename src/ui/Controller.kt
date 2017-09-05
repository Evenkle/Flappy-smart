package ui

import game.Game
import genetics.GeneticAlgorithm
import genetics.Population
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import java.util.*

val POPULATION_SIZE = 10

class Controller {
    @FXML
    var gameContainer: Pane? = null

    private var game = Game()
    private var population = Population(POPULATION_SIZE, true, game)
    private var timer = Timer()

    private val birdViews = population.birds.map { bird ->
        val imageView = ImageView("img/birdFrame0.png")
        imageView
    }

    private val pillarViews = game.pillars.map { pillar ->
        val pillarView = Rectangle()
        pillarView.width = pillar.width.toDouble()
        pillarView.height = pillar.gapSize.toDouble()
        pillarView.translateX = pillar.xPos.toDouble()
        pillarView.translateY = (Game.HEIGHT - pillar.gapY - pillar.gapSize / 2).toDouble()
        pillarView.fill = Color.valueOf("green")
        pillarView
    }

    @FXML
    fun initialize() {
        gameContainer?.children?.addAll(pillarViews)
        gameContainer?.children?.addAll(birdViews)
    }

    @FXML
    fun onStartClicked(action: ActionEvent) {
        println("Starting game!")
        restartTimer()
    }

    @FXML
    fun onStopClicked(action: ActionEvent) {
        println("Stopping game")
        timer.cancel()
        timer.purge()
    }

    @FXML
    fun onResetClicked(action: ActionEvent) {
        println("Resetting game")
        game = Game()
        population = Population(POPULATION_SIZE, true, game)
        restartTimer()
    }

    private fun restartTimer() {
        timer.cancel()
        timer.purge()

        timer = Timer()
        timer.scheduleAtFixedRate(ticker, 500, 1000 / 60)
    }

    private val ticker
        get() = object : TimerTask() {
            override fun run() {
                game.tick()
                population.birds.forEach { it.tick() }
                updateUI()
                if (population.birds.all { it.isDead }) {
                    println("All dead")
                    timer.cancel()
                    createGeneration()
                }
            }
        }

    private fun createGeneration() {
        game.reset()
        population = GeneticAlgorithm.evolvePopulation(population)
        restartTimer()
    }

    private fun updateUI() {
        birdViews.forEachIndexed { index, imageView ->
            val bird = population.birds[index]
            imageView.translateX = (bird.xPos - game.currentX - bird.size).toDouble()
            imageView.translateY = (Game.HEIGHT - bird.yPos - bird.size).toDouble()
        }
        pillarViews.forEachIndexed { index, pillarView ->
            val pillar = game.pillars[index]
            pillarView.translateX = (pillar.xPos - pillar.width - game.currentX).toDouble()
            pillarView.translateY = (Game.HEIGHT - pillar.gapY - pillar.gapSize / 2).toDouble()
        }
    }

}
