package com.example.mediaInfoApp

import SammensattTekst
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import com.example.mediaInfoApp.fragments.DetailFragment
import com.example.mediaInfoApp.fragments.ListFragment
import com.example.mediaInfoApp.compose.FragmentComposable

class MainActivity : FragmentActivity() {


    private var openItemTitle: String? = null
    private var selectedItem: SammensattTekst? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        selectedItem?.let {
            outState.putString("selectedItemTitle", it.title)
            outState.putString("selectedItemDescription", it.beskrivelse)
            outState.putInt("selectedItemImageRes", it.imageRes)
            outState.putString("selectedItemState", it.state.value.name)
        }
        outState.putString("openItemTitle", openItemTitle)
    }

    private lateinit var sammensattTekstItems: List<SammensattTekst>
    private var currentIndex by mutableIntStateOf(0)
    private var listFragment: ListFragment? = null
    private var detailFragment: DetailFragment? = null


    /**
     * Henter inn resurs id, for en gitt filnavn
     * Henter fra /res/drawable
     */
    private fun getImageResource(imageName: String): Int {
        return try {
            val resourceName = imageName.lowercase()
            if (resourceName.isEmpty() || resourceName[0].isDigit()) return android.R.drawable.ic_media_play
            resources.getIdentifier(resourceName, "drawable", packageName).takeIf { it != 0 }
                ?: android.R.drawable.ic_media_play
        } catch (e: Exception) {
            android.R.drawable.ic_media_play
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //Lastes inn på nytt hver gang
        sammensattTekstItems = listOf(

            SammensattTekst(
                title = "Harry Potter",
                beskrivelse = """
                    En film som omhandler et slott, og en fantasi verden, som kan nåes gjennom
                    den vanlige verden
                              """.trimIndent(),
                imageRes = getImageResource("harry_potter")
            ),
            SammensattTekst(
                title = "24",
                beskrivelse = """
                    Film-serie om hvordan verden kunne ha fungert om alt var lov, gitt at en
                    skal redde verden
        """.trimIndent(),
                imageRes = getImageResource("24")
            ),
            SammensattTekst(
                title = "James Bond",
                beskrivelse = """
                 Film-serie om MI6 agent som løser oppdrag
        """.trimIndent(),
                imageRes = getImageResource("james_bond")
            ),
            SammensattTekst(
                title = "Stargate",
                beskrivelse = "Film-serie om planeter og galakser",
                imageRes = getImageResource("stargate")
            ),


            SammensattTekst(
                title = "Kaptein Sabeltann",
                beskrivelse = "Bok-serie om en pirat",
                imageRes = getImageResource("kaptein_sabeltann")
            ),
            SammensattTekst(
                title = "Kardemomme by",
                beskrivelse = "Bok-serie om fredelige mennesker i møte med skumle røvere",
                imageRes = getImageResource("kardemomme_by")
            ),
            SammensattTekst(
                title = "Ole-brum",
                beskrivelse = "Bok-serie om en som spiser honning",
                imageRes = getImageResource("Ole-brum")
            ),


            SammensattTekst(
                title = "Sol",
                beskrivelse = """
                    Wikipedia (https://nn.wikipedia.org/wiki/Sola) sier at:
                    
                    Sola er stjerna som ligg i sentrum av solsystemet. Jorda og andre himmellekamar, som planetar, asteroidar, meteoroidar, kometar og støv, går i bane rundt sola. Sola står for meir enn 99 % av massen i solsystemet, og energien som ho ståler ut, støttar nesten alt liv på jorda gjennom fotosyntese. Varmen frå sola er den drivande krafta i klimaet og vêret på jorda, og ho står for 1/3 av tidvasseffektane – resten står månen for. Gjennom solstrålane er ho hovudkjelda for det meste av energien ein bruker på jorda, og mange kulturar har derfor vyrdt sola som ein guddom.
                    
                """.trimIndent(),
                imageRes = getImageResource("sol")
            ),
            SammensattTekst(
                title = "Måne",
                beskrivelse = """
                    Wikipedia(https://no.wikipedia.org/wiki/M%C3%A5nen) sier at:
                   
                    Månen (astronomisk symbol: ☾) er den eneste naturlige satellitten i bane rundt jorden,[h][L 6] og den femte største satellitten i solsystemet. Sett i forhold til størrelsen på primærlegemet, er månen den største naturlige satellitten tilhørende en planet i solsystemet med en diameter som tilsvarer en fjerdedel av jordens, men bare 1/81 av massen.[i] Månen er den nest mest kompakte satellitten etter Io, en av Jupiters måner. Den er i en bundet rotasjon med jorden – det vil si at den alltid har den samme siden vendt mot jorden, markert av et mørkt vulkansk hav som fyller området mellom de lyse antikke høylandene og de fremtredende nedslagskratrene.
                """.trimIndent(),
                imageRes = getImageResource("mane")
            ),
            SammensattTekst(
                title = "Hus",
                beskrivelse = "Et bygning som en vanligvis bor i",
                imageRes = getImageResource("hus")
            ),
            SammensattTekst(
                title = "Blyant",
                beskrivelse = """
                    Wikipedia (https://no.wikipedia.org/wiki/Blyant) sier at:
                    
                    En blyant er et redskap for å tegne og skrive, vanligvis på papir. Blyanter er hovedsakelig bygd opp av en kjerne (kalt «bly»). Til skriving på tykt papir, ble det opprinnelig brukt en legering av bly og tinn. Nå brukes blyant som består av en stift av grafitt og leire (med unntak av fargeblyanter), og en mantel av tre. Mange blyanter har et viskelær festet til enden.

                    For finere blyanter blir virket laget av den røde, velduftende kjerneveden fra blyanttreet. Dette treet kan bli opptil 30 meter høyt, og er en amerikansk einerart.
                """.trimIndent(),
                imageRes = getImageResource("blyant")
            )
        ).distinctBy { it.title }.sortedBy { it.title.lowercase() }

        savedInstanceState?.let {
            val title = it.getString("selectedItemTitle")
            val description = it.getString("selectedItemDescription")
            val imageRes = it.getInt("selectedItemImageRes", 0)
            val stateStr = it.getString("selectedItemState", SammensattTekstState.CLOSED.name)

            selectedItem =
              if (title != null && description != null)
                    SammensattTekst(
                        title = title,
                        beskrivelse = description,
                        imageRes = imageRes,
                        state = mutableStateOf(SammensattTekstState.valueOf(stateStr))
                    )
              else null

            openItemTitle = it.getString("openItemTitle")
        }
        //status bar
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(android.graphics.Color.WHITE, android.graphics.Color.BLACK),
            navigationBarStyle = SystemBarStyle.light(android.graphics.Color.WHITE, android.graphics.Color.BLACK)
        )

        val composeView = ComposeView(this)
        setContentView(composeView)

        if (listFragment == null) {
            listFragment = ListFragment.new(
                items = sammensattTekstItems,
                getSelectedItemCallback = { selectedItem },
                getOpenItemTitleCallback = { openItemTitle },
                setSelectedItemCallback = { item -> selectedItem = item },
                setOpenItemTitleCallback = { title -> openItemTitle = title },
                onItemSelectedCallback = { item ->
                    updateDetail(item)
                }
            )
        }
        if (detailFragment == null) {
            detailFragment = DetailFragment.new(selectedItem)
        }
        composeView.setContent { Content() }
    }

    @Composable
    private fun Content() {
        val isLandscape = resources.configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

        //Ved endringer i denne, må indexen for hva som vil bli next/previous
        //også oppdateres
        LaunchedEffect(selectedItem) {
            currentIndex = selectedItem?.let { item ->
                sammensattTekstItems.indexOfFirst { it.title == item.title }
            } ?: 0
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )

            NavigationBar(
                currentIndex = currentIndex,
                totalItems = sammensattTekstItems.size,
                onNavigate = { direction ->
                    when (direction) {
                        "previous" -> if (currentIndex > 0) updateDetail(sammensattTekstItems[currentIndex - 1])
                        "next" -> if (currentIndex < sammensattTekstItems.size - 1) updateDetail(sammensattTekstItems[currentIndex + 1])
                    }
                }
            )

            val fragmentModifier = Modifier.fillMaxWidth().weight(1f)

            if (isLandscape) {
                Row(modifier = Modifier.fillMaxSize()) {
                    FragmentComposable(supportFragmentManager, listFragment!!, fragmentModifier)
                    FragmentComposable(supportFragmentManager, detailFragment!!, fragmentModifier)
                }
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    FragmentComposable(supportFragmentManager, listFragment!!, fragmentModifier)
                    FragmentComposable(supportFragmentManager, detailFragment!!, fragmentModifier)
                }
            }
        }
    }

    /**
     * Navigasjonsbar med next og previous, med tilhørende callback for hva som skal skje
     */
    @Composable
    private fun NavigationBar(currentIndex: Int, totalItems: Int, onNavigate: (String) -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // Media info til venstre, knappene til høyre
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Media Info",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 16.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(enabled = currentIndex > 0) { onNavigate("previous") }
                        .padding(end = 16.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous",
                        tint = if (currentIndex > 0) Color.White else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        "Previous",
                        color = if (currentIndex > 0) Color.White else Color.Gray,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(enabled = currentIndex < totalItems - 1) { onNavigate("next") }
                ) {
                    Text(
                        "Next",
                        color = if (currentIndex < totalItems - 1) Color.White else Color.Gray,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next",
                        tint = if (currentIndex < totalItems - 1) Color.White else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }


    fun updateDetail(item: SammensattTekst?) {
        selectedItem = item.takeUnless { it?.title == selectedItem?.title }


        if (selectedItem != null) {
            // Endrer tittel i companion object, for referanse om hva en har valgt
            openItemTitle = selectedItem!!.title
            // Brukt til next/previous, for å finne neste og forrige element
            currentIndex = sammensattTekstItems.indexOfFirst { it.title == selectedItem!!.title }
        } else {
            openItemTitle = null
            currentIndex = -1
        }


        // Oppdater hva som vises i detail, men gjør ingen endringer i selectedItem
        detailFragment?.updateItem(selectedItem)
        //Endringen i selectedItem skjer først inni list fragmentet
        listFragment?.updateItemStates(selectedItem)
    }
}
