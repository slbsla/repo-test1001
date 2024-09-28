import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PersonService {

    @PersistenceContext
    private EntityManager entityManager;

    public PersonDto getPersonById(Long personId) {
        // Initialize CriteriaBuilder
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

        // Root of the query: PersonEntity
        Root<PersonEntity> personRoot = query.from(PersonEntity.class);

        // Join PersonEntity with PersonIdentityEntity
        Join<PersonEntity, PersonIdentityEntity> personIdentityJoin = personRoot.join("personIdentity", JoinType.LEFT);
        
        // Join PersonEntity with AddressEntity
        Join<PersonEntity, AdresseEntity> addressJoin = personRoot.join("adresses", JoinType.LEFT);

        // Select required fields for the DTO
        query.multiselect(
                personRoot.get("id"),
                personRoot.get("name"),
                personIdentityJoin.get("id"),
                personIdentityJoin.get("login"),
                personIdentityJoin.get("email"),
                addressJoin.get("id"),
                addressJoin.get("name")
        ).where(cb.equal(personRoot.get("id"), personId));

        // Execute the query
        List<Object[]> results = entityManager.createQuery(query).getResultList();

        // Transform the results into DTOs
        if (!results.isEmpty()) {
            Object[] firstResult = results.get(0);

            // Map PersonIdentityDto
            PersonIdentityDto personIdentity = new PersonIdentityDto(
                    (Long) firstResult[2], 
                    (String) firstResult[3], 
                    (String) firstResult[4]
            );

            // Map AddressDto list
            Set<AdresseDto> adresses = results.stream()
                    .map(row -> new AdresseDto((Long) row[5], (String) row[6]))
                    .collect(Collectors.toCollection(HashSet::new));

            // Map and return PersonDto
            return new PersonDto(
                    (Long) firstResult[0],
                    (String) firstResult[1],
                    personIdentity,
                    adresses
            );
        }

        return null; // or throw an exception if no record found
    }
}
