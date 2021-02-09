package com.codedevtech.mycardv2.di

import javax.inject.Qualifier

class AnnotationQualifiers {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CardDataSource

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OtherDataSources
}