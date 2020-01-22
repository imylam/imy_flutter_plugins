import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';

import 'package:facebook/constants.dart';
import 'package:facebook/facebook.dart';
import 'package:facebook/facebookAccessToken.dart';
import 'package:facebook/facebookLoginResult.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  Facebook _facebook;
  FacebookLoginResult _loginResult;
  FacebookAccessToken _accessToken;

  @override
  void initState() {
    super.initState();
    _facebook = Facebook();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> facebookLogin() async {
    FacebookLoginResult result;
    FacebookAccessToken accessToken;

    try {
      result = await _facebook.logIn(["email"]);

      if(result.status == Constants.RESP_STATUS_LOGGED_IN) {
        accessToken = result.accessToken;
      }
    } on PlatformException {
      result = null;
    }

    if (!mounted) return;

    setState(() {
      _loginResult = result;
      _accessToken = accessToken;
    });
  }

//  Future<void> update() async {
//    String output;
//    // Platform messages may fail, so we use a try/catch PlatformException.
//    try {
//      FacebookAccessToken token = await _facebook.currentAccessToken;
//      output = token.token;
//    } on PlatformException {
//      output = 'Failed';
//    }
//
//    // If the widget was removed from the tree while the asynchronous platform
//    // message was in flight, we want to discard the reply rather than calling
//    // setState to update our non-existent appearance.
//    if (!mounted) return;
//
//    setState(() {
//      _channelMsg = output;
//    });
//  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Facebook Login Example App'),
        ),
        body: Center(
          child: _loginResult!=null ? Column(
            children: <Widget>[
              Text('Token: ' + _accessToken.token),
              Text('User ID: ' + _accessToken.userId),
              Text('Expires: ' + _accessToken.expires.toIso8601String()),
            ],
          ) : RaisedButton(
            onPressed: facebookLogin,
          ),
        ),
      ),
    );
  }
}
