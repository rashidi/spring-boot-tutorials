package zin.rashidi.boot.ai.redis.document;

import java.util.Map;
import java.util.UUID;

public record DocumentItem(
    UUID id,
    String text,
    Map<String, Object> metadata
) {}
