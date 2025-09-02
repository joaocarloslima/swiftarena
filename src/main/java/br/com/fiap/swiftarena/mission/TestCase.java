package br.com.fiap.swiftarena.mission;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TestCase {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Mission mission;

    @Column(columnDefinition = "TEXT")
    private String input;

    @Column(columnDefinition = "TEXT")
    private String expectedOutput;
}