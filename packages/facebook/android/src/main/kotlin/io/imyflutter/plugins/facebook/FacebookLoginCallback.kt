package io.imyflutter.plugins.facebook

import android.content.Intent
import android.util.Log

import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult

import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry


class FacebookLoginCallback (private val callbackManager: CallbackManager)
    : FacebookCallback<LoginResult>, PluginRegistry.ActivityResultListener {

    private var result: MethodChannel.Result? = null

    override fun onSuccess(loginResult: LoginResult) {
        Log.d("success", loginResult.accessToken.toString())
        onResponseReady(FacebookResults.loginSuccess(loginResult.accessToken))
    }

    override fun onError(error: FacebookException) {
        Log.d("error", error.toString())
        onResponseReady(FacebookResults.loginError(error))
    }

    override fun onCancel() {
        Log.d("error", "cancel")
        onResponseReady(FacebookResults.loginCancelled())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun onResponseReady(input: Any?) {
        if (this.result != null) {
            this.result!!.success(input)
            this.result = null
        }
    }

    fun setLoginInProgress(result: MethodChannel.Result) {
        if (this.result != null) {
            this.result!!.error(Constants.ERROR_LOGIN_IN_PROGRESS, Constants.ERROR_LOGIN_IN_PROGRESS_MSG, null)
            return
        }

        this.result = result
    }
}