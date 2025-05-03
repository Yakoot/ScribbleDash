package dev.mamkin.scribbledash.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.mamkin.scribbledash.R
import dev.mamkin.scribbledash.domain.Rating
import dev.mamkin.scribbledash.ui.theme.OnBackground
import dev.mamkin.scribbledash.ui.theme.OnBackgroundVariant

@Composable
fun ColumnScope.RatingText(
    rating: Rating = Rating.OOPS,
) {
    val oopsArray: Array<String> = stringArrayResource(R.array.oops_array)
    val goodArray: Array<String> = stringArrayResource(R.array.good_array)
    val woohooArray: Array<String> = stringArrayResource(R.array.woohoo_array)
    val title = rating.getTitle()
    val subtitle = when (rating) {
        Rating.OOPS, Rating.MEH -> oopsArray.random()
        Rating.GREAT, Rating.GOOD -> goodArray.random()
        Rating.WOOHOO -> woohooArray.random()
    }
    Text(
        text = title,
        style = MaterialTheme.typography.headlineLarge,
        color = OnBackground
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
        textAlign = TextAlign.Center,
        text = subtitle,
        style = MaterialTheme.typography.bodyMedium,
        color = OnBackgroundVariant
    )
}