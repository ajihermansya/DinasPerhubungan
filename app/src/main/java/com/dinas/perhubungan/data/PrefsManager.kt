package com.dinas.perhubungan.data

import android.content.Context
import android.content.SharedPreferences
import hu.autsoft.krate.Krate
import hu.autsoft.krate.booleanPref
import hu.autsoft.krate.default.withDefault
import hu.autsoft.krate.stringPref


class PrefsManager(context: Context) : Krate {
    override val sharedPreferences: SharedPreferences =
        context.applicationContext.getSharedPreferences("pref_manager", Context.MODE_PRIVATE)

    var exampleBoolean by booleanPref().withDefault(false)
    var isExampleLogin by booleanPref().withDefault(false)
    var token by stringPref().withDefault("")
    var userEmail by stringPref().withDefault("")
    var isAdminLoggedIn by booleanPref().withDefault(false)

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

}