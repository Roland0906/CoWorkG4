package app.appworks.school.stylish.tinder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import androidx.fragment.app.Fragment
import app.appworks.school.stylish.R
import androidx.navigation.fragment.findNavController
import app.appworks.school.stylish.NavigationDirections

class TinderFragment : Fragment() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val swipeResults = mutableListOf<Int>()
        return ComposeView(requireContext()).apply {
            setContent {
                TinderCloneTheme {
                    var isEmpty by remember { mutableStateOf(false) }
                    var showOverlay by remember { mutableStateOf(true) }

                    Scaffold {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(it),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                if (!isEmpty) {
                                    CardStack(
                                        swipeResults = swipeResults,
                                        items = accounts,
                                        onEmptyStack = {
                                            isEmpty = true
                                        }
                                    )
                                } else {
                                    Text(text = "No more cards", fontWeight = FontWeight.Bold)
                                    //post api to submit the outcomes
                                    //get the outcome from api and then
                                    findNavController().navigate(NavigationDirections.navigateToTinderSuccessFragment())
                                    Log.i("tinder", "$swipeResults")
                                }
                            }
                            if (showOverlay) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.7f))
                                        .zIndex(1f)
                                        .clickable {
                                            showOverlay = false
                                        }// Place this on top
                                ) {
                                    // Add your content for the overlay here
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private val accounts = mutableListOf(
        Item(
            "https://images.unsplash.com/photo-1668069574922-bca50880fd70?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "Musician",
            "Alice (25)"
        ),
        Item(
            "https://images.unsplash.com/photo-1618641986557-1ecd230959aa?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "Developer",
            "Chris (33)"
        ),
        Item(
            "https://images.unsplash.com/photo-1667935764607-73fca1a86555?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=688&q=80",
            "Teacher",
            "Roze (22)"
        )
    )
}