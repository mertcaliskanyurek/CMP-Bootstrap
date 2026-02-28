package sample.app.presentation.savedposts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import com.mertcaliskanyurek.cmpbootstrap.presentation.ScreenBase
import sample.app.data.model.Post

class SavedPostsScreen : ScreenBase<SavedPostsState, SavedPostsEvent, SavedPostsScreenModel> {

    @Composable
    override fun provideScreenModel() = koinScreenModel<SavedPostsScreenModel>()

    @Composable
    override fun ScreenContent(
        state: SavedPostsState,
        emitUIEvent: (SavedPostsEvent) -> Unit
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Saved Posts") },
                    navigationIcon = {
                        IconButton(onClick = { emitUIEvent(SavedPostsEvent.NavigateBack) }) {
                            Text("<")
                        }
                    }
                )
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                if (state.posts.isEmpty()) {
                    Text(
                        text = "No saved posts yet.\nOpen a post and tap \u2606 to save it.",
                        modifier = Modifier.align(Alignment.Center).padding(32.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.posts, key = { it.id }) { post ->
                            SavedPostItem(
                                post = post,
                                onClick = { emitUIEvent(SavedPostsEvent.OnPostClick(post.id)) },
                                onRemove = { emitUIEvent(SavedPostsEvent.RemovePost(post.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedPostItem(post: Post, onClick: () -> Unit, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = post.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = post.body,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = onRemove) {
                Text(
                    text = "\u2605",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
