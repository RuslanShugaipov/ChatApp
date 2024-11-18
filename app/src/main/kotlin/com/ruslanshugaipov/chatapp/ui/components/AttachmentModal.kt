package com.ruslanshugaipov.chatapp.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Theaters
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ruslanshugaipov.chatapp.R

@Composable
fun AttachmentModal(onSelect: (uri: Uri?) -> Unit) {
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia(), onSelect)

    val pickDocument =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument(), onSelect)

    Column(modifier = Modifier.padding(10.dp)) {
        ListItem(
            modifier = Modifier.clickable {
                pickMedia.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageAndVideo
                    )
                )
            },
            headlineContent = { Text(stringResource(id = R.string.select_media_attachment)) },
            leadingContent = {
                Icon(
                    Icons.Filled.Theaters,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            })
        HorizontalDivider()
        ListItem(
            modifier = Modifier.clickable { pickDocument.launch(arrayOf("*/*")) },
            headlineContent = { Text(stringResource(id = R.string.select_file_attachment)) },
            leadingContent = {
                Icon(
                    Icons.Filled.Description,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
    }
}
