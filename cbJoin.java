import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonneService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PersonneDto> getPersonnesAvecAdresses() {
        // Création du CriteriaBuilder
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Création de la requête pour PersonneEntity
        CriteriaQuery<PersonneDto> query = cb.createQuery(PersonneDto.class);
        Root<PersonneEntity> personneRoot = query.from(PersonneEntity.class);

        // Rejoindre la table des adresses
        Join<PersonneEntity, AdresseEntity> adresseJoin = personneRoot.join("adresses", JoinType.LEFT);

        // Sélectionner les attributs requis pour PersonneDto
        query.multiselect(
                personneRoot.get("id"),
                personneRoot.get("nom"),
                cb.array(adresseJoin.get("rue"), adresseJoin.get("ville"))
        );

        // Exécuter la requête
        List<Object[]> resultList = entityManager.createQuery(query).getResultList();

        // Mapper les résultats vers PersonneDto et AdresseDto
        List<PersonneDto> personnesDtos = resultList.stream().collect(Collectors.groupingBy(
                row -> new PersonneDto((Long) row[0], (String) row[1], new ArrayList<>()),
                Collectors.mapping(row -> new AdresseDto((String) ((Object[]) row[2])[0], (String) ((Object[]) row[2])[1]), Collectors.toList())
        ));

        return personnesDtos;
    }
}
