package io.imyflutter.plugins.keystore

import android.util.Base64
import android.util.Log
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

class KeystorePlugin: MethodCallHandler {
  companion object {
    private const val CHANNEL_NAME = "imy_keystore"
    private const val METHOD_PUBLIC_KEY = "getPublicKey"
    private const val METHOD_SIGN_DATA = "signData"
    private const val METHOD_VERIFY_SIGNATURE = "verifySignature"

    private lateinit var registrar: Registrar
    private lateinit var rsaKeyStoreWrapper: RsaKeyStoreKeyWrapper

    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "keystore")
      val instance: KeystorePlugin = KeystorePlugin(registrar)
      channel.setMethodCallHandler(instance)
    }
  }

  constructor(reg: Registrar) {
    registrar = reg
    rsaKeyStoreWrapper = RsaKeyStoreKeyWrapper(registrar.activeContext())
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    try {
      when (call.method) {
        METHOD_PUBLIC_KEY -> {
          val kp = rsaKeyStoreWrapper.getAndroidKeyStoreAsymmetricKeyPair()
          result.success(Base64.encodeToString(kp?.public?.encoded, Base64.DEFAULT))

        }
        METHOD_SIGN_DATA -> {
          val data: String? = call.argument("data")
          if (data != null) {
            result.success(rsaKeyStoreWrapper.sign(data))
          }
        }
        METHOD_VERIFY_SIGNATURE -> {
          val data: String? = call.argument("data")
          val signature: String? = call.argument("signature")
          if (data != null && signature != null) {
            result.success(rsaKeyStoreWrapper.verify(data, signature))
          }
        }
        else -> result.notImplemented()
      }
    } catch (e: Exception) {
      Log.e("imy_keystore", e.message)
      result.error("imy_keystore", e.message, e)
    }
  }
}
