package org.example.web_service_v2.domain.article;

import org.example.web_service_v2.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("""
        select  a from Article a
        join fetch a.user u
        left join fetch u.profile p
        left join fetch p.field f
        order by a.createdAt desc 
    """)
    List<Article> findHomeArticles();
}
