package com.example.playlistmaker.dagger_di.component

import android.app.Application
import android.content.Context
import com.example.playlistmaker.dagger_di.annotations.ApplicationScope
import com.example.playlistmaker.dagger_di.module.DataModule
import com.example.playlistmaker.dagger_di.module.ViewmodelModule
import com.example.playlistmaker.ui.ViewModelFactory
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewmodelModule::class
    ]
)

interface ApplicationComponent {
    fun getViewModelFactory(): ViewModelFactory

    @Component.Factory
    interface Factory {
        fun create (
            @BindsInstance context:Context,
            @BindsInstance application: Application
        ):ApplicationComponent
    }
}