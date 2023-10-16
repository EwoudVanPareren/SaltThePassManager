package nl.vanparerensoftwaredevelopment.saltthepassmanager.common.screens.about

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.Platform
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.ui.AppIcons
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.ui.components.WithVerticalScrollbarOnDesktop
import nl.vanparerensoftwaredevelopment.saltedpassmanager.resources.MR

/**
 * A screen that displays information about the app.
 */
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
class AboutScreen: Screen {
    @Composable
    override fun Content() {
        val scrollState = rememberScrollState()

        Scaffold(
            topBar = {
                val navigator = LocalNavigator.current!!
                TopAppBar(
                    title = { Text(stringResource(MR.strings.about_screen_title)) },
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator.pop() }
                        ) { Icon(AppIcons.ArrowBack, contentDescription = stringResource(MR.strings.generic_back)) }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                WithVerticalScrollbarOnDesktop(scrollState) { paddingValues ->
                    Inner(
                        scrollState = scrollState,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }

    @Composable
    private fun Inner(
        scrollState: ScrollState,
        modifier: Modifier = Modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = modifier
                .padding(10.dp)
                .verticalScroll(scrollState)
        ) {
            Text(stringResource(MR.strings.about_text))
            Text(stringResource(MR.strings.about_license_copyright))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LinkButton(
                    label = stringResource(MR.strings.about_link_repository_label),
                    url = stringResource(MR.strings.about_link_repository_url),
                    icon = painterResource(MR.images.icon_github)
                )

                LinkButton(
                    label = stringResource(MR.strings.about_link_saltthepass_label),
                    url = stringResource(MR.strings.about_link_saltthepass_url)
                )
            }
        }
    }

    @Composable
    private fun LinkButton(
        url: String,
        label: String,
        icon: Painter? = null
    ) {
        val urlOpener = Platform.getOpenUrlInBrowserMethod()

        Button(
            onClick = {
                urlOpener(url)
            }
        ) {
            icon?.let { icon ->
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier
                        .height(28.dp)
                        .padding(end = 10.dp)
                )
            }
            Text(
                label,
                fontSize = LocalTextStyle.current.fontSize * 1.25,
                modifier = Modifier.padding(end = 5.dp)
            )
            Icon(
                AppIcons.OpenInNew,
                contentDescription = null,
                modifier = Modifier.size(18.dp).align(Alignment.Top)
            )
        }
    }
}