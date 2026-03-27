package sample.app.presentation.writepost

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import com.mertcaliskanyurek.bootstrap.media.MediaPickerLauncher
import com.mertcaliskanyurek.bootstrap.media.picker.rememberMediaPickerContext
import com.mertcaliskanyurek.bootstrap.media.picker.rememberMediaPickerLauncher
import com.mertcaliskanyurek.cmpbootstrap.presentation.ScreenBase

class WritePostScreen : ScreenBase<WritePostState, WritePostEvent, WritePostScreenModel> {

    @Composable
    override fun provideScreenModel() = koinScreenModel<WritePostScreenModel>()

    @Composable
    override fun ScreenContent(
        state: WritePostState,
        emitUIEvent: (WritePostEvent) -> Unit
    ) {
        val mediaContext = rememberMediaPickerContext()

        val galleryLauncher = rememberMediaPickerLauncher(
            context = mediaContext,
            config = MediaPickerLauncher.visualMediaPicker()
        ) { files ->
            files.firstOrNull()?.let { file ->
                emitUIEvent(WritePostEvent.FileAttached(file.uri, file.name))
            }
        }

        val fileLauncher = rememberMediaPickerLauncher(
            context = mediaContext,
            config = MediaPickerLauncher.filePicker()
        ) { files ->
            files.firstOrNull()?.let { file ->
                emitUIEvent(WritePostEvent.FileAttached(file.uri, file.name))
            }
        }

        var showMenu by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("New Post") },
                    navigationIcon = {
                        IconButton(onClick = { emitUIEvent(WritePostEvent.NavigateBack) }) {
                            Text("←", style = MaterialTheme.typography.titleLarge)
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.title,
                    onValueChange = { emitUIEvent(WritePostEvent.TitleChanged(it)) },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = state.body,
                    onValueChange = { emitUIEvent(WritePostEvent.BodyChanged(it)) },
                    label = { Text("Content") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box {
                    OutlinedButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (state.attachedFileName != null)
                                "File: ${state.attachedFileName}"
                            else
                                "Add File"
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Pick from gallery") },
                            onClick = {
                                showMenu = false
                                galleryLauncher.launch()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Pick from files") },
                            onClick = {
                                showMenu = false
                                fileLauncher.launch()
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { emitUIEvent(WritePostEvent.Save) },
                    enabled = state.title.isNotBlank() && !state.isSaving,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (state.isSaving) "Saving…" else "Save")
                }
            }
        }
    }
}
