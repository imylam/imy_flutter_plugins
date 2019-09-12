#import "KeystorePlugin.h"
#import <keystore/keystore-Swift.h>

@implementation KeystorePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftKeystorePlugin registerWithRegistrar:registrar];
}
@end
