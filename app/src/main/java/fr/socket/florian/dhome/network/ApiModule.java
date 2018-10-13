package fr.socket.florian.dhome.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import fr.socket.florian.dhome.R;
import io.realm.RealmObject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiModule {
    @NonNull
    private final Gson gson;
    @NonNull
    private final String baseUrl;
    @NonNull
    private final OkHttpClient.Builder httpClientBuilder;

    public ApiModule(@NonNull String baseUrl, @NonNull Context context) {
        this.baseUrl = baseUrl;

        httpClientBuilder = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS);

        gson = initGson();

        initHttpLogging(HttpLoggingInterceptor.Level.BODY);

        initSSL(context);
    }

    private static Gson initGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        gsonBuilder.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getDeclaringClass().equals(RealmObject.class);
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });
        return gsonBuilder.create();
    }

    private void initHttpLogging(HttpLoggingInterceptor.Level level) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(level);
    }

    private void initSSL(@NonNull Context context) {
        SSLContext sslContext = null;
        try {
            sslContext = createCertificate(context.getResources().openRawResource(R.raw.certificate));
        } catch (CertificateException | IOException | KeyStoreException | KeyManagementException | NoSuchAlgorithmException e) {
            Log.e("ApiModule", "initSSL : " + e.getMessage(), e);
        }
        if (sslContext != null)
            httpClientBuilder.sslSocketFactory(sslContext.getSocketFactory());
    }

    private SSLContext createCertificate(InputStream trustedCertificateIS) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca;
        try {
            ca = cf.generateCertificate(trustedCertificateIS);
        } finally {
            trustedCertificateIS.close();
        }

        // creating a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // creating a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // creating an SSLSocketFactory that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        return sslContext;
    }

    @NonNull
    public Network provideApi() {

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .build()
                .create(Network.class);
    }
}

