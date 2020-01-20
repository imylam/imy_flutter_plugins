package io.imyflutter.plugins.facebook

object Constants {
    const val CHANNEL_NAME = "imy_facebook"
    const val METHOD_LOG_IN = "login"
    const val METHOD_LOG_OUT = "logout"
    const val METHOD_GET_ACCESS_TOKEN = "getAccessToken"

    const val ARG_LOG_IN_BEHAVIOUR = "loginBehaviour"
    const val ARG_PERMISSIONS = "permissions"

    const val ERROR_LOGIN_IN_PROGRESS = "loginInProgress"
    const val ERROR_LOGIN_IN_PROGRESS_MSG = "cannot login when another login process is in progress"

    const val LOGIN_BEHAVIOR_NATIVE_WITH_FALLBACK = "nativeWithFallback"
    const val LOGIN_BEHAVIOR_NATIVE_ONLY = "nativeOnly"
    const val LOGIN_BEHAVIOR_WEB_ONLY = "webOnly"
    const val LOGIN_BEHAVIOR_WEB_VIEW_ONLY = "webViewOnly"

    const val RESP_ACCESS_TOKEN = "accessToken"
    const val RESP_ERROR_MSG = "errorMsg"
    const val RESP_STATUS_KEY = "status"
    const val RESP_STATUS_CANCELLED = "cancelled"
    const val RESP_STATUS_ERROR = "error"
    const val RESP_STATUS_LOGGED_IN = "loggined"

    const val FB_TOKEN = "token"
    const val FB_USER_ID = "userId"
    const val FB_EXPIRES = "expires"
    const val FB_PERMISSIONS = "permissions"
    const val FB_DECLINE_PERMISSIONS = "declinedPermissions"
}