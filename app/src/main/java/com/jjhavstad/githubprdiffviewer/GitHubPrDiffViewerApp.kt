package com.jjhavstad.githubprdiffviewer

import android.app.Application
import com.jjhavstad.githubprdiffviewer.datasources.apis.PrApiDataSource
import com.jjhavstad.githubprdiffviewer.datasources.apis.retrofit.RetrofitGitHubApiCreator
import com.jjhavstad.githubprdiffviewer.datasources.apis.retrofit.RetrofitPrApiDataSource
import com.jjhavstad.githubprdiffviewer.viewmodels.PrDiffViewModel
import com.jjhavstad.githubprdiffviewer.viewmodels.PrViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class GitHubPrDiffViewerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        inject()
    }

    private fun inject() {
        val appModule = module {
            single {
                RetrofitGitHubApiCreator.create(
                    baseUrl = "https://https://api.github.com/",
                    httpClientBuilder = OkHttpClient.Builder()
                )
            }
            single<PrApiDataSource> {
                RetrofitPrApiDataSource(get())
            }
            viewModel {
                PrViewModel(get())
            }
            viewModel {
                PrDiffViewModel(get())
            }
        }

        startKoin {
            androidLogger()
            androidContext(this@GitHubPrDiffViewerApp)
            modules(appModule)
        }
    }
}
