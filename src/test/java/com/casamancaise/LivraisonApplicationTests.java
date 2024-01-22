package com.casamancaise;

import com.casamancaise.dao.ArticleRepository;
import com.casamancaise.entities.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LivraisonApplicationTests {

	@Test
	void contextLoads() {
		testRetrieveArticle();
	}
	@Autowired
	private ArticleRepository articleRepository;
	public void testRetrieveArticle() {
		Article article = articleRepository.findById(1L).orElse(null);
		if (article != null) {
			System.out.println("Tonnage: " + article.getTonage());
		} else {
			System.out.println("Article not found");
		}
	}
}
