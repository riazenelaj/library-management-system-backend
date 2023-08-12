package com.teamsystem.project.TeamSystem.Project.service;

import com.teamsystem.project.TeamSystem.Project.entities.Author;
import com.teamsystem.project.TeamSystem.Project.models.*;
import com.teamsystem.project.TeamSystem.Project.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    public List<Author> displayAllAuthors() throws Exception {
        return authorRepository.findAll();
    }
    public AuthorResponse createAuthor(AuthorRequest authorRequest) throws Exception {
        Author author = new Author();
        author.setName(authorRequest.getName());
        author.setSurname(authorRequest.getSurname());
        Author savedAuthor = authorRepository.save(author);
        return new AuthorResponse(
                savedAuthor.getId()
        );
    }
    public void deleteAuthor(String id) throws Exception {
        if (authorRepository.findById(id).isEmpty()) {
            throw new Exception("Id does not exist .");
        }
        authorRepository.deleteById(id);
    }
    public AuthorResponse updateAuthor(UpdateAuthorRequest updateAuthorRequest, String id) throws Exception {
        Optional<Author> foundAuthor = authorRepository.findById(id);
        if (foundAuthor.isEmpty()) {
            throw new Exception("Author does not exist");
        }

        Author author = foundAuthor.get();
        if (updateAuthorRequest.getName() != null) {
            author.setName(updateAuthorRequest.getName());
        }
        if (updateAuthorRequest.getSurname() != null) {
            author.setSurname(updateAuthorRequest.getSurname());
        }

        Author savedAuthor = authorRepository.save(author);
        return new AuthorResponse(savedAuthor.getId());
    }


    public  List<Author> searchAuthor(String query)throws Exception{
        List<Author> authors=authorRepository.searchAuthor(query);
        List<Author>  responses=new ArrayList<>();

        for (Author author: authors){
           Author response=new Author();
           response.setId(author.getId());
            response.setName(author.getName());
            response.setSurname(author.getSurname());
            responses.add(response);
        }
        return responses;
    }
}
