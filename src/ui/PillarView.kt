package ui

import game.Game
import game.Pillar
import javafx.scene.Group
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Rectangle

var paint = LinearGradient(0.0, 0.0, 1.0, 0.0, true, CycleMethod.NO_CYCLE,
        arrayOf(Stop(0.0, Color.LIGHTGREEN), Stop(1.0, Color.DARKGREEN)).asList())

class PillarView(val pillar: Pillar): Group() {
    private val topPart = Rectangle(Pillar.width.toDouble(), (Game.HEIGHT - pillar.gapY - Pillar.gapSize / 2).toDouble())
    private val bottomPart = Rectangle(Pillar.width.toDouble(), (pillar.gapY - Pillar.gapSize / 2).toDouble())

    init {
        bottomPart.translateY = (Game.HEIGHT - pillar.gapY + Pillar.gapSize / 2).toDouble()
        topPart.fill = paint
        bottomPart.fill = paint
        children.add(topPart)
        children.add(bottomPart)
    }

    fun recycle() {
        topPart.height = (Game.HEIGHT - pillar.gapY - Pillar.gapSize / 2).toDouble()
        bottomPart.height = (pillar.gapY - Pillar.gapSize / 2).toDouble()
        bottomPart.translateY = (Game.HEIGHT - pillar.gapY + Pillar.gapSize / 2).toDouble()
    }
}