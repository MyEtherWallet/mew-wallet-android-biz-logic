package com.myetherwallet.mewwalletbl.data

import org.webrtc.SessionDescription

/**
 * Created by BArtWell on 24.07.2019.
 */

data class Offer(
    val type: String?,
    val sdp: String?
) : BaseMessage() {
    constructor(sessionDescription: SessionDescription) : this(sessionDescription.type.canonicalForm(), sessionDescription.description)

    fun toSessionDescription() = SessionDescription(SessionDescription.Type.fromCanonicalForm(type), sdp)
}
