import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';

import 'package:facebook/facebook.dart';
import 'package:facebook/facebookAccessToken.dart';
import 'package:facebook/facebookLoginResult.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _channelMsg = 'Unknown';
  Facebook _facebook;

  @override
  void initState() {
    super.initState();
    _facebook = Facebook();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    FacebookLoginResult result;
    String output;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      result = await _facebook.logIn(["email"]);
      if(result.accessToken != null) {
        output = result.accessToken.token;
      } else {
        output = result.errorMessage ?? result.status;
      }
    } on PlatformException {
      output = 'Failed';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _channelMsg = output;
    });
  }

  Future<void> update() async {
    String output;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      FacebookAccessToken token = await _facebook.currentAccessToken;
      output = token.token;
    } on PlatformException {
      output = 'Failed';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _channelMsg = output;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: <Widget>[
              Text('Token: $_channelMsg\n'),
              RaisedButton(
                onPressed: update,
              ),
            ],
          ),
        ),
      ),
    );
  }
}
