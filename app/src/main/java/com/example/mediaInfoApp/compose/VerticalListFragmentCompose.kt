package com.example.mediaInfoApp.compose

import RowCompose
import SammensattTekst
import SammensattTekstState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
@Composable
fun VerticalListFragmentCompose(
    items: List<SammensattTekst>,
    state: LazyListState,
    selectedItem: SammensattTekst?,
    onItemSelected: (SammensattTekst?) -> Unit
) {
    //Det som skal vises i listen
    val itemList = remember { mutableStateListOf<SammensattTekst>(*items.toTypedArray()) }

    //For å unngå dobbel klikk
    var hasClicked by remember { mutableStateOf(false) }

    // Skyldes i hovedsak at det kan skje endringer i selectedItem,
    //pga next/previous, noe som håndteres av menyen. Endringene
    //blir gjort av listFragment, og denne reagerer på endring i variabel
    LaunchedEffect(selectedItem) {
        selectedItem?.let { selected ->

            //Forrige element settes til å være i lukket tilstand
            val prevOpenItem = itemList.find { it.state.value == SammensattTekstState.OPEN }
            prevOpenItem?.state?.value = SammensattTekstState.CLOSED

            //Setter element en trykket på til å være i åpen tilstand
            val index = itemList.indexOfFirst { it.title == selected.title }
            if (index != -1) {
                itemList[index].state.value = SammensattTekstState.OPEN
                state.animateScrollToItem(index)
            }
        }
    }

    LazyColumn(
        state = state,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(itemList.size) { index ->

            //Elementet en trykket på
            val targetItem = itemList[index]

            //Tilstand til element en trykket på
            val isActive = targetItem.state.value == SammensattTekstState.OPEN

            RowCompose(
                isHorizontalFragment = false,
                item = targetItem,
                onClick = {

                    //For å unngå problemer med dobbel klikk
                    if (hasClicked) return@RowCompose
                    hasClicked = true

                    // Lukk forrige åpne element
                    val prevOpenItem = itemList.find { it.state.value == SammensattTekstState.OPEN }

                    // Endrer tilstand til forrige trykket element, gitt
                    // at den finnes.
                    // Sjekken prevOpenItem != targetItem er for
                    // å forhindre å dobbel endre tilstand til element,
                    //gitt at forrige element og neste element er trykket på
                    if (prevOpenItem != null && prevOpenItem != targetItem) {
                        prevOpenItem.state.value = SammensattTekstState.CLOSED
                    }

                    //Sjekker om en allerede har trykket på elementet
                    val wasOpen = targetItem.state.value == SammensattTekstState.OPEN

                    // Toggle det trykkede elementet
                    targetItem.state.value =
                        if (wasOpen) SammensattTekstState.CLOSED
                        else         SammensattTekstState.OPEN

                    //Hvis elementet en trykket på er lukket etter
                    // toggling (da er forrige og nåværende samme element),
                    // så settes det til null (tilsvarer ingen elmenent)
                    val openItem =
                        if (targetItem.state.value == SammensattTekstState.OPEN) targetItem
                        else                                                     null

                    //callback om hva en skal gjøre, når en har trykket på et element
                    onItemSelected(openItem)

                    hasClicked = false
                }
            )
            //Ingen divider under siste rad
            if (index < itemList.size - 1) {
                HorizontalDivider(
                    color = Color.Gray,
                    thickness = 1.dp
                )
                Spacer(modifier = if (isActive) Modifier.height(8.dp) else Modifier.height(12.dp))
            }
        }
    }
}
