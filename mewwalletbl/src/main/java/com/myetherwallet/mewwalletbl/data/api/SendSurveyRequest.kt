package com.myetherwallet.mewwalletbl.data.api

data class SendSurveyRequest(
    val email: String,
    val message: String,
    val rating: Int
)