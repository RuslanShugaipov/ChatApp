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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ruslanshugaipov.chatapp.R
import com.ruslanshugaipov.chatapp.ui.theme.MessageListBackground
import com.ruslanshugaipov.chatapp.utils.AttachmentMeta
import com.ruslanshugaipov.chatapp.utils.formatBytes

@Composable
fun AttachmentPreview(meta: AttachmentMeta, onRemove: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .size(48.dp)
                .background(MessageListBackground)
        ) {
            if (meta.mimeType.startsWith("image")) {
                AsyncImage(
                    model = meta.contentUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    Icons.Outlined.Description,
                    contentDescription = stringResource(id = R.string.document_icon),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .height(48.dp)
                .padding(vertical = 6.dp)
                .weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = meta.originalName,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = formatBytes(meta.fileSize),
                style = MaterialTheme.typography.titleSmall
            )
        }
        IconButton(onClick = onRemove) {
            Icon(
                Icons.Filled.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}
