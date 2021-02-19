package com.codedevtech.mycardv2.di

import javax.inject.Qualifier

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AddedCardDataSource

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class PersonalCardDataSource


    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthService

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UpdateService
