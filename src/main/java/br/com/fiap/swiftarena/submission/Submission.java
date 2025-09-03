package br.com.fiap.swiftarena.submission;

import br.com.fiap.swiftarena.mission.Mission;
import br.com.fiap.swiftarena.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Mission mission;

    private int attempt;

    private boolean passed;

    @Column(columnDefinition = "TEXT")
    private String output;

    @Column(columnDefinition = "TEXT")
    private String code;

    private java.time.LocalDateTime submittedAt;
}