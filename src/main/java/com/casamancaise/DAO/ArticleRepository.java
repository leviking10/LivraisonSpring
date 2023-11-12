package com.casamancaise.DAO;

import com.casamancaise.Entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long>{
}
