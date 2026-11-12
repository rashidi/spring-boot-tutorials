package zin.rashidi.boot.data.repositorydefinition.note;

import org.springframework.data.annotation.Id;

/**
 * @author Rashidi Zin
 */
record Note(@Id Long id, String title, String content) {
}
