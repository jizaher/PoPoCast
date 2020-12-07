package tw.jizah.popocast.model

import androidx.compose.ui.graphics.vector.ImageVector

data class OptionsCollection(
    val image: String,
    val title: String,
    val subtitle: String,
    val options: List<OptionItem>
)

data class OptionItem(
    val icon: ImageVector,
    val name: String
)