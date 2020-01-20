package io.imyflutter.plugins.facebook

import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

import io.flutter.plugin.common.MethodChannel

class FacebookLogInManager(private val activityBinding: ActivityPluginBinding) {
    private val callbackManager: CallbackManager = CallbackManager.Factory.create()
    private val loginManager: LoginManager = LoginManager.getInstance()
    private val facebookLoginCallback: FacebookLoginCallback = FacebookLoginCallback(callbackManager)

    init {
        activityBinding.addActivityResultListener(facebookLoginCallback)
        loginManager.registerCallback(callbackManager, facebookLoginCallback)
    }

    fun logIn(loginBehavior: LoginBehavior, permissions: List<String>, result: MethodChannel.Result) {
        facebookLoginCallback.setLoginInProgress(result)
        loginManager.loginBehavior = loginBehavior
        loginManager.logIn(activityBinding.activity, permissions)
    }

    fun logOut(result: MethodChannel.Result) {
        loginManager.logOut()
        result.success(null)
    }

    fun getCurrentAccessToken(result: MethodChannel.Result) {
        val accessToken: AccessToken = AccessToken.getCurrentAccessToken()
        result.success(FacebookResults.accessToken(accessToken))
    }

}