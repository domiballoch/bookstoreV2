package dom.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
@Data
@Entity
@Table(name = "basket")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"basketId"})
@ToString(of = {"basketId", "basketItems"})
public class Basket {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_id")
    private Long basketId;

//    @Getter(AccessLevel.NONE)
//    @Setter(AccessLevel.NONE)
//    @JsonIgnore
//    @Column(insertable = false, updatable = false)
//    private Long fkUserId;

    @OneToMany(mappedBy = "basket", fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<BasketItem> basketItems = new ArrayList<>();

}