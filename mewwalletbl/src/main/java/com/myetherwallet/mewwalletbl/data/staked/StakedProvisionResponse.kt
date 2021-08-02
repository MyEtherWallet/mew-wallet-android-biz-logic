package com.myetherwallet.mewwalletbl.data.staked

import com.google.gson.annotations.SerializedName

/**
 * Created by BArtWell on 02.12.2020.
 */

data class StakedProvisionResponse(
    @SerializedName("provisioning_request_uuid")
    val uuid: String
)
//{"id":341,"uuid":"959208fa-c984-4482-a1d9-800d6c88cd6f","created":"2020-12-02T14:31:45","status":"CREATED","chain":"ETH2","user_id":null,"partner_id":"44",
//    "attributes":{"validators":[{"count":1,"provider":"decentralized"}]
//    ,"withdrawalKey":"78657de16886a86202bfc718f6684b829b09e28f3e9a51fffb1233032419ab3944330755e14310d41e0ecce948d1077b","withdrawalCredentials":null}}
