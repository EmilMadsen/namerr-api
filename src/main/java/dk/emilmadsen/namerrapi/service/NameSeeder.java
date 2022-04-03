package dk.emilmadsen.namerrapi.service;

import dk.emilmadsen.namerrapi.model.Name;
import dk.emilmadsen.namerrapi.model.NameCount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class NameSeeder {

    private final NameService nameService;
    private final RestTemplate restTemplate;

    /**
     * loads csv file with all names from filesystem and insets in database.
     */
    public void loadNamesFromFile(String path) {

//        File file = new File("C:\\Users\\Emilo\\Downloads\\navne_drenge.csv");
//        File file = new File("C:\\Users\\Emilo\\Downloads\\navne_piger.csv");
        try (Stream<String> lines = Files.lines(new File(path).toPath())) {
            lines.forEachOrdered(line-> {
                String[] split = line.split(",");

                Name name = new Name();
                name.setName(split[0]);
                name.setMale(split[1].equalsIgnoreCase("ja"));
                name.setFemale(split[2].equalsIgnoreCase("ja"));

                Optional<Name> existing = nameService.findByName(name.getName());
                if (existing.isPresent()) {
                    log.info("updating: {}", name.getName());
                    Name old = existing.get();
                    if (old.isFemale()) {
                        name.setFemale(true);
                    }
                    if (old.isMale()) {
                        name.setMale(true);
                    }
                    name.setId(old.getId());
                } else {
                    log.info("inserting: {}", name.getName());
                }

                nameService.save(name);
            });
        } catch (IOException e) {
            log.error("failed storing name.", e);
        }

    }

    public Name enrichNameWithCount(Name name) {

        String url = "https://www.dst.dk/da/Statistik/emner/borgere/navne/HvorMange?ajax=1";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        Map<String, String> map = new HashMap<>();
        map.put("firstName", name.getName());
        HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);

        String response = restTemplate.postForObject(url, request, String.class);

        name.setNameCountList(parseCountHtml(response));

        return nameService.save(name);

    }

    protected List<NameCount> parseCountHtml(String html) {

        Document doc = Jsoup.parse(html);
        Elements rows = doc.getElementsByTag("tr");
        if (rows.size() != 3) {
            throw new IllegalArgumentException("unexpected amount of rows found in html.");
        }

        List<NameCount> nameCountList = new ArrayList<>();
        NameCount y21 = new NameCount(2021);
        NameCount y22 = new NameCount(2022);

        for (Element row : rows) {

            List<Element> children = row.getAllElements();

            String first = children.get(1).getAllElements().get(0).text();
            if (first.equalsIgnoreCase("Resultat af søgning")) {
                continue;
            }

            String second = children.get(2).getAllElements().get(0).text();
            String third = children.get(3).getAllElements().get(0).text();
            if (first.startsWith("Mænd med fornavnet")) {
                y21.setMaleCount(Integer.valueOf(second.replace(".", "")));
                y22.setMaleCount(Integer.valueOf(third.replace(".", "")));
            } else if (first.startsWith("Kvinder med fornavnet")) {
                y21.setFemaleCount(Integer.valueOf(second.replace(".", "")));
                y22.setFemaleCount(Integer.valueOf(third.replace(".", "")));
            }

        }

        nameCountList.add(y21);
        nameCountList.add(y22);
        return nameCountList;

    }
}

