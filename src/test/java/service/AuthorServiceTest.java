package service;

import com.teamsystem.project.TeamSystem.Project.entities.Author;
import com.teamsystem.project.TeamSystem.Project.entities.Book;
import com.teamsystem.project.TeamSystem.Project.models.AuthorRequest;
import com.teamsystem.project.TeamSystem.Project.models.AuthorResponse;
import com.teamsystem.project.TeamSystem.Project.models.GetAuthorResponse;
import com.teamsystem.project.TeamSystem.Project.models.UpdateAuthorRequest;
import com.teamsystem.project.TeamSystem.Project.repositories.AuthorRepository;
import com.teamsystem.project.TeamSystem.Project.service.AuthorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private AuthorService authorService;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testCreateAuthor()throws Exception{
        String id="111";
        AuthorRequest author=new AuthorRequest("test","test");
        Author savedAuthor=new Author();
        savedAuthor.setId(id);
        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);
        AuthorResponse authorResponse=authorService.createAuthor(author);
        Assertions.assertEquals(id,savedAuthor.getId());
        verify(authorRepository,times(1)).save(any(Author.class));
    }

    @Test
    public void testDeleteAuthor()throws Exception{
        String id="123";
        Author author=new Author(id,"test","test");
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        authorService.deleteAuthor(id);
        verify(authorRepository,times(1)).deleteById(id);
    }

    @Test
    public void throwsExceptionWhenAuthorNotFoundOnDelete() throws Exception {
        String nonExistingId="123";
        when(authorRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        Assertions.assertThrows(Exception.class,()->authorService.deleteAuthor(nonExistingId));
    }

    @Test
    public void testUpdateBook()throws Exception{
        String id="123";
        Author author=new Author(id,"test","test");
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        UpdateAuthorRequest authorRequest=new UpdateAuthorRequest("updated","updated");
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        AuthorResponse authorResponse=authorService.updateAuthor(authorRequest,id);

        verify(authorRepository,times(1)).findById(id);
        verify(authorRepository,times(1)).save(author);

        Assertions.assertEquals(authorRequest.getName(),author.getName());
        Assertions.assertEquals(authorRequest.getSurname(),author.getSurname());
    }
    @Test
    public void throwsExceptionsWhenAuthorNotFoundOnUpdate()throws Exception{
        String nonExistingId="123";
        UpdateAuthorRequest updateAuthorRequest=new UpdateAuthorRequest("test","test");
        when(authorRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        Assertions.assertThrows(Exception.class,()->authorService.updateAuthor(updateAuthorRequest,nonExistingId));
    }

    @Test
    public void testSearchAuthor()throws Exception{
        String query="name";
        Author author1=new Author("1","name 1","test");
        Author author2=new Author("2","name 2","test");
        List<Author> authorsList=new ArrayList<>();
        authorsList.add(author1);
        authorsList.add(author2);
        when(authorRepository.searchAuthor(query)).thenReturn(authorsList);
        List<Author> authorResponseList=authorService.searchAuthor(query);
        Assertions.assertEquals(2,authorResponseList.size());

        Author response1=authorResponseList.get(0);
        Assertions.assertEquals(author1.getName(),response1.getName());
        Assertions.assertEquals(author1.getSurname(),response1.getSurname());

        Author response2=authorResponseList.get(1);
        Assertions.assertEquals(author2.getName(),response2.getName());
        Assertions.assertEquals(author2.getSurname(),response2.getSurname());
        verify(authorRepository,times(1)).searchAuthor(query);
    }

    @Test
    public void testDisplayAllAuthors()throws Exception{
        Author author1=new Author("1","name 1","test");
        Author author2=new Author("2","name 2","test");
        List<Author> authorsList=new ArrayList<>();
        authorsList.add(author1);
        authorsList.add(author2);
        when(authorRepository.findAll()).thenReturn(authorsList);
        List<Author> response=authorService.displayAllAuthors();

        Assertions.assertEquals(2,response.size());
        Author authorResponse1=response.get(0);
        Assertions.assertEquals(author1.getId(),authorResponse1.getId());
        Assertions.assertEquals(author1.getName(),authorResponse1.getName());
        Assertions.assertEquals(author1.getSurname(),authorResponse1.getSurname());

        Author authorResponse2=response.get(1);
        Assertions.assertEquals(author2.getId(),authorResponse2.getId());
        Assertions.assertEquals(author2.getName(),authorResponse2.getName());
        Assertions.assertEquals(author2.getSurname(),authorResponse2.getSurname());
        verify(authorRepository,times(1)).findAll();
    }
}
