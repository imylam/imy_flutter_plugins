import 'dart:async';

import 'package:flutter/services.dart';

import 'package:facebook/constants.dart';
import 'package:facebook/facebookAccessToken.dart';
import 'package:facebook/facebookLoginResult.dart';

enum FacebookLoginBehaviour {
  nativeWithFallback,
  nativeOnly,
  webOnly,
  webViewOnly,
}

class Facebook {
  static const MethodChannel _channel = const MethodChannel(Constants.CHANNEL_NAME);

  final FacebookLoginBehaviour loginBehavior;

  Facebook({ this.loginBehavior });

  Future<bool> get isLoggedIn async =>
      (await currentAccessToken)?.isValid() ?? false;

  Future<FacebookAccessToken> get currentAccessToken async {
    final Map<dynamic, dynamic> accessTokenMap = await _channel.invokeMethod(Constants.METHOD_GET_ACCESS_TOKEN);

    if (accessTokenMap == null) {
      return null;
    }

    return FacebookAccessToken.fromMap(accessTokenMap.cast<String, dynamic>());
  }

  Future<FacebookLoginResult> logIn(List<String> permissions) async {
    final Map<dynamic, dynamic> result = await _channel.invokeMethod(Constants.METHOD_LOG_IN, {
      Constants.ARG_LOG_IN_BEHAVIOUR: loginBehavior.toString(),
      Constants.ARG_PERMISSIONS: permissions,
    });

    return FacebookLoginResult.fromMap(result.cast<String, dynamic>());
  }

  Future<void> logOut() async => _channel.invokeMethod('logOut');
}
