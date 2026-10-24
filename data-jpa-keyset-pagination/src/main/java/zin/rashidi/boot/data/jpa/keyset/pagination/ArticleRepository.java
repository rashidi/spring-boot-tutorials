package zin.rashidi.boot.data.jpa.keyset.pagination;

import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.repository.Repository;

import java.util.UUID;

interface ArticleRepository extends Repository<Article, UUID> {

    Article save(Article article);

    void deleteAll();

    Window<Article> findTop2ByAuthorOrderByViewsDesc(String author, ScrollPosition position);

}
