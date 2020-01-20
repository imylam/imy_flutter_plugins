package io.imyflutter.plugins.facebook

import androidx.annotation.NonNull
import com.facebook.login.LoginBehavior
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

class MethodCallHandlerImpl(activityPluginBinding: ActivityPluginBinding): MethodCallHandler {
    private val facebookLoginManager = FacebookLogInManager(activityPluginBinding)

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: MethodChannel.Result) {
        when (call.method) {
            Constants.METHOD_LOG_IN -> {
                val loginBehaviourStr: String = call.argument(Constants.ARG_LOG_IN_BEHAVIOUR)!!
                val permissions: List<String> = call.argument(Constants.ARG_PERMISSIONS)!!
                val loginBehaviour = translateLoginBehavior(loginBehaviourStr)

                facebookLoginManager.logIn(loginBehaviour, permissions, result)
            }

            Constants.METHOD_LOG_OUT -> {
                facebookLoginManager.logOut(result)
            }

            Constants.METHOD_GET_ACCESS_TOKEN -> {
                facebookLoginManager.getCurrentAccessToken(result)
            }

            else -> result.notImplemented()
        }
    }

    private fun translateLoginBehavior(loginBehavior: String): LoginBehavior {
        return when (loginBehavior) {
            Constants.LOGIN_BEHAVIOR_NATIVE_WITH_FALLBACK -> LoginBehavior.NATIVE_WITH_FALLBACK
            Constants.LOGIN_BEHAVIOR_NATIVE_ONLY -> LoginBehavior.NATIVE_ONLY
            Constants.LOGIN_BEHAVIOR_WEB_ONLY -> LoginBehavior.WEB_ONLY
            Constants.LOGIN_BEHAVIOR_WEB_VIEW_ONLY -> LoginBehavior.WEB_VIEW_ONLY
            else -> LoginBehavior.NATIVE_WITH_FALLBACK
        }
    }
}