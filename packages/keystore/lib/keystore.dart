import 'dart:async';

import 'package:flutter/services.dart';

class Keystore {
  static const MethodChannel _channel = const MethodChannel('keystore');
  static const String CHANNEL_NAME = "imy_keystore";
  static const String METHOD_PUBLIC_KEY = "getPublicKey";
  static const String METHOD_SIGN_DATA = "signData";
  static const String METHOD_VERIFY_SIGNATURE = "verifySignature";

  static Future<String> get getPublicKey async {
    final String publicKey = await _channel.invokeMethod(METHOD_PUBLIC_KEY);
    return publicKey;
  }

  static Future<String> signData(String data) async {
    final Map<String, dynamic> params = <String, dynamic> {
      'data': data,
    };

    final String signature = await _channel.invokeMethod(METHOD_SIGN_DATA, params);
    return signature;
  }

  static Future<bool> verifySignature(String data, signature) async {
    final Map<String, dynamic> params = <String, dynamic> {
      'data': data,
      'signature': signature,
    };

    final bool isSignatureValid = await _channel.invokeMethod(METHOD_VERIFY_SIGNATURE, params);
    return isSignatureValid;
  }
}
