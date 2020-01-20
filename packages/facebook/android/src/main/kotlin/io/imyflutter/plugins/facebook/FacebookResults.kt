package io.imyflutter.plugins.facebook

import com.facebook.AccessToken
import com.facebook.FacebookException

class FacebookResults {

    companion object {

        fun loginCancelled(): Map<String, String> {
            return mapOf(
                    Constants.RESP_STATUS_KEY to Constants.RESP_STATUS_CANCELLED
            )
        }

        fun loginSuccess(accessToken: AccessToken): Map<String, Any> {
            return mapOf(
                    Constants.RESP_STATUS_KEY to Constants.RESP_STATUS_LOGGED_IN,
                    Constants.RESP_ACCESS_TOKEN to mapAccessToken(accessToken)
            )
        }

        fun loginError(error: FacebookException): Map<String, String> {
            return mapOf(
                    Constants.RESP_STATUS_KEY to Constants.RESP_STATUS_ERROR,
                    Constants.RESP_ERROR_MSG to error.message!!
            )
        }

        fun accessToken(accessToken: AccessToken?): Map<String, Any>? {
            return if (accessToken == null) {
                null
            } else {
                return mapAccessToken(accessToken)
            }
        }

        private fun mapAccessToken(accessToken: AccessToken) : Map<String, Any> {
            return mapOf(
                    Constants.FB_TOKEN to accessToken.token,
                    Constants.FB_USER_ID to accessToken.userId,
                    Constants.FB_EXPIRES to accessToken.expires,
                    Constants.FB_PERMISSIONS to accessToken.permissions,
                    Constants.FB_DECLINE_PERMISSIONS to accessToken.declinedPermissions
            )
        }
    }
}