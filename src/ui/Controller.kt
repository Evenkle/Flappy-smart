package ui

import game.Bird
import game.Game
import game.Pillar
import genetics.GeneticAlgorithm
import genetics.Population
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.text.Text
import smart.HumanBrain
import java.util.*

val POPULATION_SIZE = 10

class Controller {
    @FXML
    var gameContainer: Pane? = null

    @FXML
    var statusText: Label? = null

    @FXML
    var textContainer: GridPane? = null

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
        PillarView(pillar)
    }

    var scoreTexts: List<Text>? = null
    var fitnessTexts: List<Text>? = null

    @FXML
    fun initialize() {
        gameContainer?.children?.addAll(pillarViews)
        gameContainer?.children?.addAll(birdViews)
        val children = textContainer?.children
        scoreTexts = children?.filter { it.id.startsWith("score") } as List<Text>
        fitnessTexts = children?.filter { it.id.startsWith("fitness") } as List<Text>
    }

    @FXML
    fun onStartClicked(action: ActionEvent) {
        println("Starting game!")
        statusText?.text = "Best distance: ${bestDistance}. Generation: ${population.generation}. "
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
        statusText?.text = "Best distance: ${bestDistance}. Generation: ${population.generation}. "
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
        bestDistance = Math.max(bestDistance, population.birds.maxBy { it.score }!!.score)
        statusText ?. text = "Best distance: ${bestDistance}. Generation: ${population.generation}."
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
            population.birds.forEachIndexed{index, bird ->
                val imageView = birdViews[index]
                imageView.translateX = (bird.xPos - game.currentX - bird.size).toDouble()
                imageView.translateY = (Game.HEIGHT - bird.yPos - bird.size).toDouble()
                if (!bird.isDead) {
                    scoreTexts?.get(index)?.text = bird.score.toString()
                    fitnessTexts?.get(index)?.text = bird.fitness.toString()
                }
            }
            pillarViews.forEachIndexed { index, pillarView ->
                val pillar = game.pillars[index]
                pillarView.recycle()
                pillarView.translateX = (pillar.xPos - Pillar.width - game.currentX).toDouble()
            }
        }
    }

}
