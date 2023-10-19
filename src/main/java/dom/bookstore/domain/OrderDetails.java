package dom.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * If experiencing JSON serialisation exception with LAZY loading.
 * Either change to EAGER if you need the Entity info always available as part of the response or if performing calculations
 * Otherwise use @JsonIgnore if its not required in the response
 * Alternatively use @JsonManagedReference (Parent) and @JsonBackReference (Child)
 */
@Builder(toBuilder = true)
@Data
@Entity
@Table(name = "order_details")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"orderDetailsId"})
@ToString(of = {"orderDetailsId", "totalOrderPrice", "orderDate", "users", "orderItems"})
public class OrderDetails implements Serializable {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_details_id")
    private Long orderDetailsId;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @JsonIgnore
    @Column(insertable = false, updatable = false)
    private Long fkUserId;

    @Column(name = "total_order_price")
    private BigDecimal totalOrderPrice;

    @Column(name = "order_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "fkUserId")
    private Users users;

    @OneToMany(mappedBy = "orderDetails", fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

}
