import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.HttpURLConnection;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() throws Exception {
        SSLUtil.disableSSLVerification();

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                if (connection instanceof HttpsURLConnection) {
                    HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
                    httpsConnection.setHostnameVerifier((hostname, session) -> true);
                }
                super.prepareConnection(connection, httpMethod);
            }
        };

        return RestClient.builder()
                .requestFactory(() -> requestFactory)
                .baseUrl("https://api.externalprovider.com")  // Change to your actual base URL
                .build();
    }
}
