class FacebookAccessToken {
  final String token;
  final String userId;
  final DateTime expires;
  final List<String> permissions;
  final List<String> declinedPermissions;

  bool isValid() => DateTime.now().isBefore(expires);

  FacebookAccessToken.fromMap(Map<String, dynamic> map)
      : token = map['token'],
        userId = map['userId'],
        expires = map['expires'],
        permissions = map['permissions'],
        declinedPermissions = map['declinedPermissions'];

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'token': token,
      'userId': userId,
      'expires': expires.millisecondsSinceEpoch,
      'permissions': permissions,
      'declinedPermissions': declinedPermissions,
    };
  }

}