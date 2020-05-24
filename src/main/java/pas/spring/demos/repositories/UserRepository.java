package pas.spring.demos.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pas.spring.demos.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}