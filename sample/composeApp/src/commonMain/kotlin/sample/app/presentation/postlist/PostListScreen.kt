package sample.app.presentation.postlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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

class PostListScreen : ScreenBase<PostListState, PostListEvent, PostListScreenModel> {

    @Composable
    override fun provideScreenModel() = koinScreenModel<PostListScreenModel>()

    @Composable
    override fun ScreenContent(
        state: PostListState,
        emitUIEvent: (PostListEvent) -> Unit
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Posts") },
                    actions = {
                        IconButton(onClick = { emitUIEvent(PostListEvent.NavigateToSavedPosts) }) {
                            Text(
                                text = "\u2605",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                when {
                    state.isLoading -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                    state.error != null -> Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { emitUIEvent(PostListEvent.LoadPosts) }) {
                            Text("Retry")
                        }
                    }
                    else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.posts) { post ->
                            PostItem(
                                post = post,
                                onClick = { emitUIEvent(PostListEvent.OnPostClick(post.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PostItem(post: Post, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = post.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = post.body,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
