package zin.rashidi.boot.data.jpa.keyset.pagination.article;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "articles")
class Article {

    @Id
    private UUID id;
    private String title;
    private String author;
    private int views;

}
