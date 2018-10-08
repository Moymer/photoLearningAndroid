package com.moymer.spoken.usercases.main.vision.category

import com.moymer.spoken.di.qualifiers.ActivityScoped
import com.moymer.spoken.di.qualifiers.FragmentScoped

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by gabriellins @ moymer
 * on 31/07/18.
 */
@Module
abstract class SCategoryVisionModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun sCategoryVisionFragment(): SCategoryVisionFragment

    @ActivityScoped
    @Binds
    abstract fun sCategoryVisionPresenter(presenter: SCategoryVisionPresenter): SCategoryVisionContract.Presenter

}