package innovation.ride.umusare.entity;

import innovation.ride.umusare.entity.enums.LocationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "locations")
public class Location extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "location_id")
    private String locationId;

    @Column(name = "location_name", nullable = false)
    private String locationName;

    @Column(name = "location_code", unique = true)
    private String locationCode;

    @Enumerated(EnumType.STRING)
    private LocationType type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_location_id")
    private Location parent;

    @OneToMany(mappedBy = "parent")
    private List<Location> children = new ArrayList<>();
}
