package hu.bme.aut.android.monkeychess.board.pieces.enums

enum class Side {
    UP, DOWN;

    fun oppositeSide(): Side {
        return when (this) {
            UP -> DOWN
            DOWN -> UP
        }
    }
}