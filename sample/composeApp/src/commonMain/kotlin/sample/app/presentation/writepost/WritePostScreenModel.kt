package sample.app.presentation.writepost

import com.mertcaliskanyurek.cmpbootstrap.presentation.NavigationEvent
import com.mertcaliskanyurek.cmpbootstrap.presentation.ScreenModelBase
import kotlin.random.Random
import sample.app.data.model.LocalPost
import sample.app.domain.SaveLocalPostUseCase

data class WritePostState(
    val title: String = "",
    val body: String = "",
    val attachedFileUri: String? = null,
    val attachedFileName: String? = null,
    val isSaving: Boolean = false
)

sealed class WritePostEvent {
    data class TitleChanged(val title: String) : WritePostEvent()
    data class BodyChanged(val body: String) : WritePostEvent()
    data class FileAttached(val uri: String, val name: String) : WritePostEvent()
    data object Save : WritePostEvent()
    data object NavigateBack : WritePostEvent()
}

class WritePostScreenModel(
    private val saveLocalPostUseCase: SaveLocalPostUseCase
) : ScreenModelBase<WritePostState, WritePostEvent>(WritePostState()) {

    override fun handleUIEvent(event: WritePostEvent) {
        when (event) {
            is WritePostEvent.TitleChanged -> updateState { it.copy(title = event.title) }
            is WritePostEvent.BodyChanged -> updateState { it.copy(body = event.body) }
            is WritePostEvent.FileAttached -> updateState {
                it.copy(attachedFileUri = event.uri, attachedFileName = event.name)
            }
            WritePostEvent.Save -> save()
            WritePostEvent.NavigateBack -> emitNavigationEvent(NavigationEvent.NavigateBack)
        }
    }

    private fun save() {
        val current = uiState.value
        if (current.title.isBlank()) return
        safeLaunch {
            updateState { it.copy(isSaving = true) }
            val post = LocalPost(
                id = Random.nextLong().toString(),
                title = current.title.trim(),
                body = current.body.trim(),
                attachedFileUri = current.attachedFileUri,
                attachedFileName = current.attachedFileName
            )
            saveLocalPostUseCase(post)
            emitNavigationEvent(NavigationEvent.NavigateBack)
        }
    }
}
