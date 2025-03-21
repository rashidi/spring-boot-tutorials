package zin.rashidi.data.repositorydefinition.note;

import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;

/**
 * @author Rashidi Zin
 */
@RepositoryDefinition(domainClass = Note.class, idClass = Long.class)
interface NoteRepository {

    List<Note> findByTitleContainingIgnoreCase(String title);

}
