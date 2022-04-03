package dk.emilmadsen.namerrapi.repository;

import dk.emilmadsen.namerrapi.model.Name;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NameRepository extends JpaRepository<Name, Long> {
    Optional<Name> findByName(String name);
}
