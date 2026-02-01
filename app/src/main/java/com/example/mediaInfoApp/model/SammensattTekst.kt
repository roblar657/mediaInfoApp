
    import androidx.annotation.DrawableRes
    import androidx.compose.runtime.MutableState
    import androidx.compose.runtime.mutableStateOf


    enum class SammensattTekstState {
        OPEN, CLOSED
    }

    data class SammensattTekst (
        val title: String,
        var state : MutableState<SammensattTekstState> = mutableStateOf(SammensattTekstState.CLOSED),
        val beskrivelse: String,
        @DrawableRes val imageRes: Int
    )
