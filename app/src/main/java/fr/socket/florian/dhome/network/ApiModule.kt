package fr.socket.florian.dhome.network

import android.content.Context
import android.util.Log

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.io.IOException
import java.io.InputStream
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

import fr.socket.florian.dhome.R
import io.realm.RealmObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


internal class ApiModule(private val baseUrl: String, context: Context) {
    private val gson: Gson
    private val httpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS)

    init {

        gson = initGson()

        initHttpLogging(HttpLoggingInterceptor.Level.BODY)

        initSSL(context)
    }

    private fun initGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setLenient()
        gsonBuilder.setExclusionStrategies(object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes): Boolean {
                return f.declaringClass == RealmObject::class.java
            }

            override fun shouldSkipClass(clazz: Class<*>): Boolean {
                return false
            }
        })
        return gsonBuilder.create()
    }

    private fun initHttpLogging(level: HttpLoggingInterceptor.Level) {
        val logging = HttpLoggingInterceptor()
        logging.level = level
    }

    private fun initSSL(context: Context) {
        var sslContext: SSLContext? = null
        try {
            sslContext = createCertificate(context.resources.openRawResource(R.raw.certificate))
        } catch (e: CertificateException) {
            Log.e("ApiModule", "initSSL : " + e.message, e)
        } catch (e: IOException) {
            Log.e("ApiModule", "initSSL : " + e.message, e)
        } catch (e: KeyStoreException) {
            Log.e("ApiModule", "initSSL : " + e.message, e)
        } catch (e: KeyManagementException) {
            Log.e("ApiModule", "initSSL : " + e.message, e)
        } catch (e: NoSuchAlgorithmException) {
            Log.e("ApiModule", "initSSL : " + e.message, e)
        }

        if (sslContext != null)
            httpClientBuilder.sslSocketFactory(sslContext.socketFactory)
    }

    @Throws(CertificateException::class, IOException::class, KeyStoreException::class, NoSuchAlgorithmException::class, KeyManagementException::class)
    private fun createCertificate(trustedCertificateIS: InputStream): SSLContext {
        val cf = CertificateFactory.getInstance("X.509")
        val ca: Certificate
        try {
            ca = cf.generateCertificate(trustedCertificateIS)
        } finally {
            trustedCertificateIS.close()
        }

        // creating a KeyStore containing our trusted CAs
        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", ca)

        // creating a TrustManager that trusts the CAs in our KeyStore
        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
        tmf.init(keyStore)

        // creating an SSLSocketFactory that uses our TrustManager
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers, null)
        return sslContext
    }

    fun provideApi(): Network {

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(createUnsafeOkHttpClient())
                .build()
                .create(Network::class.java)
    }

    // TODO: 10/10/18 Remove this function
    private fun createUnsafeOkHttpClient() : OkHttpClient {
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}

                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            })
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }

            return builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

