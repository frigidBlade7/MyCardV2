package com.codedevtech.mycardv2.di

import javax.inject.Qualifier

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AddedCardDataSource

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class PersonalCardDataSource
