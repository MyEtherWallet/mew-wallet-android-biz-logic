package com.myetherwallet.mewwalletbl.connection

import com.myetherwallet.mewwalletkit.core.extension.hexToBigInteger
import java.math.BigInteger
import java.security.spec.ECFieldFp
import java.security.spec.ECParameterSpec
import java.security.spec.ECPoint
import java.security.spec.EllipticCurve

/**
 * Created by BArtWell on 26.07.2019.
 */

object Secp256k1CurveTable {

    fun getParameterSpec(): ECParameterSpec {
        return ECParameterSpec(
            EllipticCurve(
                ECFieldFp("00fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2f".hexToBigInteger()),
                BigInteger.ZERO,
                BigInteger.valueOf(7),
                null
            ),
            ECPoint("79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798".hexToBigInteger(), "483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8".hexToBigInteger()),
            "fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141".hexToBigInteger(),
            1
        )
    }
}
