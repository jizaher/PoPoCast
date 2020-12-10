package tw.jizah.popocast.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import tw.jizah.popocast.R
import tw.jizah.popocast.model.Setting
import tw.jizah.popocast.ui.theme.Colors
import tw.jizah.popocast.ui.theme.Dimens

@Preview
@Composable
fun SettingPagePreview() {
    SettingPage(list = getSettingList(), onBackPressed = {
        // TODO: Ivan
    })
}

@Preview
@Composable
fun SwitchPreview() {
    SwitchSetting(
        setting = Setting.Switch(
            name = "Download using cellular",
            isOn = false,
            description = "Recommended Setting: Off"
        )
    )
}

@Composable
fun SettingPage(list: List<Setting>, onBackPressed: () -> Unit) {
    Column(modifier = Modifier.background(color = Colors.black)) {
        SettingToolbar(onBackPressed = onBackPressed)
        LazyColumnFor(items = list, modifier = Modifier.background(color = Colors.black)) {
            SettingItem(it)
        }
    }
}

@Composable
fun SettingToolbar(onBackPressed: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .preferredHeight(Dimens.toolBarHeight)
            .background(color = Colors.black)
    ) {
        Text(
            text = stringResource(id = R.string.settings),
            style = MaterialTheme.typography.h6,
            color = Colors.white,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().align(Alignment.Center)
        )
        IconButton(onClick = onBackPressed, modifier = Modifier.fillMaxHeight()) {
            Icon(imageVector = Icons.Filled.ArrowBack, tint = Colors.white)
        }
    }
}

@Composable
private fun SettingItem(setting: Setting) {
    when (setting) {
        is Setting.PlainText -> PlainText(setting)
        is Setting.Section -> Section(setting)
        is Setting.Switch -> SwitchSetting(setting)
    }
}

@Composable
private fun PlainText(setting: Setting.PlainText) {
    Column(modifier = Modifier.fillMaxWidth().padding(Dimens.m3)) {
        SettingName(name = setting.name)
        SettingDescription(description = setting.description)
    }
}

@Composable
private fun Section(setting: Setting.Section) {
    SettingName(
        name = setting.name,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(Dimens.m3)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SwitchSetting(setting: Setting.Switch) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(Dimens.m3)
    ) {
        Column(modifier = Modifier.weight(5F)) {
            SettingName(
                name = setting.name,
            )
            SettingDescription(
                description = setting.description,
            )
        }
        Switch(
            checked = setting.isOn,
            colors = SwitchConstants.defaultColors(
                checkedThumbColor = Colors.greenA700,
                checkedTrackColor = Colors.greenA700,
                uncheckedThumbColor = Colors.gray600,
                uncheckedTrackColor = Colors.gray600
            ),
            onCheckedChange = { /*TODO: Ivan */ },
            modifier = Modifier.weight(1F)
        )
    }
}

@Composable
private fun SettingName(name: String, modifier: Modifier = Modifier, fontWeight: FontWeight? = null) {
    Text(
        text = name,
        color = Colors.white,
        fontWeight = fontWeight,
        style = MaterialTheme.typography.body1,
        modifier = modifier
    )
}

@Composable
private fun SettingDescription(description: String, modifier: Modifier = Modifier) {
    Text(
        text = description,
        color = Colors.gray600,
        style = MaterialTheme.typography.body2,
        modifier = modifier
    )
}

private fun getSettingList(): List<Setting> {
    return listOf(
        Setting.Section(name = "Data Saver"),
        Setting.Switch(
            name = "Data Saver",
            isOn = false,
            description = "Sets your sound quality to low"
        ),
        Setting.Section(name = "Playback"),
        Setting.Switch(
            name = "Offline",
            isOn = false,
            description = "When you go offline, you'll only be able to play the podcasts you've downloaded."
        ),
        Setting.Switch(
            name = "Allow Explicit Content",
            isOn = true,
            description = "Turn on to play explicit content"
        ),
        Setting.Switch(
            name = "Autoplay",
            isOn = true,
            description = "Keep on listening to similar podcasts when your podcast ends."
        ),
        Setting.Section(name = "Audio Quality"),
        Setting.PlainText(name = "Equalizer", description = "Open the equalizer control panel."),
        Setting.Switch(
            name = "Download using cellular",
            isOn = false,
            description = "Recommended Setting: Off"
        ),
        Setting.Section(name = "About"),
        Setting.PlainText(name = "Version", description = "Alpha")
    )
}