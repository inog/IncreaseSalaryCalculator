package de.ingoreschke.increasesalarycalculator.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.ingoreschke.increasesalarycalculator.R

@Composable
fun NegotiationTipsDialog(
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = {
            Icon(
                imageVector = Icons.Default.TipsAndUpdates,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.dialog_info_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                TipItem(
                    title = stringResource(id = R.string.tip_1_title),
                    desc = stringResource(id = R.string.tip_1_desc)
                )
                Spacer(modifier = Modifier.height(12.dp))
                TipItem(
                    title = stringResource(id = R.string.tip_2_title),
                    desc = stringResource(id = R.string.tip_2_desc)
                )
                Spacer(modifier = Modifier.height(12.dp))
                TipItem(
                    title = stringResource(id = R.string.tip_3_title),
                    desc = stringResource(id = R.string.tip_3_desc)
                )
                Spacer(modifier = Modifier.height(12.dp))
                TipItem(
                    title = stringResource(id = R.string.tip_4_title),
                    desc = stringResource(id = R.string.tip_4_desc)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(id = R.string.action_close))
            }
        }
    )
}

@Composable
private fun TipItem(title: String, desc: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = desc,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
