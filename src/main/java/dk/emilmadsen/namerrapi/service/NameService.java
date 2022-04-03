package dk.emilmadsen.namerrapi.service;

import dk.emilmadsen.namerrapi.repository.NameRepository;
import dk.emilmadsen.namerrapi.model.Name;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NameService {

    private final NameRepository nameRepository;

    public Optional<Name> findById(Long id) {
        return nameRepository.findById(id);
    }

    public Optional<Name> findByName(String name) {
        return nameRepository.findByName(name);
    }

    public Name save(Name name) {
        return nameRepository.save(name);
    }

}
