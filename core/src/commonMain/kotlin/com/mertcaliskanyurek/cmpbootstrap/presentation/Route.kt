package com.mertcaliskanyurek.cmpbootstrap.presentation

interface Route {
    fun getScreen(): ScreenBase<*, *, *>
}