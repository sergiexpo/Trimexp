package com.neoland.trimexp.ddbb

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Db.getDatabase(this)
    }
}