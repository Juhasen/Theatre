package pl.juhas.theater.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "play")
public class Play {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String director;
    private String genre;
    private Integer duration; // in minutes

    public Play(String title, String description, String director, String genre, Integer duration) {
        this.title = title;
        this.description = description;
        this.director = director;
        this.genre = genre;
        this.duration = duration;
    }
}
