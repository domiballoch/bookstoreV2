package dom.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dom.bookstore.exception.ValidateEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
@Data
@Entity
@Table(name = "book")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"isbn"})
@ToString(of = {"isbn", "category", "title", "author", "price", "stock"})
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "isbn")
    private Long isbn;

    @NotNull(message = "Category cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Transient
    @ValidateEnum(enumClass = Category.class, message = "Please select a valid category")
    private String categoryTypeString;

    @NotEmpty(message = "Title must not be empty")
    @Size(max = 100, message = "Title length must be less than one hundred chars")
    @Size(min = 1, message = "Title length must be greater than zero")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Author must not be empty")
    @Size(max = 50, message = "Author length must be less than fifty chars")
    @Size(min = 1, message = "Author length must be greater than zero")
    @Column(name = "author")
    private String author;

    @NotNull(message = "Price cannot be null")
    @Digits(integer = 3, fraction = 2, message = "Price must have a max of 3 integers and 2 fractions")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @DecimalMax(value = "999.99", message = "Price must be less than 1,000.00")
    @Column(name = "price")
    private BigDecimal price;

    @NotNull(message = "Stock cannot be null")
    @Min(value = 0, message = "Stock must only contain numbers with a minimum of zero")
    @Max(value = 9999, message = "Stock must only contain numbers with a maximum of 9999")
    @Column(name = "stock")
    private int stock;

    @JsonIgnore
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<BasketItem> basketItems = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

}
