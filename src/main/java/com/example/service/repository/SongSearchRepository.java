package com.example.service.repository;

import com.example.service.entity.SongDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SongSearchRepository  extends ElasticsearchRepository<SongDocument, Long> {

    List<SongDocument> findByTitleContaining(String keyword);

    @Query("""
{
  "match": {
    "title": {
      "query": "?0",
      "fuzziness": "AUTO"
    }
  }
}
""")
    List<SongDocument> searchFuzzy(String keyword);

}