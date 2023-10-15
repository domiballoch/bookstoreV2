package dom.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
@Data
@Entity
@Table(name = "basket")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"basketId"})
@ToString(of = {"basketId", "totalPrice", "basketItemList", "users"})
public class Basket {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_id")
    private Long basketId;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @JsonIgnore
    @Column(insertable = false, updatable = false)
    private Long fkUserId;

//    @Column(name = "total_price")
//    private BigDecimal totalPrice;

    //@OneToMany(mappedBy = "basketItemId", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "fkBasketId")
    private List<BasketItem> basketItems = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "fkUserId")
    private Users users;

//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "fkUserId", referencedColumnName = "user_id")
//    private Users users;
}
