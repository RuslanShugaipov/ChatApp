package com.ruslanshugaipov.chatapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chat2desk.chat2desk_sdk.IChat2Desk
import com.chat2desk.chat2desk_sdk.domain.entities.Message
import com.ruslanshugaipov.chatapp.R
import com.ruslanshugaipov.chatapp.ui.theme.HeaderBackground
import kotlinx.coroutines.launch

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    chat2Desk: IChat2Desk,
    client: String?
) {
    val operator = chat2Desk.operator.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var expanded by remember { mutableStateOf(false) }

    var isSearchActive by rememberSaveable { mutableStateOf(false) }

    var searchResult by remember {
        mutableStateOf<List<Message>>(emptyList())
    }

    fun search(query: String) {
        searchResult = chat2Desk.searchByQuery(query)
    }

    Surface(color = HeaderBackground, shadowElevation = 4.dp, modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Operator(operator = operator.value, Modifier.weight(1f))
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.MoreVert, null, tint = MaterialTheme.colorScheme.primary)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(
                            Color.White
                        )
                    ) {
                        DropdownMenuItem(onClick = {
                            coroutineScope.launch {
                                chat2Desk.flushAll()
                                expanded = false
                            }
                        }, text = {
                            Text(text = stringResource(id = R.string.flush_all_data))
                        })
                        DropdownMenuItem(onClick = {
                            coroutineScope.launch {
                                chat2Desk.fetchMessages(clear = true)
                                expanded = false
                            }
                        }, text = {
                            Text(text = stringResource(id = R.string.fetch_messages))
                        })
                        DropdownMenuItem(onClick = {
                            coroutineScope.launch {
                                chat2Desk.read()
                                expanded = false
                            }
                        }, text = {
                            Text(text = stringResource(id = R.string.read_chat))
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            ConnectionStatus(chat2Desk, client)
            Spacer(modifier = Modifier.height(10.dp))
            EmbeddedSearchBar(
                onQueryChange = ::search,
                isSearchActive = isSearchActive,
                onActiveChanged = { isSearchActive = it }) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(
                        items = searchResult,
                        key = { _, message -> message.id }
                    ) { index, message ->
                        Text(text = message.text ?: "EMPTY")
                        Spacer(modifier = Modifier.height(10.dp))
                        if (index < searchResult.lastIndex) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}
