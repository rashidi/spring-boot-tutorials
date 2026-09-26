package zin.rashidi.boot.ai.chroma.document;

import java.util.Map;
import java.util.UUID;

/**
 * @author Rashidi Zin
 */
public record DocumentItem(UUID id, String content, Map<String, Object> metadata) {
}
