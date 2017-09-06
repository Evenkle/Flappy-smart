package ui

import game.Game
import game.Pillar
import javafx.scene.Group
import javafx.scene.shape.Rectangle

class PillarView(val pillar: Pillar): Group() {
    val topPart = Rectangle(Pillar.width.toDouble(), (Game.HEIGHT - pillar.gapY - Pillar.gapSize / 2).toDouble())
    val bottomPart = Rectangle(Pillar.width.toDouble(), (pillar.gapY - Pillar.gapSize / 2).toDouble())

    init {
        bottomPart.translateY = (Game.HEIGHT - pillar.gapY + Pillar.gapSize / 2).toDouble()
        children.add(topPart)
        children.add(bottomPart)
    }

    fun recycle() {
        topPart.height = (Game.HEIGHT - pillar.gapY - Pillar.gapSize / 2).toDouble()
        bottomPart.height = (pillar.gapY - Pillar.gapSize / 2).toDouble()
        bottomPart.translateY = (Game.HEIGHT - pillar.gapY + Pillar.gapSize / 2).toDouble()
    }
}