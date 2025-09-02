package br.com.fiap.swiftarena.mission;

import br.com.fiap.swiftarena.lesson.Lesson;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Mission {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private boolean challenge;

    @ManyToOne
    private Lesson lesson;

    @Column(columnDefinition = "TEXT")
    private String descriptionMarkdown;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    private List<TestCase> tests = new ArrayList<>();
}