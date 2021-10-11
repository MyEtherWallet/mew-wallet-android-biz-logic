package com.myetherwallet.mewwalletbl

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.myetherwallet.mewwalletbl.core.persist.database.Database
import com.myetherwallet.mewwalletbl.preference.Preferences
import com.myetherwallet.mewwalletbl.util.NetworkHandler

/**
 * Created by BArtWell on 17.07.2019.
 */

internal class MewWalletBlProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        Database.init(context!!)
        Preferences.init(context!!)
        NetworkHandler.init(context!!)
        return true
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        throw NotImplementedError("Not implemented")
    }

    override fun query(p0: Uri, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor? {
        throw NotImplementedError("Not implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        throw NotImplementedError("Not implemented")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        throw NotImplementedError("Not implemented")
    }

    override fun getType(p0: Uri): String? {
        throw NotImplementedError("Not implemented")
    }
}

