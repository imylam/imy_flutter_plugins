package io.imyflutter.plugins.facebook

import android.util.Log
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel


/** FacebookPlugin */
class FacebookPlugin : FlutterPlugin, ActivityAware  {
  private var channel: MethodChannel? = null
  private lateinit var messenger: BinaryMessenger

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    Log.d("hello", "onEngineAttached")
    messenger = flutterPluginBinding.flutterEngine.dartExecutor
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    Log.d("hello", "onEngineDetached")
    if(channel != null) {
      channel!!.setMethodCallHandler(null)
      channel = null
    }
  }

  override fun onAttachedToActivity(activityPluginBinding: ActivityPluginBinding) {
    Log.d("hello", "onActivityAttached")
    setUpChannel(activityPluginBinding)
  }

  override fun onReattachedToActivityForConfigChanges(p0: ActivityPluginBinding) {
    Log.d("hello", "onActivityConfigReattached")
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onDetachedFromActivityForConfigChanges() {
    Log.d("hello", "onActivityConfigDetached")
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onDetachedFromActivity() {
    Log.d("hello", "onActivityDetached")
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  private fun setUpChannel(activityBinding: ActivityPluginBinding) {
    channel = MethodChannel(messenger, Constants.CHANNEL_NAME)
    val handler = MethodCallHandlerImpl(activityBinding)
    channel!!.setMethodCallHandler(handler)
  }
}
