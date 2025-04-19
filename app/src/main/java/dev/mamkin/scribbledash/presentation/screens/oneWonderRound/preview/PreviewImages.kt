package dev.mamkin.scribbledash.presentation.screens.oneWonderRound.preview

import dev.mamkin.scribbledash.R
import kotlin.random.Random

enum class PreviewImages(val resourceId: Int) {
    ALIEN(R.drawable.alien),
    BICYCLE(R.drawable.bicycle),
    BOAT(R.drawable.boat),
    BOOK(R.drawable.book),
    BUTTERFLY(R.drawable.butterfly),
    CAMERA(R.drawable.camera),
    CAR(R.drawable.car),
    CASTLE(R.drawable.castle),
    CAT(R.drawable.cat),
    CLOCK(R.drawable.clock),
    CROWN(R.drawable.crown),
    CUP(R.drawable.cup),
    DOG(R.drawable.dog),
    ENVELOPE(R.drawable.envelope),
    EYE(R.drawable.eye),
    FISH(R.drawable.fish),
    FLOWER(R.drawable.flower),
    FOOTBALL_FIELD(R.drawable.football_field),
    FROG(R.drawable.frog),
    GLASSES(R.drawable.glasses),
    HEART(R.drawable.heart),
    HELICOTPER(R.drawable.helicotper),
    HOTAIRBALLOON(R.drawable.hotairballoon),
    HOUSE(R.drawable.house),
    MOON(R.drawable.moon),
    MOUNTAINS(R.drawable.mountains),
    ROBOT(R.drawable.robot),
    ROCKET(R.drawable.rocket),
    SMILEY(R.drawable.smiley),
    SNOWFLAKE(R.drawable.snowflake),
    SOFA(R.drawable.sofa),
    TRAIN(R.drawable.train),
    UMBRELLA(R.drawable.umbrella),
    WHALE(R.drawable.whale);

    companion object {
        fun getRandomImage(): PreviewImages {
            val values = PreviewImages.entries.toTypedArray()
            return values[Random.nextInt(values.size)]
        }
    }
}