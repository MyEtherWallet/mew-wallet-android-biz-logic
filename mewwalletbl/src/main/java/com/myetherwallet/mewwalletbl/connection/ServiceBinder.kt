package com.myetherwallet.mewwalletbl.connection

import android.app.Service
import android.os.Binder

/**
 * Created by BArtWell on 23.07.2019.
 */

class ServiceBinder<T : Service>(val service: T) : Binder()
