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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Builder
@Data
@Entity
@Table(name = "basket_item")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"basketItemId"})
@ToString(of = {"basketItemId", "quantity", "totalPrice", "book"})
public class BasketItem {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_item_id")
    private Long basketItemId;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @JsonIgnore
    @Column(insertable = false, updatable = false)
    private Long fkBasketId;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @JsonIgnore
    @Column(insertable = false, updatable = false)
    private Long fkIsbn;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="fkBasketId")
    private Basket basket;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "fkIsbn")
    private Book book;
}
