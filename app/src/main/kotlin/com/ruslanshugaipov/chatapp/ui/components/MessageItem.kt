package com.ruslanshugaipov.chatapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.chat2desk.chat2desk_sdk.domain.entities.DeliveryStatus
import com.chat2desk.chat2desk_sdk.domain.entities.Message
import com.chat2desk.chat2desk_sdk.domain.entities.MessageType
import com.chat2desk.chat2desk_sdk.domain.entities.ReadStatus
import com.ruslanshugaipov.chatapp.ui.theme.inMessageBackground
import com.ruslanshugaipov.chatapp.ui.theme.outMessageBackground
import com.ruslanshugaipov.chatapp.utils.messageDate
import com.ruslanshugaipov.chatapp.utils.statusIcon
import kotlinx.datetime.Instant

@Composable
fun MessageItem(message: Message, onResend: () -> Unit) {
    val horizontalArrangement = if (message.inMessage()) Arrangement.End else Arrangement.Start
    val color = if (message.inMessage()) inMessageBackground else outMessageBackground

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (message.inMessage() && message.status == DeliveryStatus.NOT_DELIVERED) {
            IconButton(onClick = onResend) {
                Icon(
                    Icons.Outlined.Replay,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            modifier = Modifier
                .fillMaxWidth(0.75f),
            colors = CardDefaults.cardColors(
                containerColor = color
            )
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                if (!message.attachments.isNullOrEmpty()) {
                    message.attachments?.filter { it.contentType.contains("image/") }?.map {
                        ImageAttachment(it)
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                    message.attachments?.filter { !it.contentType.contains("image/") }?.map {
                        DocumentAttachment(it)
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
                if (!message.text.isNullOrEmpty()) {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = message.text!!,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = horizontalArrangement
                ) {
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        text = messageDate(LocalContext.current, message.date),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        statusIcon(message.status),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


class MessagePreviewParameterProvider : PreviewParameterProvider<Message> {
    override val values: Sequence<Message> =
        sequenceOf(
            Message(
                id = "1",
                date = Instant.fromEpochSeconds(1667999139),
                read = ReadStatus.READ,
                status = DeliveryStatus.NOT_DELIVERED,
                type = MessageType.IN,
                text = "First Message"
            ),
            Message(
                id = "2",
                date = Instant.fromEpochSeconds(1667999139),
                read = ReadStatus.READ,
                status = DeliveryStatus.DELIVERED,
                type = MessageType.OUT,
                text = "Second Message"
            ),
            Message(
                id = "3",
                date = Instant.fromEpochSeconds(1667999139),
                read = ReadStatus.UNREAD,
                status = DeliveryStatus.DELIVERED,
                type = MessageType.RATING_IN,
                text = "Third Message"
            ),
            Message(
                id = "4",
                date = Instant.fromEpochSeconds(1667999139),
                read = ReadStatus.UNREAD,
                status = DeliveryStatus.DELIVERED,
                type = MessageType.RATING_OUT,
                text = "Fourth Message"
            ),
            Message(
                id = "5",
                date = Instant.fromEpochSeconds(1667999139),
                read = ReadStatus.UNREAD,
                status = DeliveryStatus.DELIVERED,
                type = MessageType.RATING_OUT,
                attachments = imageAttachments.plus(documentAttachments).toList()
            )
        )

}

@Preview(name = "Message Item")
@Composable
fun MessageItemPreview(
    @PreviewParameter(MessagePreviewParameterProvider::class) message: Message
) {
    MessageItem(message, onResend = {})
}
