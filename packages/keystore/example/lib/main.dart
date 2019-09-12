import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:keystore/keystore.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _publicKey = '';
  String _isSignatureValid = '';
  bool _exception = false;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String publicKey;
    String isSignatureValid;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      publicKey = await Keystore.getPublicKey;

      String data = "hello";
      bool ver = await Keystore.signData(data).then((signature) { return Keystore.verifySignature(data, signature); });
      isSignatureValid = ver.toString();
    } on PlatformException {
      _exception = true;
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _publicKey = publicKey;
      _isSignatureValid = isSignatureValid;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Imy Flutter Plugin Keytore'),
        ),
        body: Center(
          child: _exception? Text("Excepiton!")
            : Column(
                children: <Widget>[
                  Text('$_publicKey\n'),
                  Text('$_isSignatureValid\n'),
                ],
              ),
        ),
      ),
    );
  }
}
