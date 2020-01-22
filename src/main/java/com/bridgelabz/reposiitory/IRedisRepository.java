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
package com.bridgelabz.reposiitory;

import com.bridgelabz.model.RedisModel;

public interface IRedisRepository{
	void add(RedisModel model);
	long delete(String id);
	RedisModel findUser(String id);
}
