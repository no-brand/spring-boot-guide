package com.nobrand.springbootjparest.data;

import com.nobrand.springbootjparest.model.Person;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {

    // GET /people/search/findByLastName?name={ }
    List<Person> findByLastName(@Param("name") String name);

}
