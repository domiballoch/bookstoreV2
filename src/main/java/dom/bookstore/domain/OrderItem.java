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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Builder(toBuilder = true)
@Data
@Entity
@Table(name = "order_item")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"orderItemId"})
@ToString(of = {"orderItemId", "orderDetails", "book"})
public class OrderItem implements Serializable {

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="fkOrderDetailsId")
    private OrderDetails orderDetails;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fkIsbn")
    private Book book;

}
