
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun RowCompose(
    item: SammensattTekst,
    onClick: () -> Unit = {},
    isHorizontalFragment: Boolean = false
) {
    //Hvor mye en skal forsykve ikon, i landskap modus, når den er aktiv
    val openIndent: Dp = 14.dp
    val isOpen = item.state.value == SammensattTekstState.OPEN

    val titleSize = if (isOpen) 24.sp else 16.sp
    val iconSize = if (isOpen) 32.dp else 24.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.title,
                fontSize = titleSize,
                color = Color.Black
            )
        }

        if (!isHorizontalFragment) {
            Icon(
                imageVector = if (isOpen)
                    Icons.Filled.KeyboardArrowDown
                else
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(iconSize)
            )
        } else {
            Box(modifier = Modifier.wrapContentWidth(Alignment.End)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = if (isOpen)
                        Modifier.size(iconSize).offset(x = openIndent)
                    else
                        Modifier.size(iconSize)
                )
            }
        }
    }
}
