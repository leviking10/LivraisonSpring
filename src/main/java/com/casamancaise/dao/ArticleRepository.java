package com.casamancaise.dao;

import com.casamancaise.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*") //ou ("http://localhost:4200) port par default de Angular
public interface ArticleRepository extends JpaRepository<Article, Long>{
}
