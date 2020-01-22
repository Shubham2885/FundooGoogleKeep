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

import org.springframework.data.jpa.repository.JpaRepository;
import com.bridgelabz.model.UserCollaborate;


public interface UserCollaborateRepository extends JpaRepository<UserCollaborate, Integer>{
}
