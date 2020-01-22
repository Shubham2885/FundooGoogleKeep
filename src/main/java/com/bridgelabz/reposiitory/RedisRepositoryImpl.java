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

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.bridgelabz.model.RedisModel;

@Repository
@SuppressWarnings({"unchecked","rawtypes"})
public class RedisRepositoryImpl implements IRedisRepository{

	 
	 final static private String KEY="UserToken";
	 private RedisTemplate<String, Object> redisTemplate;
	 
	private HashOperations hashOperations;
	 
	 @Autowired
	 public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate){
	        this.redisTemplate = redisTemplate;
	 }
	 @PostConstruct
	 private void init(){
	    hashOperations = redisTemplate.opsForHash();
	 }
	

	@Override
	public RedisModel findUser(String id) {
		return (RedisModel)hashOperations.get(id, KEY);
	}

	@Override
	public void add(RedisModel model) {
		hashOperations.put(model.getId(),KEY,model);
	}


	@Override
	public long delete(String id) {
		return hashOperations.delete(id, KEY);
	}
}
