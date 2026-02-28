package sample.app.presentation.postdetail

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import com.mertcaliskanyurek.cmpbootstrap.presentation.ScreenBase
import org.koin.core.parameter.parametersOf
import sample.app.data.model.Comment
import sample.app.data.model.Post

class PostDetailScreen(
    private val postId: Int
) : ScreenBase<PostDetailState, PostDetailEvent, PostDetailScreenModel> {

    override val key: ScreenKey = "PostDetailScreen_$postId"

    @Composable
    override fun provideScreenModel() =
        koinScreenModel<PostDetailScreenModel>(parameters = { parametersOf(postId) })

    @Composable
    override fun ScreenContent(
        state: PostDetailState,
        emitUIEvent: (PostDetailEvent) -> Unit
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Post #$postId") },
                    navigationIcon = {
                        IconButton(onClick = { emitUIEvent(PostDetailEvent.NavigateBack) }) {
                            Text("<")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                if (state.isSaved) emitUIEvent(PostDetailEvent.RemovePost)
                                else emitUIEvent(PostDetailEvent.SavePost)
                            }
                        ) {
                            Text(
                                text = if (state.isSaved) "\u2605" else "\u2606",
                                style = MaterialTheme.typography.titleLarge,
                                color = if (state.isSaved) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface
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
                        Button(onClick = { emitUIEvent(PostDetailEvent.Retry) }) {
                            Text("Retry")
                        }
                    }
                    else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                        state.post?.let { post ->
                            item { PostDetailHeader(post = post) }
                        }
                        if (state.comments.isNotEmpty()) {
                            item {
                                HorizontalDivider()
                                Text(
                                    text = "Comments (${state.comments.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            items(state.comments) { comment ->
                                CommentItem(comment = comment)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PostDetailHeader(post: Post) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = post.title, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = post.body, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun CommentItem(comment: Comment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = comment.name, style = MaterialTheme.typography.labelLarge)
            Text(text = comment.email, style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = comment.body, style = MaterialTheme.typography.bodySmall)
        }
    }
}
