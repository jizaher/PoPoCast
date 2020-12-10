package tw.jizah.popocast.model

sealed class Setting(open val name: String) {
    data class Section(override val name: String): Setting(name)
    data class PlainText(override val name: String, val description: String): Setting(name)
    data class Switch(override val name: String, val isOn: Boolean, val description: String): Setting(name)
}