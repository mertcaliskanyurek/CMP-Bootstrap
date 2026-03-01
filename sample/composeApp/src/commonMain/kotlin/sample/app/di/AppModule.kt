package sample.app.di

import com.mertcaliskanyurek.bootstrap.datastore.DataStoreContext
import com.mertcaliskanyurek.bootstrap.datastore.KeyValueStorage
import com.mertcaliskanyurek.bootstrap.networking.ApiClient
import com.mertcaliskanyurek.bootstrap.networking.HttpClientConfig
import com.mertcaliskanyurek.bootstrap.networking.HttpLogLevel
import org.koin.dsl.module
import sample.app.data.repository.LocalPostsRepository
import sample.app.data.repository.PostRepository
import sample.app.data.repository.SavedPostsRepository
import sample.app.domain.GetPostCommentsUseCase
import sample.app.domain.GetPostUseCase
import sample.app.domain.GetPostsUseCase
import sample.app.domain.ObserveLocalPostsUseCase
import sample.app.domain.ObserveSavedPostsUseCase
import sample.app.domain.RemovePostUseCase
import sample.app.domain.SaveLocalPostUseCase
import sample.app.domain.SavePostUseCase
import sample.app.presentation.postdetail.PostDetailScreenModel
import sample.app.presentation.postlist.PostListScreenModel
import sample.app.presentation.savedposts.SavedPostsScreenModel
import sample.app.presentation.writepost.WritePostScreenModel

fun appModule(dataStoreContext: DataStoreContext) = module {
    single {
        ApiClient.create(
            HttpClientConfig(
                baseUrl = "https://jsonplaceholder.typicode.com/",
                logLevel = HttpLogLevel.BODY,
                retryCount = 1
            )
        )
    }

    single { KeyValueStorage.create(dataStoreContext) }

    single { PostRepository(get()) }
    single { SavedPostsRepository(get()) }
    single { LocalPostsRepository(get()) }

    factory { GetPostsUseCase(get()) }
    factory { GetPostUseCase(get()) }
    factory { GetPostCommentsUseCase(get()) }
    factory { ObserveSavedPostsUseCase(get()) }
    factory { SavePostUseCase(get()) }
    factory { RemovePostUseCase(get()) }
    factory { ObserveLocalPostsUseCase(get()) }
    factory { SaveLocalPostUseCase(get()) }

    factory { params -> PostDetailScreenModel(params.get(), get(), get(), get(), get(), get()) }
    factory { PostListScreenModel(get(), get()) }
    factory { SavedPostsScreenModel(get(), get()) }
    factory { WritePostScreenModel(get()) }
}
