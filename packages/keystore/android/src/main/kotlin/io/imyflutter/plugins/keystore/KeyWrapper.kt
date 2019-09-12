package io.imyflutter.plugins.keystore

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.*
import javax.crypto.Cipher
import android.util.Log

//interface KeyWrapper {
//    @Throws(Exception::class)
//    fun wrap(key: Key): ByteArray;
//
//    @Throws(Exception::class)
//    fun unwrap(wrappedKey: ByteArray, algorithm: String): Key;
//}

class RsaKeyStoreKeyWrapper(context: Context) {
    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val TRANSFORMATION_ASYMMETRIC = "RSA/ECB/OAEPWithSHA-512AndMGF1Padding"
        private const val SIGNATURE_ALGORITHM = "SHA512withRSA/PSS"
    }

    private val keyStoreAlias: String = context.packageName + "KeyStore"
    private val _keyStore: KeyStore = createAndroidKeyStore()

    init {
        genKeyPairIfNeeded()
    }

//    @Throws(Exception::class)
//    override fun wrap(key: Key): ByteArray {
//        val publicKey = getEntry().certificate.publicKey
//        val cipher = getRSACipher()
//        cipher.init(Cipher.WRAP_MODE, publicKey)
//        return cipher.wrap(key)
//    }
//
//    @Throws(Exception::class)
//    override fun unwrap(wrappedKey: ByteArray, algorithm: String): Key {
//        val privateKey = getEntry().privateKey
//        val cipher = getRSACipher()
//        cipher.init(Cipher.UNWRAP_MODE, privateKey)
//
//        return cipher.unwrap(wrappedKey, algorithm, Cipher.SECRET_KEY)
//    }

    @Throws(Exception::class)
    fun encrypt(plainText: String): String {
        val publicKey = getAndroidKeyStoreAsymmetricKeyPair()?.public
        val cipher = getRSACipher()
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val bytes = cipher.doFinal(plainText.toByteArray())

        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    @Throws(Exception::class)
    fun decrypt(cipherText: String): String {
        val cipherTextByte : ByteArray = Base64.decode(cipherText, Base64.DEFAULT)
        val privateKey = getAndroidKeyStoreAsymmetricKeyPair()?.private
        val cipher = getRSACipher()
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val decodedData = cipher.doFinal(cipherTextByte)

        return String(decodedData)
    }

    @Throws(Exception::class)
    fun sign(data: String): String {
        val signatureBytes = Signature.getInstance(SIGNATURE_ALGORITHM).run {
            initSign(getAndroidKeyStoreAsymmetricKeyPair()?.private)
            update(data.toByteArray())
            sign()
        }
        Log.d("data", data)
        return Base64.encodeToString(signatureBytes, Base64.DEFAULT)
    }

    @Throws(Exception::class)
    fun verify(data: String, signature: String): Boolean {
        val signatureByteArray = Base64.decode(signature, Base64.DEFAULT)
        return Signature.getInstance(SIGNATURE_ALGORITHM).run {
            initVerify(getAndroidKeyStoreAsymmetricKeyPair()?.public)
            update(data.toByteArray())
            verify(signatureByteArray)
        }
    }

    @Throws(Exception::class)
    private fun genKeyPairIfNeeded() {
        var kp: KeyPair? = null
        for (i in 1..5) {
            try {
                kp = getAndroidKeyStoreAsymmetricKeyPair()
                break
            } catch (ignored: Exception) {
            }
        }

        if (kp == null) {
            createAsymmetricKeyPairInKeyStore()
            try {
                getAndroidKeyStoreAsymmetricKeyPair()
            } catch (ignored: Exception) {
                removeAndroidKeyStoreKey(keyStoreAlias)
            }
        }
    }

    private fun createAndroidKeyStore(): KeyStore {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        return keyStore
    }

    @Throws(Exception::class)
    private fun getRSACipher(): Cipher {
        return Cipher.getInstance(TRANSFORMATION_ASYMMETRIC, "AndroidKeyStoreBCWorkaround")
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Throws(Exception::class)
    private fun createAsymmetricKeyPairInKeyStore() {
        val kpg: KeyPairGenerator
                = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEYSTORE)

        val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
                keyStoreAlias, KeyProperties.PURPOSE_ENCRYPT or
                KeyProperties.PURPOSE_DECRYPT or
                KeyProperties.PURPOSE_SIGN or
                KeyProperties.PURPOSE_VERIFY
        ).run {
            setDigests(KeyProperties.DIGEST_SHA512)
            setBlockModes(KeyProperties.BLOCK_MODE_ECB)
            setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
            setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PSS)
            build()
        }

        kpg.initialize(parameterSpec)
        kpg.generateKeyPair()
    }

    @Throws(Exception::class)
    fun getAndroidKeyStoreAsymmetricKeyPair(): KeyPair? {
        val privateKey: PrivateKey?
        val publicKey: PublicKey?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            privateKey = _keyStore.getKey(keyStoreAlias, null) as PrivateKey?
            publicKey = _keyStore.getCertificate(keyStoreAlias)?.publicKey
        } else {
            val keyEntry = getEntry()
            privateKey = keyEntry.privateKey
            publicKey = keyEntry.certificate.publicKey
        }

        return if (privateKey != null && publicKey != null) {
            KeyPair(publicKey, privateKey)
        } else {
            null
        }
    }

    @Throws(Exception::class)
    private fun getEntry(): KeyStore.PrivateKeyEntry {
        return (_keyStore.getEntry(keyStoreAlias, null)
                ?: throw Exception("No key found under alias: $keyStoreAlias")) as? KeyStore.PrivateKeyEntry
                ?: throw Exception("Not an instance of a PrivateKeyEntry")
    }

    private fun removeAndroidKeyStoreKey(alias: String) = _keyStore.deleteEntry(alias)
}