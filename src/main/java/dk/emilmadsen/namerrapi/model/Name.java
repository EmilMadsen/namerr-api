package dk.emilmadsen.namerrapi.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "name")
@Data
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Name {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private boolean male;
    private boolean female;
    private String origin;
    private String meaning;
    private String description;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<NameCount> nameCountList = new ArrayList<>();

    @CreationTimestamp
    private ZonedDateTime created;
    @UpdateTimestamp
    private ZonedDateTime updated;

}
