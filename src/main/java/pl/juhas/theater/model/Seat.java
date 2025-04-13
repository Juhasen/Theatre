package pl.juhas.theater.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.List;

@Data
@With
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seat")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer row;

    @Column(name = "column_number")
    private Integer column;

    @Column(name = "is_available")
    private Boolean isAvailable;


}
