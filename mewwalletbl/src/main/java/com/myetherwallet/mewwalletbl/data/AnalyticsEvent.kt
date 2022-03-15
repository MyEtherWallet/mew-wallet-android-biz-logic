package com.myetherwallet.mewwalletbl.data

import com.myetherwallet.mewwalletbl.preference.Preferences
import java.math.BigDecimal
import java.util.*

private const val BLOCKCHAIN = "{blockchain}"

data class AnalyticsEvent internal constructor(
    val id: String,
    val timestamp: Date
) {

    constructor(id: Id, timestamp: Date = Date()) : this(id, null, timestamp)

    constructor(id: Id, blockchain: Blockchain? = null, timestamp: Date = Date()) : this(
        if (id.value.contains(BLOCKCHAIN)) id.value.replace(BLOCKCHAIN, blockchain?.symbol ?: Preferences.persistent.getBlockchain().symbol, true) else id.value,
        timestamp
    )

    enum class Id(val value: String) {
        MAIN_ADD_ACCOUNT_CLICKED("Android-Main-AddAccount-clicked"),
        MAIN_RECEIVE_BIG_CLICKED("Android-Main-Receive-big-clicked"),
        MAIN_BUY_BIG_CLICKED("Android-Main-Buy-big-clicked"),
        MAIN_GUIDE_CLICKED("Android-Main-WhatIsEthereum-clicked"),
        MAIN_KEY_CORRUPTED("Android-KeyCorrupted-Authorization"),
        MAIN_BALANCE_ABOVE_THRESHOLD("Android-General-Balance-Above-Threshold"),
        GENERATING_KEY_CORRUPTED_POPUP_SHOWN("Android-KeyCorrupted-CreateWallet-Popup-shown"),
        GENERATING_KEY_INVALID_MNEMONIC("Android-CreateWallet-MnemonicCheckout-failed"),
        MAIN_KEY_RESTORED("Android-KeyRestored"),
        MAIN_KEY_CORRUPTED_POPUP_SHOWN("Android-KeyCorrupted-Authorization-Popup-shown"),
        ACCOUNT_DETAILS_RECEIVE_CLICKED("Android-AccountDetails-Receive-clicked"),
        ACCOUNT_DETAILS_BUY_CLICKED("Android-AccountDetails-Buy-clicked"),
        ACCOUNT_DETAILS_TOKEN_INFO_SWAP_CLICKED("Android-AccountDetails-TokenPopupSwap-clicked"),
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
        RATER_DONT_SHOW_CLICKED("Android-Rater-DontShow-clicked"),
        RATER_CLOSED("Android-Rater-closed"),
        CAMERA_VALID_MEWCONNECT_QR("Android-camera-valid-mewconnect-QRcode-scanned"),
        CAMERA_VALID_ADDRESS_QR("Android-camera-valid-eth-address-QRcode-scanned"),
        CAMERA_INVALID_QR("Android-camera-invalid-QRcode-scanned"),
        CAMERA_VALID_QR_CONNECTION_FAILED("Android-camera-valid-mewconnect-QRcode-scanned-connection-failed"),
        CAMERA_VALID_QR_CONNECTION_SUCCESS("Android-camera-valid-mewconnect-QRcode-scanned-connection-success"),
        CAMERA_VALID_QR_CONNECTING("Android-camera-valid-mewconnect-QRcode-scanned-connecting"),
        EXCHANGE_MARKET_TOKEN_CLICKED("Android-ExchangeScreen-TokenMarket-clicked"),
        EXCHANGE_TOKEN_INFO_SWAP_CLICKED("Android-ExchangeScreen-TokenPopupSwap-clicked"),

        EXCHANGE_SWAP_CLICKED("Android-$BLOCKCHAIN-ExchangeScreen-Swap-clicked"),
        EXCHANGE_BUY_ETH_CLICKED("Android-$BLOCKCHAIN-ExchangeScreen-Buy-clicked"),
        WALLET_MAINSCREEN_SHOWN("Android-Wallet-MainScreen-Shown"),
        SWAP_SHOWN("Android-$BLOCKCHAIN-Swap-MainScreen-shown"),
        SWAP_LOW_LIQUIDITY("Android-$BLOCKCHAIN-Swap-MainScreen-LowLiquidity-shown"),
        SWAP_MAX_CLICKED("Android-$BLOCKCHAIN-Swap-SwapTokensScreen-Max-clicked"),
        SWAP_FIRST_TOKEN_CLICKED("Android-$BLOCKCHAIN-Swap-MainScreen-FirstFieldToken-clicked"),
        SWAP_SECOND_TOKEN_CLICKED("Android-$BLOCKCHAIN-Swap-MainScreen-SecondFieldToken-clicked"),
        SWAP_WANTED_TOKEN_SHOWN("Android-$BLOCKCHAIN-Swap-MainScreen-TokenYouWantToGetScreen-shown"),
        SWAP_WANTED_TOKEN_SEARCH_CLICKED("Android-$BLOCKCHAIN-Swap-MainScreen-TokenYouWantToGetScreen-SearchIcon-clicked"),
        SWAP_WANTED_TOKEN_CLICKED("Android-$BLOCKCHAIN-Swap-MainScreen-TokenYouWantToGetScreen-Token-clicked"),
        SWAP_LOW_BALANCE_SHOWN("Android-$BLOCKCHAIN-Swap-MainScreen-LowBalance-shown"),
        SWAP_FIND_BEST_RATE_CLICKED("Android-$BLOCKCHAIN-Swap-MainScreen-FindBestRate-clicked"),
        SWAP_PROVIDER_SHOWN("Android-$BLOCKCHAIN-Swap-SelectProviderScreen-shown"),
        SWAP_PROVIDER_SELECTED("Android-$BLOCKCHAIN-Swap-SelectProviderScreen-provider-selected"),
        SWAP_VERIFY_SHOWN("Android-$BLOCKCHAIN-Swap-VerifySwapScreen-shown"),
        SWAP_VERIFY_PROCEED_SWAP("Android-$BLOCKCHAIN-Swap-VerifySwapScreen-ProceedWithSwap-clicked"),
        SWAP_INITIATED_SHOWN("Android-$BLOCKCHAIN-Swap-SwapInitiatedPopup-shown"),
        SWAP_SUCCESS_EXECUTED("Android-$BLOCKCHAIN-Swap-successfully-executed"),
        SWAP_VERIFY_FEE_DISCLAIMER_CLICKED("Android-$BLOCKCHAIN-Swap-VerifySwapScreen-FeeDisclaimer-Clicked"),
        SWAP_VERIFY_CHOOSE_FEE_DISCLAIMER_CLICKED("Android-$BLOCKCHAIN-Swap-ChooseFeeOverlay-FeeDisclaimer-Clicked"),
        SWAP_MAINSCREEN_SHOWN("Android-Swap-MainScreen-shown"),

        EXCHANGE_SWAP2_CLICKED("Android-$BLOCKCHAIN-ExchangeScreen-Swap2-Clicked"),
        SWAP2_SHOWN("Android-$BLOCKCHAIN-Swap2-MainScreen-Shown"),
        SWAP2_MAX_CLICKED("Android-$BLOCKCHAIN-Swap2-MainScreen-Max-Clicked"),

        SWAP2_FIRST_TOKEN_CLICKED("Android-$BLOCKCHAIN-Swap2-MainScreen-FirstToken-Clicked"),
        SWAP2_TOKEN_SWAP_SHOWN("Android-$BLOCKCHAIN-Swap2-TokenToSwapScreen-Shown"),
        SWAP2_TOKEN_SWAP_SELECTED("Android-$BLOCKCHAIN-Swap2-MainScreen-TokenToSwapScreen-token-Selected"),

        SWAP2_SECOND_TOKEN_CLICKED("Android-$BLOCKCHAIN-Swap2-MainScreen-SecondToken-Clicked"),
        SWAP2_TOKEN_RECEIVE_SHOWN("Android-$BLOCKCHAIN-Swap2-TokenToReceiveScreen-Shown"),
        SWAP2_TOKEN_RECEIVE_SELECTED("Android-$BLOCKCHAIN-Swap2-MainScreen-TokenToReceiveScreen-token-Selected"),

        SWAP2_SWITCH_CLICKED("Android-$BLOCKCHAIN-Swap2-MainScreen-Switch-Clicked"),
        SWAP2_UNAVAILABLE("Android-$BLOCKCHAIN-Swap2-MainScreen-UnavailablePair-Shown"),
        SWAP2_FIND_BEST_OFFER_CLICKED("Android-$BLOCKCHAIN-Swap2-MainScreen-FindBestOffer-Clicked"),
        SWAP2_LOOKING_FOR_BEST_OFFER_SHOWN("Android-$BLOCKCHAIN-Swap2-LookingForBestOfferPopup-Shown"),
        SWAP2_NO_OFFERS("Android-$BLOCKCHAIN-Swap2-LookingForBestOfferPopup-NoOffers"),
        SWAP2_ACCOUNT_CLICKED("Android-$BLOCKCHAIN-Swap2-MainScreen-SelectAccount-Clicked"),
        SWAP2_TOKEN_SWAP_SEARCH_CLICKED("Android-$BLOCKCHAIN-Swap2-TokenToSwapScreen-Search-Clicked"),
        SWAP2_TOKEN_RECEIVE_SEARCH_CLICKED("Android-$BLOCKCHAIN-Swap2-TokenToReceiveScreen-Search-Clicked"),
        SWAP2_ALL_OFFERS_SHOWN("Android-$BLOCKCHAIN-Swap2-AllOffersScreen-Shown"),
        SWAP2_OFFER_SELECTED("Android-$BLOCKCHAIN-Swap2-AllOffersScreen-offer-Selected"),
        SWAP2_BEST_OFFER_SHOWN("Android-$BLOCKCHAIN-Swap2-BestOfferScreen-Shown"),
        SWAP2_LOW_BALANCE_SHOWN("Android-$BLOCKCHAIN-Swap2-BestOfferScreen-LowBalance-Shown"),
        SWAP2_BAD_RATE_SHOWN("Android-$BLOCKCHAIN-Swap2-BestOfferScreen-BadRate-Shown"),
        SWAP2_BEST_OFFER_PROCEED("Android-$BLOCKCHAIN-Swap2-BestOfferScreen-ProceedWithSwap-Clicked"),
        SWAP2_INITIATED_SHOWN("Android-$BLOCKCHAIN-Swap2-SwapInitiatedPopup-Shown"),
        SWAP2_SUCCESS_EXECUTED("Android-$BLOCKCHAIN-Swap2-successfully-Executed"),

        BUY_AMOUNTS_LIST_SHOWN("Android-$BLOCKCHAIN-Buy-AmountsList-Shown"),
        BUY_CUSTOM_AMOUNT_SHOWN("Android-$BLOCKCHAIN-Buy-CustomAmount-Shown"),
        BUY_SIMPLEX_PAGE_SHOWN("Android-$BLOCKCHAIN-Buy-SimplexPage-Shown"),

        PUSH_NOTIFICATION_RECEIVED("Android-broadcast-push-notification-received"),
        COMIC_MAIN_CLICKED("Android-Main-Comic-banner-clicked"),
        COMIC_EDUCATION_CLICKED("Android-Education-center-Comic-banner-clicked"),
        COMIC_SHARE("Android-Comic-Share-clicked"),
        CORRUPTED_KEYS_REPORT("Android-Corrupted-Keys-Report"),
        APPLICATION_LAUNCHED_WITH_SAMSUNG_STORAGE("Android-App-launchedWithSamsungStorage"),
        APPLICATION_LAUNCHED_WITH_MEW_STORAGE("Android-App-launchedWithMewStorage"),
        APPLICATION_LAUNCHED_IN_DARK_MODE("Android-App-launchedInDarkMode"),
        APPLICATION_LAUNCHED_IN_LIGHT_MODE("Android-App-launchedInLightMode"),

        MAIN_TRANSACTION_CLICKED("Android-Main-transaction-Clicked"),
        ACCOUNT_DETAILS_TRANSACTION_CLICKED("Android-AccountDetails-transaction-history-Clicked"),
        TRANSACTION_DETAILS_SHOWN("Android-TransactionDetailsScreen-Shown"),
        TRANSACTION_DETAILS_SPEEDUP_CLICKED("Android-TransactionDetailsScreen-SpeedUp-Clicked"),
        TRANSACTION_DETAILS_CANCEL_CLICKED("Android-TransactionDetailsScreen-Cancel-Clicked"),
        SPEEDUP_UPDATE_CLICKED("Android-SpeedUpScreen-UpdateTransaction-Clicked"),
        CANCEL_TRANSACTION_ATTEMPT_CLICKED("Android-CancelTransactionScreen-AttemptToCancel-Clicked"),
        SPEEDUP_TRANSACTION_SUCCESS_UPDATED("Android-SpeedUpTransaction-SuccessfullyUpdated"),
        CANCEL_TRANSACTION_SUCCESS_REPLACED("Android-CancelTransaction-SuccessfullyReplaced"),

        EARN_MAINSCREEN_SHOWN("Android-EARN-MainScreen-Shown"),
        EARN_MAINSCREEN_ETH2BANNER_CLICKED("Android-EARN-MainScreen-Eth2Banner-Clicked"),
        EARN_ETH2_INFOSCREEN_MOREABOUTRISKS_CLICKED("Android-EARN-Eth2-InfoScreen-MoreAboutRisks-Clicked"),
        EARN_ETH2_INFOSCREEN_STARTSTAKING_CLICKED("Android-EARN-Eth2-InfoScreen-StartStaking-Clicked"),
        EARN_ETH2_STAKEONETH2SCREEN_SHOWN("Android-EARN-Eth2-StakeOnEth2Screen-Shown"),
        EARN_ETH2_STAKEONETH2SCREEN_ENABLESTAKING_CLICKED("Android-EARN-Eth2-StakeOnEth2Screen-EnableStaking-Clicked"),
        EARN_ETH2_STAKEONETH2SCREEN_STAKEETH_CLICKED("Android-EARN-Eth2-StakeOnEth2Screen-StakeETH-Clicked"),
        EARN_ETH2_STAKEONETH2SCREEN_INPUTFIELD_CLICKED("Android-EARN-Eth2-StakeOnEth2Screen-InputField-Clicked"),
        EARN_ETH2_STAKEONETH2SCREEN_READYTOSTAKE_SHOWN("Android-EARN-Eth2-StakeOnEth2Screen-ReadyToStake-Shown"),
        EARN_ETH2_STAKEONETH2SCREEN_BUYETHER_CLICKED("Android-EARN-Eth2-StakeOnEth2Screen-BuyEther-Clicked"),
        EARN_ETH2_STAKEONETH2SCREEN_SWAP_CLICKED("Android-EARN-Eth2-StakeOnEth2Screen-Swap-Clicked"),
        EARN_ETH2_STAKINGFEEOVERLAY_SHOWN("Android-EARN-Eth2-StakingFeeOverlay-Shown"),
        EARN_ETH2_ONEWAYDISCLAIMER_SHOWN("Android-EARN-Eth2-OneWayDisclaimer-Shown"),
        EARN_ETH2_ONEWAYDISCLAIMER_CONFIRMCLICKED("Android-EARN-Eth2-OneWayDisclaimer-ConfirmClicked"),
        EARN_ETH2_ONEWAYDISCLAIMER_CANCELCLICKED("Android-EARN-Eth2-OneWayDisclaimer-CancelClicked"),
        EARN_ETH2_VERIFYSCREEN_CONFIRMANDSTAKE_CLICKED("Android-EARN-Eth2-VerifyScreen-ConfirmAndStake-Clicked"),
        EARN_ETH2_VERIFYSCREEN_SEND_TRANSACTION_SUCCESS("Android-EARN-Eth2-VerifyScreen-SendTransaction-Success"),
        EARN_ETH2_VERIFYSCREEN_SEND_TRANSACTION_FAIL("Android-EARN-Eth2-VerifyScreen-SendTransaction-Fail"),
        EARN_ETH2_STAKEONETH2SCREEN_STAKEMORE_CLICKED("Android-EARN-Eth2-StakeOnEth2Screen-StakeMore-Clicked"),
        EARN_ETH2_STAKEONETH2SCREEN_CANIWITHDRAW_CLICKED("Android-EARN-Eth2-StakeOnEth2Screen-CanIWithdraw-Clicked"),
        EARN_ETH2_STAKEONETH2SCREEN_YOUWILLNEEDTOENABLEAGAINALERT_SHOWN("Android-EARN-Eth2-StakeOnEth2Screen-YouWillNeedToEnableAgainAlert-Shown"),

        DAPP_MAINSCREEN_SHOWN("Android-Dapp-MainScreen-Shown"),
        DAPP_BROWSER_LAUNCHED("Android-Dapp-Browser-Launched"),

        MARKET_MAINSCREEN_SHOWN("Android-Market-MainScreen-Shown"),
        MARKET_ALLTOKENS_SELECTED("Android-Market-AllTokens-Selected"),
        MARKET_OVERVIEW_BIGMOVERS_CLICKED("Android-Market-Overview-BigMovers-Clicked"),
        MARKET_OVERVIEW_TOPPERIOD_CLICKED("Android-Market-Overview-TopPeriod-Clicked"),
        MARKET_OVERVIEW_TOPGAINERS_CLICKED("Android-Market-Overview-TopGainers-Clicked"),
        MARKET_OVERVIEW_TOPLOSERS_CLICKED("Android-Market-Overview-TopLosers-Clicked"),
        MARKET_TOKEN_INFO_SWAP_CLICKED("Android-Market-TokenPopupSwap-Clicked"),

        SEND_INPUT_FEE_DISCLAIMER_CLICKED("Android-SendInputScreen-FeeDisclaimer-Clicked"),
        SEND_CHOOSE_FEE_DISCLAIMER_CLICKED("Android-Send-ChooseFeeOverlay-FeeDisclaimer-Clicked"),

        DAPP_MAIN_BANNER_CLICKED("Android-DAPP-MainScreen-Banner-Clicked"),

        ERROR_SWAP_SAVE_PURCHASE_HISTORY("Android-Error-Swap-SavePurchaseHistory-failed"),

        SURVEY_SHOWN("Android-SurveyBottomSheet-Shown"),
        SURVEY_GO_TO_CLICKED("Android-SurveyBottomSheet-GoTo-Clicked"),
        SURVEY_LATER_CLICKED("Android-SurveyBottomSheet-Later-Clicked"),
        SURVEY_DO_NOT_SHOW_CLICKED("Android-SurveyBottomSheet-DoNotShow-Clicked"),

        EXCHANGE_BINANCE_CLICKED("Android-$BLOCKCHAIN-ExchangeScreen-BinanceBridge-Clicked"),
        BINANCE_ACTIVEBRIDGESCREEN_SHOWN("Android-$BLOCKCHAIN-Binance-ActiveBridgesScreen-Shown"),
        BINANCE_MAINSCREEN_SHOWN("Android-$BLOCKCHAIN-Binance-MainScreen-Shown"),
        BINANCE_MOVETOBSCSCREEN_MAX_CLICKED("Android-$BLOCKCHAIN-Binance-MainScreen-Max-Clicked"),
        BINANCE_MOVETOBSCSCREEN_TOKEN_CLICKED("Android-$BLOCKCHAIN-Binance-MainScreen-FieldToken-Clicked"),
        BINANCE_WANTED_TOKEN_SHOWN("Android-$BLOCKCHAIN-Binance-MainScreen-TokenYouWantToMoveScreen-Shown"),
        BINANCE_SUGGESTSWAP_SHOWN("Android-$BLOCKCHAIN-Binance-MainScreen-TokenYouWantToMoveScreen-SuggestSwapPopup-Shown"),
        BINANCE_MOVETOBSCSCREEN_NEXT_CLICKED("Android-$BLOCKCHAIN-Binance-MainScreen-Next-Clicked"),
        BINANCE_VERIFY_SHOWN("Android-$BLOCKCHAIN-Binance-VerifyTransferScreen-Shown"),
        BINANCE_VERIFY_ERROR("Android-Binance-VerifyTransferScreen-CreateBridge-Error"),
        BINANCE_GOTBNB_SHOWN("Android-Binance-GotBnbScreen-Shown"),
        BINANCE_VERIFY_PROCEED_MOVE("Android-$BLOCKCHAIN-Binance-VerifyTransferScreen-ProceedWithTransfer-Clicked"),
        BINANCE_INITIATED_SHOWN("Android-$BLOCKCHAIN-Binance-TransferInitiatedPopup-Shown"),
        BINANCE_INITIATED_SEND_TRANSACTION_SUCCESS("Android-$BLOCKCHAIN-Binance-TransferInitiatedPopup-SendTransaction-Success"),
        BINANCE_INITIATED_SEND_TRANSACTION_FAIL("Android-$BLOCKCHAIN-Binance-TransferInitiatedPopup-SendTransaction-Fail"),
        BINANCE_DEPOSITDETAILS_SHOWN("Android-$BLOCKCHAIN-Binance-DepositDetailsScreen-Shown"),

        BLOCKCHAIN_MAIN_SELECTOR_CLICKED("Android-Main-BlockchainSelector-Clicked"),
        BLOCKCHAIN_SWAP_SELECTOR_CLICKED("Android-Swap-BlockchainSelector-Clicked"),
        BLOCKCHAIN_MARKETS_SELECTOR_CLICKED("Android-Market-BlockchainSelector-Clicked"),
        BLOCKCHAIN_EARN_SELECTOR_CLICKED("Android-Earn-BlockchainSelector-Clicked"),
        BLOCKCHAIN_BROWSER_SELECTOR_CLICKED("Android-DAPP-BlockchainSelector-Clicked"),
        BROWSER_MAINSCREEN_SHOWN("Android-Browser-MainScreen-Shown"),

        BLOCKCHAIN_MAIN_CHANGED("Android-Main-Blockchain-Changed-$BLOCKCHAIN"),
        BLOCKCHAIN_SWAP_CHANGED("Android-Swap-Blockchain-Changed-$BLOCKCHAIN"),
        BLOCKCHAIN_MARKETS_CHANGED("Android-Market-Blockchain-Changed-$BLOCKCHAIN"),
        BLOCKCHAIN_EARN_CHANGED("Android-Earn-Blockchain-Changed-$BLOCKCHAIN"),
        BLOCKCHAIN_BROWSER_CHANGED("Android-DAPP-Blockchain-Changed-$BLOCKCHAIN"),

        ERROR_AWS_TOO_MANY_REQUESTS("Android-Error-AWS-TooManyRequests"),

        YEARN_MAINSCREEN_YEARNBANNER_CLICKED("Android-Yearn-MainScreen-YearnBanner-Clicked"),
        YEARN_BALANCESCREEN_MOREABOUTRISKS_CLICKED("Android-Yearn-BalanceScreen-MoreAboutRisks-Clicked"),

        YEARN_DEPOSITAMOUNTSCREEN_SHOWN("Android-Yearn-DepositAmountScreen-Shown"),
        YEARN_DEPOSITAMOUNTSCREEN_MAX_CLICKED("Android-Yearn-DepositAmountScreen-Max-Clicked"),
        YEARN_DEPOSITAMOUNTSCREEN_DEPOSIT_CLICKED("Android-Yearn-DepositAmountScreen-Deposit-Clicked"),
        YEARN_DEPOSITAMOUNTSCREEN_GETMORETOKEN_CLICKED("Android-Yearn-DepositAmountScreen-GetMoreToken-Clicked"),

        YEARN_DEPOSITVERIFYSCREEN_SHOWN("Android-Yearn-DepositVerifyScreen-Shown"),
        YEARN_DEPOSITVERIFYSCREEN_ERROR("Android-Yearn-DepositVerifyScreen-CreateTx-Error"),
        YEARN_DEPOSITVERIFYSCREEN_CONFIRM_CLICKED("Android-Yearn-DepositVerifyScreen-Confirm-Clicked"),
        YEARN_DEPOSIT_INITIATED_SHOWN("Android-Yearn-DepositInitiatedPopup-Shown"),
        YEARN_DEPOSIT_INITIATED_SEND_SUCCESS("Android-Yearn-DepositInitiatedPopup-SendTransaction-Success"),

        YEARN_WITHDRAWAMOUNTSCREEN_SHOWN("Android-Yearn-WithdrawAmountScreen-Shown"),
        YEARN_WITHDRAWAMOUNTSCREEN_MAX_CLICKED("Android-Yearn-WithdrawAmountScreen-Max-Clicked"),
        YEARN_WITHDRAWAMOUNTSCREEN_HALF_CLICKED("Android-Yearn-WithdrawAmountScreen-Half-Clicked"),
        YEARN_WITHDRAWAMOUNTSCREEN_QUARTER_CLICKED("Android-Yearn-WithdrawAmountScreen-Quarter-Clicked"),
        YEARN_WITHDRAWAMOUNTSCREEN_WITHDRAW_CLICKED("Android-Yearn-WithdrawAmountScreen-Withdraw-Clicked"),

        YEARN_WITHDRAWVERIFYSCREEN_SHOWN("Android-Yearn-WithdrawVerifyScreen-Shown"),
        YEARN_WITHDRAWVERIFYSCREEN_CONFIRM_CLICKED("Android-Yearn-WithdrawVerifyScreen-Confirm-Clicked"),
        YEARN_WITHDRAWVERIFYSCREEN_BUY_CLICKED("Android-Yearn-WithdrawVerifyScreen-Buy-Clicked"),
        YEARN_WITHDRAWVERIFYSCREEN_ERROR("Android-Yearn-WithdrawVerifyScreen-CreateTx-Error"),
        YEARN_WITHDRAW_INITIATED_SHOWN("Android-Yearn-WithdrawInitiatedPopup-Shown"),
        YEARN_WITHDRAW_INITIATED_SEND_SUCCESS("Android-Yearn-WithdrawInitiatedPopup-SendTransaction-Success"),

        LIDO_MAINSCREEN_LIDOBANNER_CLICKED("Android-Lido-MainScreen-LidoBanner-Clicked"),
        LIDO_BALANCESCREEN_MOREABOUTRISKS_CLICKED("Android-Lido-BalanceScreen-MoreAboutRisks-Clicked"),
        LIDO_BALANCESCREEN_TRANSACTION_DETAILS_CLICKED("Android-Lido-BalanceScreen-TransactionDetails-Clicked"),
        LIDO_BALANCESCREEN_TOKENINFO_CLICKED("Android-Lido-BalanceScreen-TokenInfo-Clicked"),

        LIDO_AMOUNTSCREEN_SHOWN("Android-Lido-AmountScreen-Shown"),
        LIDO_AMOUNTSCREEN_MAX_CLICKED("Android-Lido-AmountScreen-Max-Clicked"),
        LIDO_AMOUNTSCREEN_BUYETHER_CLICKED("Android-Lido-AmountScreen-BuyEther-Clicked"),
        LIDO_AMOUNTSCREEN_REVIEW_CLICKED("Android-Lido-AmountScreen-Review-Clicked"),

        LIDO_VERIFYSCREEN_SHOWN("Android-Lido-VerifyScreen-Shown"),
        LIDO_VERIFYSCREEN_CREATE_TX_ERROR("Android-Lido-VerifyScreen-CreateTx-Error"),
        LIDO_VERIFYSCREEN_NOT_ENOUGH("Android-Lido-VerifyScreen-NotEnoughEther"),
        LIDO_VERIFYSCREEN_CONFIRM_CLICKED("Android-Lido-VerifyScreen-Confirm-Clicked"),
        LIDO_SEND_INITIATED_SHOWN("Android-Lido-SendInitiatedPopup-Shown"),
        LIDO_SEND_INITIATED_SUCCESS("Android-Lido-SendInitiatedPopup-SendTransaction-Success"),
        LIDO_SEND_INITIATED_FAIL("Android-Lido-SendInitiatedPopup-SendTransaction-Fail")
    }

    companion object {
        fun createConnectingDappEvent(dapp: String? = null) = when (dapp) {
            null -> AnalyticsEvent(Id.CAMERA_VALID_QR_CONNECTING)
            else -> AnalyticsEvent("Android-MEWconnect-$dapp-connecting", Date())
        }

        fun createSuccessDappEvent(dapp: String? = null) = when (dapp) {
            null -> AnalyticsEvent(Id.CAMERA_VALID_QR_CONNECTION_SUCCESS)
            else -> AnalyticsEvent("Android-MEWconnect-$dapp-connected", Date())
        }

        fun createFailDappEvent(dapp: String? = null) = when (dapp) {
            null -> AnalyticsEvent(Id.CAMERA_VALID_QR_CONNECTION_FAILED)
            else -> AnalyticsEvent("Android-MEWconnect-$dapp-failed", Date())
        }

        fun createClickDappEvent(dapp: String) = AnalyticsEvent("Android-DAPP-Featured-${dapp.format()}-Clicked", Date())

        fun createSpeedUpClickTypeEvent(type: String) = AnalyticsEvent("Android-SpeedUpScreen-$type-speed-Clicked", Date())

        fun createClickMarketBannerEvent(name: String?) = AnalyticsEvent("Android-Market-OverviewBanner-${name?.format()}-Clicked", Date())

        fun createClickMarketCollectionTokenEvent(symbol: String) = AnalyticsEvent("Android-Market-Collection-${symbol.format()}-token-Clicked", Date())

        fun createBnbAmountSelectedEvent(amount: BigDecimal) = AnalyticsEvent("Android-Binance-GotBnbScreen-${amount.toPlainString()}BNB-Selected", Date())

        fun createClickYearnDeposit(symbol: String) = AnalyticsEvent("Android-Yearn-BalanceScreen-Deposit-${symbol.format()}-Clicked", Date())

        fun String?.format() = this?.uppercase(Locale.US)?.replace(" ", "_")
    }
}
