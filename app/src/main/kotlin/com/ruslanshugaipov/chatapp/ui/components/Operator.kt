package com.ruslanshugaipov.chatapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.chat2desk.chat2desk_sdk.domain.entities.Operator
import com.ruslanshugaipov.chatapp.R

@Composable
fun Operator(operator: Operator?, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        SubcomposeAsyncImage(
            model = operator?.avatar,
            contentDescription = null,
            contentScale = ContentScale.None,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        ) {
            val state = painter.state
            if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                Box(modifier = Modifier.size(30.dp), contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            } else {
                SubcomposeAsyncImageContent(contentScale = ContentScale.Crop)
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Top) {
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = operator?.name
                    ?: stringResource(id = R.string.waiting_operator),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (operator?.typing == true) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(id = R.string.operator_typing),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
