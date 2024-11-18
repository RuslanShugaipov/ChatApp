package com.ruslanshugaipov.chatapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chat2desk.chat2desk_sdk.IChat2Desk
import com.ruslanshugaipov.chatapp.R
import com.ruslanshugaipov.chatapp.ui.theme.textSecondary
import com.ruslanshugaipov.chatapp.utils.AttachmentMeta
import com.ruslanshugaipov.chatapp.utils.toC2DAttachment
import kotlinx.coroutines.launch

@Composable
fun MessageInput(
    chat2desk: IChat2Desk,
    onOpenAttachment: () -> Unit,
    attachment: AttachmentMeta?,
    clearAttachment: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var text by remember { mutableStateOf("") }
    var sending by remember { mutableStateOf(false) }

    attachment?.let {
        AttachmentPreview(
            meta = it,
            onRemove = clearAttachment
        )
    }

    Row(modifier = Modifier.padding(horizontal = 10.dp)) {
        TextField(
            modifier = Modifier
                .weight(1f),
            value = text,
            onValueChange = { text = it },
            placeholder = {
                Text(
                    stringResource(id = R.string.send_message),
                    style = MaterialTheme.typography.bodyLarge,
                    color = textSecondary
                )
            },

            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                IconButton(
                    enabled = (text.isNotEmpty() || attachment != null) && !sending,
                    onClick = {
                        sending = true
                        val job = coroutineScope.launch {
                            attachment?.let {
                                chat2desk.sendMessage(
                                    text,
                                    it.toC2DAttachment(context)
                                )
                            } ?: chat2desk.sendMessage(text)
                        }
                        job.invokeOnCompletion {
                            text = ""
                            clearAttachment()
                            sending = false
                        }
                    }) {
                    Icon(
                        if (text.isNotEmpty()) Icons.Filled.Send else Icons.Outlined.Send,
                        contentDescription = stringResource(id = R.string.send_message),
                    )
                }
            },
            leadingIcon = {
                IconButton(
                    enabled = attachment == null,
                    onClick = { onOpenAttachment() }) {
                    Icon(Icons.Outlined.AttachFile, contentDescription = null)
                }
            }
        )
    }
}
