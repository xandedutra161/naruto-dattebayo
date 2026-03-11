package com.example.dattebayoapp

import android.app.Application
import com.example.dattebayoapp.core.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DattebayoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DattebayoApplication)
            modules(appModules)
        }
    }
}
