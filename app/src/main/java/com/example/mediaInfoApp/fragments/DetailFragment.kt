package com.example.mediaInfoApp.fragments

import SammensattTekst
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.fragment.app.Fragment

/**
 *  Har ansvaret for å håndtere visning av detalje informasjon om
 *  et element fra en liste
 */
class DetailFragment : Fragment() {

    private var composeView: ComposeView? = null
    private var item: SammensattTekst? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.composeView = ComposeView(requireContext())
        return this.composeView!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateContent()
    }
    companion object {

        fun new(item: SammensattTekst?): DetailFragment {
            return DetailFragment().apply {
                updateItem(item)
            }
        }
    }
    fun updateItem(newItem: SammensattTekst?) {
        this.item = newItem
        updateContent()
    }

    private fun updateContent() {
        composeView?.setContent {
            val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            DetailViewCompose(item, isLandscape)
        }
    }
}

@Composable
fun DetailViewCompose(item: SammensattTekst?, isLandscape: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF505050))
    ) {

        if (!isLandscape) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .background(Color(0xFF4A4A4A))
            )
        }
        //Hvis element er trykket på
        if (item != null) {
            Column(modifier = Modifier.fillMaxSize()) {

                if (!isLandscape) {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = item.title,
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )


                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 16.dp),
                        thickness = 1.dp,
                        color = Color(0xFF808080)
                    )


                    Image(
                        painter = painterResource(id = item.imageRes),
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(120.dp)
                            .padding(bottom = 24.dp)
                    )


                    LazyColumn(
                        state = rememberLazyListState(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp)
                    ) {
                        item {
                            Text(
                                text = item.beskrivelse.ifEmpty { "Detaljer for valgt element..." },
                                fontSize = 14.sp,
                                color = Color.Black,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        } else {

            Text(
                text = "Velg et element fra listen",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}