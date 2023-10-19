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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder(toBuilder = true)
@Data
@Entity
@Table(name = "order_item")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"orderItemId"})
@ToString(of = {"orderItemId", "quantity", "totalPrice", "book"})
public class OrderItem implements Serializable {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @JsonIgnore
    @Column(insertable = false, updatable = false)
    private Long fkOrderDetailsId;

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
    @JoinColumn(name="fkOrderDetailsId")
    private OrderDetails orderDetails;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "fkIsbn")
    private Book book;

}
