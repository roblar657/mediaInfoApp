package com.example.mediaInfoApp.fragments

import SammensattTekst
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

import com.example.mediaInfoApp.compose.HorizontalListFragmentCompose
import com.example.mediaInfoApp.compose.VerticalListFragmentCompose

/**
 * Har ansvaret for å håndtere lister.
 */
class ListFragment : Fragment() {

    private var items: List<SammensattTekst> = emptyList()

    private var listState: LazyListState? = null
    private var composeView: ComposeView? = null



    private var onItemSelectedCallback: ((SammensattTekst?) -> Unit)? = null

    private var getOpenItemTitleCallback: (() -> String?)? = null
    private var getSelectedItemCallback: (() -> SammensattTekst?)? = null

    private var setSelectedItemCallback: ((SammensattTekst?) -> Unit)? = null

    private var setOpenItemTitleCallback: ((String?) -> Unit)? = null

    companion object {

        fun new(
            items: List<SammensattTekst>,
            getSelectedItemCallback: () -> SammensattTekst?,
            getOpenItemTitleCallback: () -> String?,
            setSelectedItemCallback: (SammensattTekst?) -> Unit,
            setOpenItemTitleCallback: (String?) -> Unit,
            onItemSelectedCallback: (SammensattTekst?) -> Unit
        ): ListFragment {
            return ListFragment().apply {
                this.items = items
                this.getSelectedItemCallback = getSelectedItemCallback
                this.getOpenItemTitleCallback = getOpenItemTitleCallback
                this.setSelectedItemCallback = setSelectedItemCallback
                this.setOpenItemTitleCallback = setOpenItemTitleCallback
                this.onItemSelectedCallback = onItemSelectedCallback
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        composeView = ComposeView(requireContext())
        return composeView!!.apply {
            setContent {
                listState = listState ?: rememberLazyListState()
                updateContent()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateContent()
    }
    override fun onDetach() {
        super.onDetach()
        this.getSelectedItemCallback = null
        this.getOpenItemTitleCallback = null
        this.setSelectedItemCallback = null
        this.setOpenItemTitleCallback =  null
        this.onItemSelectedCallback = null
    }


    /**
     * Oppdater listen når et element er valgt eller fravalgt.
     */
    fun updateItemStates(selectedItem: SammensattTekst?) {

        setSelectedItemCallback?.invoke(selectedItem)
        setOpenItemTitleCallback?.invoke(selectedItem?.title)
        updateContent()
    }
    private val selectedItem: SammensattTekst?
        get() = getSelectedItemCallback?.invoke()


    /**
     * Håndterer klikk på et element i listen.
     */
    private fun handleItemSelected(clickedItem: SammensattTekst?) {

        val isAlreadyOpen = this.selectedItem?.title == clickedItem?.title
        val newItem: SammensattTekst? =
            if (isAlreadyOpen) null
            else               clickedItem

        onItemSelectedCallback?.invoke(newItem)
    }

    /**
     * Oppdaterer Compose-innholdet i fragmentet.
     */
    private fun updateContent() {

        composeView?.setContent {
            listState = listState ?: rememberLazyListState()
            ListFragmentContent(
                items = items,
                orientation = resources.configuration.orientation,
                state = listState!!,
                selectedItem = this.selectedItem,
                onItemSelected = { handleItemSelected(it) }
            )
        }
    }
}

/**
 * Bygger enten horisontal eller vertikal liste basert på orientering.
 */
@Composable
fun ListFragmentContent(
    items: List<SammensattTekst>,
    orientation: Int,
    state: LazyListState,
    selectedItem: SammensattTekst?,
    onItemSelected: (SammensattTekst?) -> Unit
) {
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        HorizontalListFragmentCompose(items, state, selectedItem, onItemSelected)
    } else {
        VerticalListFragmentCompose(items, state, selectedItem, onItemSelected)
    }
}
