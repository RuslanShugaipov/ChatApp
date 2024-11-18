package com.ruslanshugaipov.chatapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.chat2desk.chat2desk_sdk.domain.entities.Attachment
import com.chat2desk.chat2desk_sdk.domain.entities.DeliveryStatus
import com.ruslanshugaipov.chatapp.R
import com.ruslanshugaipov.chatapp.ui.theme.MessageListBackground
import com.ruslanshugaipov.chatapp.utils.formatBytes
import com.ruslanshugaipov.chatapp.utils.statusIcon

@Composable
fun ImageAttachment(attachment: Attachment) {
    Box(contentAlignment = Alignment.Center) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(attachment.link)
                .crossfade(true)
                .build(),
            contentDescription = attachment.originalFileName,
            loading = {
                CircularProgressIndicator()
            },
            contentScale = ContentScale.Crop,
        )
        if (attachment.status != DeliveryStatus.DELIVERED) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    statusIcon(attachment.status),
                    contentDescription = null,
                    tint = if (attachment.status == DeliveryStatus.NOT_DELIVERED) MaterialTheme.colorScheme.error else Color.White,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}


@Composable
fun DocumentAttachment(attachment: Attachment) {
    Row {
        Card(
            shape = MaterialTheme.shapes.medium,
        ) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .size(48.dp)
                    .background(MessageListBackground)
            ) {
                Icon(
                    if (attachment.status != DeliveryStatus.DELIVERED) statusIcon(attachment.status) else Icons.Outlined.Description,
                    contentDescription = stringResource(id = R.string.document_icon),
                    tint = if (attachment.status == DeliveryStatus.NOT_DELIVERED) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            Modifier
                .height(48.dp)
                .padding(vertical = 6.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = attachment.originalFileName ?: "",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = formatBytes(attachment.fileSize),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}


val imageAttachments = sequenceOf(
    Attachment(
        id = 1,
        fileSize = 123,
        contentType = "image/jpeg",
        link = "http://placekitten.com/200/300",
        originalFileName = "kitten.jpeg",
        status = DeliveryStatus.DELIVERED
    ),
    Attachment(
        id = 2,
        fileSize = 123,
        contentType = "image/jpeg",
        link = "http://placekitten.com/200/300",
        originalFileName = "kitten.jpeg",
        status = DeliveryStatus.SENDING
    ),
    Attachment(
        id = 3,
        fileSize = 123,
        contentType = "image/jpeg",
        link = "http://placekitten.com/200/300",
        originalFileName = "kitten.jpeg",
        status = DeliveryStatus.NOT_DELIVERED
    ),
    Attachment(
        id = 4,
        fileSize = 123,
        contentType = "image/jpeg",
        link = "http://placekitten.com/200/300",
        originalFileName = "kitten.jpeg",
        status = DeliveryStatus.SENT
    )
)

val documentAttachments = sequenceOf(
    Attachment(
        id = 1,
        fileSize = 123,
        contentType = "application/pdf",
        link = "http://placekitten.com/200/300",
        originalFileName = "kitten.pdf",
        status = DeliveryStatus.DELIVERED
    ),
    Attachment(
        id = 2,
        fileSize = 123,
        contentType = "application/pdf",
        link = "http://placekitten.com/200/300",
        originalFileName = "kitten.pdf",
        status = DeliveryStatus.NOT_DELIVERED
    ),
    Attachment(
        id = 3,
        fileSize = 123,
        contentType = "application/pdf",
        link = "http://placekitten.com/200/300",
        originalFileName = "kitten.pdf",
        status = DeliveryStatus.SENDING
    ),
    Attachment(
        id = 4,
        fileSize = 123,
        contentType = "application/pdf",
        link = "http://placekitten.com/200/300",
        originalFileName = "kitten.pdf",
        status = DeliveryStatus.SENT
    )
)

class ImagePreviewParameterProvider : PreviewParameterProvider<Attachment> {
    override val values: Sequence<Attachment> = imageAttachments
}

class DocumentPreviewParameterProvider : PreviewParameterProvider<Attachment> {
    override val values: Sequence<Attachment> = documentAttachments
}

@Preview
@Composable
fun ImageAttachmentPreview(
    @PreviewParameter(ImagePreviewParameterProvider::class) attachment: Attachment
) {
    ImageAttachment(attachment = attachment)
}

@Preview
@Composable
fun DocumentAttachmentPreview(
    @PreviewParameter(DocumentPreviewParameterProvider::class) attachment: Attachment
) {
    DocumentAttachment(attachment = attachment)
}
