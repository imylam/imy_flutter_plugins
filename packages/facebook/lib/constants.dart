class Constants {
  static const CHANNEL_NAME = "imy_facebook";
  static const METHOD_LOG_IN = "login";
  static const METHOD_LOG_OUT = "logout";
  static const METHOD_GET_ACCESS_TOKEN = "getAccessToken";

  static const ARG_LOG_IN_BEHAVIOUR = "loginBehaviour";
  static const ARG_PERMISSIONS = "permissions";

  static const RESP_ACCESS_TOKEN = "accessToken";
  static const RESP_ERROR_MSG = "errorMsg";
  static const RESP_STATUS_KEY = "status";
  static const RESP_STATUS_CANCELLED = "cancelled";
  static const RESP_STATUS_ERROR = "error";
  static const RESP_STATUS_LOGGED_IN = "loggined";

  static const ERROR_LOGIN_IN_PROGRESS = "loginInProgress";
  static const ERROR_LOGIN_IN_PROGRESS_MSG = "cannot login when another login process is in progress";
}