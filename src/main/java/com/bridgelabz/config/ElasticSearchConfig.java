/******************************************************************************
 *  Compilation:  javac -d bin ElasticSearchConfig.java
 *  Execution:    java -cp bin com.bridgelabz.config;
 *  						  
 *  
 *  Purpose:      ElasticSearch configuration class
 *  @author  Shubham Chavan
 *  @version 1.0
 *  @since   11-12-2019
 *
 ******************************************************************************/
package com.bridgelabz.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

	@Value("${elasticsearch.host}")
	private String elasticSearchHost;
	@Value("${elasticsearch.port}")
	private Integer elasticSearchPort;
	
	@Bean
	public RestHighLevelClient client() {
		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(new HttpHost(elasticSearchHost, elasticSearchPort)));
		return client;
	}
}
