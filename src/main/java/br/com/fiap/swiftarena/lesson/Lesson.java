package br.com.fiap.swiftarena.lesson;

import br.com.fiap.swiftarena.mission.Mission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int number;

    private boolean active;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<Mission> missions = new ArrayList<>();

}
