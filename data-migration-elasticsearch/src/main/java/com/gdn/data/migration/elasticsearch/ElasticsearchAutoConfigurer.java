package com.gdn.data.migration.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.data.migration.core.DataMigrationAutoConfigurer;
import com.gdn.data.migration.core.DataMigrationProperties;
import okhttp3.OkHttpClient;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Eko Kurniawan Khannedy
 */
@Configuration
@AutoConfigureAfter(DataMigrationAutoConfigurer.class)
@EnableConfigurationProperties({ElasticsearchConfiguration.class, DataMigrationProperties.class})
public class ElasticsearchAutoConfigurer {

  /**
   * OkHTTP client bean
   *
   * @return OkHTTPClient
   */
  @Bean
  public OkHttpClient okHttpClient() {
    return new OkHttpClient();
  }

  /**
   * ElaticsearchInternal bean
   *
   * @param configuration elasticsearch configuration
   * @param okHttpClient  ok http client
   * @param objectMapper  object mapper
   * @return
   */
  @Bean
  @Autowired
  public ElasticsearchInternal elasticsearchInternal(ElasticsearchConfiguration configuration,
                                                     OkHttpClient okHttpClient,
                                                     ObjectMapper objectMapper,
                                                     DataMigrationProperties dataMigrationProperties) {
    return new ElasticsearchInternal(configuration, okHttpClient, objectMapper, dataMigrationProperties);
  }

  /**
   * Elasticsearch RESTfull high level client bean
   *
   * @param configuration elasticsearch configuration
   * @return bean
   */
  @Bean(destroyMethod = "close")
  @Autowired
  public RestHighLevelClient restHighLevelClient(ElasticsearchConfiguration configuration) {
    return new RestHighLevelClient(
      RestClient.builder(
        new HttpHost(configuration.getHost(), configuration.getPort(), configuration.getProtocol())
      )
    );
  }

}
