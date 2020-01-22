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
package com.bridgelabz.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import com.bridgelabz.model.Note;
import com.bridgelabz.response.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@SuppressWarnings("unchecked")
public class ElasticSearchServiceImpl implements IElasticSearchService{

	@Autowired
	ObjectMapper mapper;
	//@Autowired
	//ElasticsearchTemplate template;
	@Autowired
	RestHighLevelClient clinet;//spelling
	private final String INDEX = "note";
	private final String TYPE = "notes";
	@Override
	public Response insert(Note note) throws IOException {
		
		Map<String, Object> map = mapper.convertValue(note, Map.class);
		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, Integer.toString(note.getNoteId())).source(map);
		IndexResponse indexResponse = clinet.index(indexRequest, RequestOptions.DEFAULT);
		return new Response(200, "Added"+indexResponse.getResult().name(), null);
	}
	
	@Override
	public Response delete(Note note) throws IOException {
		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, Integer.toString(note.getNoteId()));
		DeleteResponse deleteResponse = clinet.delete(deleteRequest, RequestOptions.DEFAULT);
		return new Response(200,"Note Deleted "+deleteResponse.getResult().name(),null );
	}


	@Override
	public Response update(Note note) throws IOException {
		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, Integer.toString(note.getNoteId()));
		Map<String,Object> map = mapper.convertValue(note, Map.class);
		updateRequest.doc(map);
		UpdateResponse updateResponse = clinet.update(updateRequest, RequestOptions.DEFAULT);
		return new Response(200, "update "+updateResponse.getResult().name(),null);
	} 

	@Override
	public Response searchNoteByTitle(String title,int userId) throws IOException {
		//System.out.println(title);
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder builder=new SearchSourceBuilder();
		builder.query(QueryBuilders.boolQuery()//.must(QueryBuilders.matchQuery("title", ".*"+title+".*"))
				.must(QueryBuilders.wildcardQuery("title", title+"*"))
				.must(QueryBuilders.matchQuery("user.id", userId)));
//		builder.query(
//				QueryBuilders.boolQuery().should(QueryBuilders.queryStringQuery(Integer.toString(userId))
//						.lenient(true).field("user.id"))
//				.should(QueryBuilders.queryStringQuery(title)
//						.lenient(true).field("title")));
		searchRequest.source(builder);
		SearchResponse searchResponse = clinet.search(searchRequest, RequestOptions.DEFAULT);
		return new Response(200, "Search By Title :", getSearchResultNote(searchResponse));
	}

	@Override
	public Response searchNoteByDescription(String description,int userId) throws IOException{
		SearchSourceBuilder builder=getSearchSourceBuilder();
		builder.query(QueryBuilders.boolQuery().must(QueryBuilders.wildcardQuery("description", ""+description+"*"))
				.must(QueryBuilders.matchQuery("user.id", userId)));
		return new Response(200, "Search By Description :", getSearchResultNote(clinet.search(getSearchRequest().source(builder), RequestOptions.DEFAULT)));
	}

	@Override
	public Response searchNoteByText(String text,int userId) throws IOException {
		SearchSourceBuilder builder=getSearchSourceBuilder();
		builder.query(QueryBuilders.boolQuery()//.must(QueryBuilders.multiMatchQuery(text+"*","title","description"))
				.must(QueryBuilders.queryStringQuery("*"+text+"*").field("title").field("description"))
				.must(QueryBuilders.matchQuery("user.id", userId)));
		return new Response(200, "Search By Text :", getSearchResultNote(clinet.search(getSearchRequest().source(builder), RequestOptions.DEFAULT)));
	}

	@Override
	public Response findAll(int userId) throws IOException {
		SearchSourceBuilder builder = getSearchSourceBuilder();
		builder.query(QueryBuilders.boolQuery().should(QueryBuilders.queryStringQuery(Integer.toString(userId))
				.lenient(true).field("user.id")));
		return new Response(200,"List of Notes", getSearchResultNote(clinet.search(getSearchRequest().source(builder), RequestOptions.DEFAULT)));
	}


	@Override
	public List<Note> searchNoteById(String id) throws IOException {
		System.out.println(id);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(
				QueryBuilders.boolQuery().should(QueryBuilders.queryStringQuery(id)
						.lenient(true).field("noteId")));
		SearchResponse searchResponse = clinet.search(getSearchRequest().source(searchSourceBuilder), RequestOptions.DEFAULT);
		return getSearchResultNote(searchResponse);
	}
	
	public List<Note> getSearchResultNote(SearchResponse response) {
		SearchHit[] searchHit = response.getHits().getHits();
		List<Note> notes = new ArrayList<>();
			if(searchHit.length>0) {
				Arrays.stream(searchHit).forEach(i->notes.add(mapper.convertValue(i.getSourceAsMap(),Note.class)));
			}
		return  notes;
	}
	
	private SearchRequest getSearchRequest() {
		return new SearchRequest();
	}
	
	private SearchSourceBuilder getSearchSourceBuilder() {
		return new SearchSourceBuilder();
	}
}
