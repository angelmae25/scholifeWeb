// FILE PATH: src/main/java/com/scholife1/repository/NewsRepository.java

package com.scholife1.repository;

import com.scholife1.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findAllByOrderByPublishedAtDesc();
    List<News> findByCategoryOrderByPublishedAtDesc(News.NewsCategory category);
}