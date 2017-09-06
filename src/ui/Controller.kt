package ui

import game.Pillar
import game.Bird
import game.Game
import genetics.GeneticAlgorithm
import genetics.Population
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import smart.HumanBrain
import java.util.*

val POPULATION_SIZE = 10

class Controller {
    @FXML
    var gameContainer: Pane? = null

    @FXML
    var statusText: Label? = null

    private var game = Game()
    private var population = Population(POPULATION_SIZE, true, game, 1)
    private var timer = Timer()
    private var bestDistance = 0

    var humanBird:Bird? = null
    val humanBirdView = ImageView("img/birdFrame0.png")

    private val birdViews = population.birds.map { bird ->
        val imageView = ImageView("img/birdFrame0.png")
        imageView
    }

    private val pillarViews = game.pillars.map { pillar ->
        val pillarView = Rectangle()
        pillarView.width = Pillar.width.toDouble()
        pillarView.height = Pillar.gapSize.toDouble()
        pillarView.translateX = pillar.xPos.toDouble()
        pillarView.translateY = (Game.HEIGHT - pillar.gapY - Pillar.gapSize / 2).toDouble()
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
        statusText?.text = "Best distance: ${bestDistance}\nGeneration: ${population.generation}"
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
        population = Population(POPULATION_SIZE, true, game, 1)
        bestDistance = 0
        statusText?.text = "Best distance: ${bestDistance}\nGeneration: ${population.generation}"
        restartTimer()
    }

    @FXML
    fun onJumpClicked(action: MouseEvent) {
        if (humanBird == null) {
            humanBird = Bird(game, HumanBrain())
            gameContainer?.children?.add(humanBirdView)
        }
        (humanBird?.brain as HumanBrain).thinksAboutJumping = action.eventType == MouseEvent.MOUSE_PRESSED
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
                humanBird?.tick()
                updateUI()
                if (population.birds.all { it.isDead }) {
                    println("All dead")
                    timer.cancel()
                    Platform.runLater {
                        createGeneration()
                    }
                }
            }
        }

    private fun createGeneration() {
        bestDistance = Math.max(bestDistance, game.currentX)
        statusText?.text = "Best distance: ${bestDistance}\nGeneration: ${population.generation}"
        game.reset()
        population = GeneticAlgorithm.evolvePopulation(population)
        restartTimer()
    }

    private fun updateUI() {
        Platform.runLater {
            if (humanBird != null) {
                humanBirdView.translateX = (humanBird!!.xPos - game.currentX - humanBird!!.size).toDouble()
                humanBirdView.translateY = (Game.HEIGHT - humanBird!!.yPos - humanBird!!.size).toDouble()
            }
            birdViews.forEachIndexed { index, imageView ->
                val bird = population.birds[index]
                imageView.translateX = (bird.xPos - game.currentX - bird.size).toDouble()
                imageView.translateY = (Game.HEIGHT - bird.yPos - bird.size).toDouble()
            }
            pillarViews.forEachIndexed { index, pillarView ->
                val pillar = game.pillars[index]
                pillarView.translateX = (pillar.xPos - Pillar.width - game.currentX).toDouble()
                pillarView.translateY = (Game.HEIGHT - pillar.gapY - Pillar.gapSize / 2).toDouble()
            }
        }
    }

}
