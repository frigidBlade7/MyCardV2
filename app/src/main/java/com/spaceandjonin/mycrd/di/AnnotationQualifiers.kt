package com.spaceandjonin.mycrd.di

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

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ImageFile

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class VcfFile

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HasNetwork