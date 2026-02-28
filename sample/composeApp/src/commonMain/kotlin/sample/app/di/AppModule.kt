package sample.app.di

import com.mertcaliskanyurek.bootstrap.networking.ApiClient
import com.mertcaliskanyurek.bootstrap.networking.HttpClientConfig
import com.mertcaliskanyurek.bootstrap.networking.HttpLogLevel
import org.koin.dsl.module
import sample.app.data.repository.PostRepository
import sample.app.domain.GetPostCommentsUseCase
import sample.app.domain.GetPostUseCase
import sample.app.domain.GetPostsUseCase
import sample.app.presentation.postdetail.PostDetailScreenModel
import sample.app.presentation.postlist.PostListScreenModel

val appModule = module {
    single {
        ApiClient.create(
            HttpClientConfig(
                baseUrl = "https://jsonplaceholder.typicode.com/",
                logLevel = HttpLogLevel.BODY,
                retryCount = 1
            )
        )
    }

    single { PostRepository(get()) }

    factory { GetPostsUseCase(get()) }
    factory { GetPostUseCase(get()) }
    factory { GetPostCommentsUseCase(get()) }

    factory { params -> PostDetailScreenModel(params.get(), get(), get()) }
    factory { PostListScreenModel(get()) }
}
