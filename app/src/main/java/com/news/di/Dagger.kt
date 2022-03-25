package com.news.di

import com.news.ui.NewsActivity
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        RetrofitModule::class,
        AppModule::class,
        DatabaseModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
    ]
)
@Singleton
interface AppComponent {
    @Component.Builder
    interface Builder {

        fun appModule(appModule: AppModule): Builder

        fun build(): AppComponent
    }

    fun inject(activity: NewsActivity)
}