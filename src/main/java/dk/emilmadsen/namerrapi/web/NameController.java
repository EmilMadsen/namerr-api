package dk.emilmadsen.namerrapi.web;

import com.google.gson.Gson;
import dk.emilmadsen.namerrapi.model.Name;
import dk.emilmadsen.namerrapi.service.NameSeeder;
import dk.emilmadsen.namerrapi.service.NameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/name")
@RequiredArgsConstructor
public class NameController {

    private final NameSeeder seeder;
    private final NameService service;
    private final Gson gson;

    @GetMapping("/{id}")
    public Name findById(@PathVariable String id) {
        return service.findById(Long.valueOf(id)).orElse(null);
    }

    @GetMapping("/name/{name}")
    public Name findByName(@PathVariable String name) {
        return service.findByName(name).orElse(null);
    }

    @GetMapping("/seed")
    public void seedNames() {
        seeder.loadNamesFromFile("blank");
    }

}
