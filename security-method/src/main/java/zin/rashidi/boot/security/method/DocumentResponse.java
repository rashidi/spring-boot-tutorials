package zin.rashidi.boot.security.method;

import java.util.UUID;

public record DocumentResponse(UUID id, String title, String content, String author) {

    public static DocumentResponse from(Document document) {
        return new DocumentResponse(
            document.getId(),
            document.getTitle(),
            document.getContent(),
            document.getAuthor()
        );
    }
}
