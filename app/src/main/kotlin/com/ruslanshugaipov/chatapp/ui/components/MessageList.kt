package com.ruslanshugaipov.chatapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.chat2desk.chat2desk_sdk.domain.entities.Message
import com.ruslanshugaipov.chatapp.R
import com.ruslanshugaipov.chatapp.ui.theme.MessageListBackground
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageList(
    modifier: Modifier,
    messages: List<Message>,
    isRefreshing: Boolean,
    onResend: (message: Message) -> Unit,
    onFetch: () -> Unit,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        coroutineScope.launch {
            listState.animateScrollToItem(0)
        }
    }


    PullToRefreshBox(
        modifier = modifier
            .background(MessageListBackground)
            .fillMaxWidth(),
        isRefreshing = isRefreshing,
        onRefresh = onFetch,
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            reverseLayout = true,
            contentPadding = PaddingValues(10.dp)
        ) {
            items(
                items = messages,
                key = { it.id }
            ) { message ->
                MessageItem(message = message, onResend = {
                    onResend(message)
                })
            }

            item {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (messages.isEmpty()) {
                        Text(
                            text = "Empty list",
                            style = MaterialTheme.typography.titleMedium
                        )
                    } else {
                        Button(onClick = onFetch) {
                            Text(
                                text = stringResource(id = R.string.load_more),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(name = "Messages List")
@Composable
fun MessageListPreview(@PreviewParameter(MessageListPreviewProvider::class) list: List<Message>) {
    MessageList(
        modifier = Modifier,
        messages = list,
        isRefreshing = false,
        onResend = {},
        onFetch = {}
    )
}

class MessageListPreviewProvider : PreviewParameterProvider<List<Message>> {
    override val values: Sequence<List<Message>> =
        sequenceOf(MessagePreviewParameterProvider().values.toList(), emptyList())
}
