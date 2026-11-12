package zin.rashidi.boot.data.jpa.keyset.pagination.article;

import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.repository.Repository;

import java.util.UUID;

interface ArticleRepository extends Repository<Article, UUID> {

    Window<Article> findTop2ByAuthorOrderByViewsDesc(String author, ScrollPosition position);

}
