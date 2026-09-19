package innovation.ride.umusare.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "passengers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Passenger extends User {


    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL)
    private List<Vehicle> vehicles = new ArrayList<>();
}
