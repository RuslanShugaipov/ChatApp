package com.ruslanshugaipov.chatapp.ui.components

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.chat2desk.chat2desk_sdk.IChat2Desk
import com.chat2desk.chat2desk_sdk.datasource.services.ConnectionState
import com.chat2desk.chat2desk_sdk.domain.entities.Message
import com.ruslanshugaipov.chatapp.utils.AttachmentMeta
import com.ruslanshugaipov.chatapp.utils.getFileMetaData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chat(chat2desk: IChat2Desk, client: String?) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val messages = chat2desk.messages.collectAsState(initial = listOf())

    val state by chat2desk.connectionStatus.collectAsState()

    fun resendMessage(message: Message) = coroutineScope.launch {
        chat2desk.resendMessage(message)
    }

    fun fetchMessages() = coroutineScope.launch {
        chat2desk.fetchMessages()
    }

    val attachmentSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var attachment by remember {
        mutableStateOf<AttachmentMeta?>(null)
    }

    fun handleAttachmentSelect(uri: Uri?) = coroutineScope.launch {
        uri?.let { attachment = getFileMetaData(context, it) }

        launch { attachmentSheetState.hide() }.invokeOnCompletion {
            if (!attachmentSheetState.isVisible) {
                showBottomSheet = false
            }
        }
    }

    fun onClearAttachment() {
        attachment = null
    }

    fun handleOpenAttachment() {
        showBottomSheet = true
    }

    LaunchedEffect(Unit) {
        if (state === ConnectionState.CLOSED) {
            chat2desk.start(clientId = client)
        }
    }
    LaunchedEffect(state) {
        if (state == ConnectionState.CONNECTED) {
//            chat2desk.sendClientParams(
//                "Chat2Desk SDK Demo",
//                "Test Phone",
//                mapOf(1 to "Field 1", 5 to "Field 5")
//            )
            chat2desk.fetchNewMessages()
        }
    }

    Column {
        MessageList(Modifier.weight(1f), messages.value, false, ::resendMessage, ::fetchMessages)

        MessageInput(chat2desk, ::handleOpenAttachment, attachment, ::onClearAttachment)
    }
    if (showBottomSheet) {
        val bottomPadding = WindowInsets.navigationBars.asPaddingValues()
            .calculateBottomPadding().value.toInt() + 30
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = attachmentSheetState
        ) {
            Box(
                Modifier
                    .padding(bottom = bottomPadding.dp)
            ) {
                AttachmentModal(onSelect = ::handleAttachmentSelect)
            }
        }
    }
}
