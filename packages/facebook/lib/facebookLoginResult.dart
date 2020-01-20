import 'package:facebook/constants.dart';
import 'package:facebook/facebookAccessToken.dart';

class FacebookLoginResult {
  final String status;
  final FacebookAccessToken accessToken;
  final String errorMessage;

  FacebookLoginResult.fromMap(Map<String, dynamic> map)
      : status = map[Constants.RESP_STATUS_KEY],
        accessToken = map[Constants.RESP_ACCESS_TOKEN] != null
            ? FacebookAccessToken.fromMap(
          map[Constants.RESP_ACCESS_TOKEN].cast<String, dynamic>(),
        )
            : null,
        errorMessage = map[Constants.RESP_STATUS_ERROR];
}