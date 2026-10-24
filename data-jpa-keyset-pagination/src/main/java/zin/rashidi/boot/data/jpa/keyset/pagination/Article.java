package zin.rashidi.boot.data.jpa.keyset.pagination;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    private UUID id;
    private String title;
    private String author;
    private int views;

    public Article() {
    }

    public Article(UUID id, String title, String author, int views) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.views = views;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getViews() {
        return views;
    }
}
