import javax.net.ssl.*

def disableSSLValidation() {
    TrustManager[] trustAllCerts = [
        [
    getAcceptedIssuers: { null },
    checkClientTrusted: { chain, authType -> },
    checkServerTrusted: { chain, authType -> }
        ] as X509TrustManager
    ] as TrustManager[]

    SSLContext sc = SSLContext.getInstance("SSL")
    sc.init(null, trustAllCerts, new java.security.SecureRandom())
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
    HttpsURLConnection.setDefaultHostnameVerifier({ hostname, session -> true })
    println "⚠️  SSL validation has been disabled for this Gradle session."
}

allprojects {
    // Apply SSL override as early as possible
    gradle.projectsLoaded {
        disableSSLValidation()
    }

    // Optional: apply to all repository definitions
    buildscript {
        repositories {
            all {
                allowInsecureProtocol = true
            }
        }
    }

    repositories {
        all {
            allowInsecureProtocol = true
        }
    }
}
