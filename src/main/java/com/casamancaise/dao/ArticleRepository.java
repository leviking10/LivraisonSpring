package com.casamancaise.dao;

import com.casamancaise.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;


public interface ArticleRepository extends JpaRepository<Article, Long>{
}
