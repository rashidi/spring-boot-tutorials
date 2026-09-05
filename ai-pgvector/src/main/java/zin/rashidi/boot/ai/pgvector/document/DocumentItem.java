package zin.rashidi.boot.ai.pgvector.document;

import java.util.Map;
import java.util.UUID;

/**
 * @author Rashidi Zin
 */
record DocumentItem(UUID id, String content, Map<String, Object> metadata) {
}
