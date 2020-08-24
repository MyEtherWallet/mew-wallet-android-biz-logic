package com.myetherwallet.mewwalletbl.data

import java.util.*

data class AnalyticsEvent internal constructor(
    val id: String,
    val timestamp: Date
) {

    constructor(id: Id, timestamp: Date = Date()) : this(id.value, timestamp)

    enum class Id(val value: String) {
        MAIN_ADD_ACCOUNT_CLICKED("Android-Main-AddAccount-clicked"),
        MAIN_RECEIVE_BIG_CLICKED("Android-Main-Receive-big-clicked"),
        MAIN_BUY_BIG_CLICKED("Android-Main-Buy-big-clicked"),
        MAIN_GUIDE_CLICKED("Android-Main-WhatIsEthereum-clicked"),
        MAIN_KEY_CORRUPTED("Android-KeyCorrupted-Authorization"),
        GENERATING_KEY_CORRUPTED_POPUP_SHOWN("Android-KeyCorrupted-CreateWallet-Popup-shown"),
        MAIN_KEY_RESTORED("Android-KeyRestored"),
        MAIN_KEY_CORRUPTED_POPUP_SHOWN("Android-KeyCorrupted-Authorization-Popup-shown"),
        ACCOUNT_DETAILS_RECEIVE_CLICKED("Android-AccountDetails-Receive-clicked"),
        ACCOUNT_DETAILS_BUY_CLICKED("Android-AccountDetails-Buy-clicked"),
        ADD_ACCOUNT_ADD_CLICKED("Android-AddAccount-Add-clicked"),
        LINK_POPUP_SHOWN("Android-LinkPopup-shown"),
        LINK_POPUP_LINK_CLICKED("Android-LinkPopup-Link-clicked"),
        LINK_POPUP_IMPORT_MEWCONNECT_CLICKED("Android-LinkPopup-ImportMEWconnect-clicked"),
        LINK_POPUP_FRESH_WALLET_CLICKED("Android-LinkPopup-FreshWallet-clicked"),
        MEWCONNECT_POPUP_SHOWN("Android-MEWconnectPopup-shown"),
        MEWCONNECT_POPUP_IMPORT_CLICKED("Android-MEWconnectPopup-import-clicked"),
        MEWCONNECT_POPUP_IMPORT_SUCCESS("Android-MEWconnectPopup-import-success"),
        MEWCONNECT_POPUP_FRESHWALLET_CLICKED("Android-MEWconnectPopup-FreshWallet-clicked"),
        RATER_SHOWN("Android-Rater-Shown"),
        RATER_FEEDBACK_CLICKED("Android-Rater-Feedback-clicked"),
        RATER_RATE_CLICKED("Android-Rater-Rate-clicked"),
        RATER_CLOSED("Android-Rater-closed"),
        CAMERA_VALID_MEWCONNECT_QR("Android-camera-valid-mewconnect-QRcode-scanned"),
        CAMERA_VALID_ADDRESS_QR("Android-camera-valid-eth-address-QRcode-scanned"),
        CAMERA_INVALID_QR("Android-camera-invalid-QRcode-scanned"),
        CAMERA_VALID_QR_CONNECTION_FAILED("Android-camera-valid-mewconnect-QRcode-scanned-connection-failed"),
        CAMERA_VALID_QR_CONNECTION_SUCCESS("Android-camera-valid-mewconnect-QRcode-scanned-connection-success"),
        EXCHANGE_SWAP_CLICKED("Android-ExchangeScreen-Swap-clicked"),
        EXCHANGE_BUY_ETH_CLICKED("Android-ExchangeScreen-BuyEth-clicked"),
        EXCHANGE_MARKET_TOKEN_CLICKED("Android-ExchangeScreen-TokenMarket-clicked"),
        EXCHANGE_TOKEN_INFO_SWAP_CLICKED("Android-ExchangeScreen-TokenPopupSwap-clicked"),
        SWAP_SHOWN("Android-Swap-MainScreen-shown"),
        SWAP_MAX_CLICKED("Android-Swap-SwapTokensScreen-Max-clicked"),
        SWAP_FIRST_TOKEN_CLICKED("Android-Swap-MainScreen-FirstFieldToken-clicked"),
        SWAP_SECOND_TOKEN_CLICKED("Android-Swap-MainScreen-SecondFieldToken-clicked"),
        SWAP_WANTED_TOKEN_SHOWN("Android-Swap-MainScreen-TokenYouWantToGetScreen-shown"),
        SWAP_WANTED_TOKEN_SEARCH_CLICKED("Android-Swap-MainScreen-TokenYouWantToGetScreen-SearchIcon-clicked"),
        SWAP_WANTED_TOKEN_CLICKED("Android-Swap-MainScreen-TokenYouWantToGetScreen-Token-clicked"),
        SWAP_FIND_BEST_RATE_CLICKED("Android-Swap-MainScreen-FindBestRate-clicked"),
        SWAP_PROVIDER_SHOWN("Android-Swap-SelectProviderScreen-shown"),
        SWAP_PROVIDER_SELECTED("Android-Swap-SelectProviderScreen-provider-selected"),
        SWAP_VERIFY_SHOWN("Swap-VerifySwapScreen-shown"),
        SWAP_VERIFY_PROCEED_SWAP("Android-Swap-VerifySwapScreen-ProceedWithSwap-clicked"),
        SWAP_INITIATED_SHOWN("Android-Swap-SwapInitiatedPopup-shown"),
        SWAP_SUCCESS_EXECUTED("Android-Swap-successfully-executed"),
        PUSH_NOTIFICATION_RECEIVED("Android-broadcast-push-notification-received"),
        COMIC_MAIN_CLICKED("Android-Main-Comic-banner-clicked"),
        COMIC_EDUCATION_CLICKED("Android-Education-center-Comic-banner-clicked"),
        COMIC_SHARE("Android-Comic-Share-clicked"),

        ERROR_SWAP_SAVE_PURCHASE_HISTORY("Android-Error-Swap-SavePurchaseHistory-failed")
    }

    companion object {
        fun createDappEvent(dapp: String? = null) = when (dapp) {
            null -> AnalyticsEvent(Id.CAMERA_VALID_QR_CONNECTION_SUCCESS)
            else -> AnalyticsEvent("Android-MEWconnect-$dapp-connected", Date())
        }
    }
}
