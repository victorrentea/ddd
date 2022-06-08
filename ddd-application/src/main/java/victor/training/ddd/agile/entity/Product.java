package victor.training.ddd.agile.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "UK_CODE", columnNames = "CODE"))
// if on a relational DB, it's an opportunity hard to redfus
// BUT ! this does NOT mean that you allow others to write directly to your DATABASE. That would be a reason to sue them. (void the warranty)
public class Product { // PRODUCT
    @Id
    @GeneratedValue
    private Long id;
    private int currentIteration = 0;
    private int currentVersion = 0;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String code;

    // introduces a possible inconsisntency point:
//   @Embedded
//   private ProductOwner owner = new ProductOwner();
    // better, less inconsistencies but perhaps more latency on READ: block threads > WebFlux
    private String ownerUserid;


//   private String ownerEmail;
//   private String ownerName;
//   private String ownerPhone;

    private String teamMailingList;


    @OneToMany(mappedBy = "product")
    private List<Release> releases = new ArrayList<>(); // TODO remove

    protected Product() { // only by hibernate should never be called by developers
    }

    public Product(String code, String name) {
        this.code = Objects.requireNonNull(code);
        this.name = Objects.requireNonNull(name.trim());
    }


    public int incrementAndGetIteration() {
        currentIteration++;
        return currentIteration;
    }

    public int incrementAndGetVersion() {
        currentVersion++;
        return currentVersion;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Optional<String> getOwnerUserid() {
        return Optional.ofNullable(ownerUserid);
    }

    public Product setOwnerUserid(String ownerUserid) {
        this.ownerUserid = ownerUserid;
        return this;
    }

    public Optional<String> getTeamMailingList() {
        return Optional.ofNullable(teamMailingList);
    }

    public Product setTeamMailingList(String teamMailingList) {
        this.teamMailingList = teamMailingList;
        return this;
    }

    public List<Release> getReleases() {
        return releases;
    }


    public String toString() {
        return "Product(id=" + getId() + ")";
    }
}

