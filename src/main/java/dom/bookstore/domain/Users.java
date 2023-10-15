package dom.bookstore.domain;


import com.fasterxml.jackson.annotation.JsonInclude;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder(toBuilder = true)
@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"userId"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ToString(of = {"userId", "firstName", "lastName", "addressLine1", "addressLine2", "postCode"})
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotEmpty(message = "First name must not be empty")
    @Max(value = 50, message = "First name length must be less than one hundred chars")
    @Min(value = 1, message = "First name length must be greater than zero")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "Last name must not be empty")
    @Max(value = 50, message = "Last name length must be less than one hundred chars")
    @Min(value = 1, message = "Last name length must be greater than zero")
    @Column(name = "last_name")
    private String lastName;

    @NotEmpty(message = "Address line must not be empty")
    @Max(value = 100, message = "Address line length must be less than one hundred chars")
    @Min(value = 1, message = "Address line length must be greater than zero")
    @Column(name = "address_line_1")
    private String addressLine1;

    @NotEmpty(message = "Address line must not be empty")
    @Max(value = 100, message = "Address line length must be less than one hundred chars")
    @Min(value = 1, message = "Address line length must be greater than zero")
    @Column(name = "address_line_2")
    private String addressLine2;

    @NotEmpty(message = "Postcode must not be empty")
    @Max(value = 6, message = "Postcode length must be less than one hundred chars")
    @Min(value = 3, message = "Postcode length must be greater than zero")
    @Column(name = "post_code")
    private String postCode;

    @OneToMany(mappedBy = "orderDetailsId", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List <OrderDetails> orderDetails;

    @OneToMany(mappedBy = "basketId", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Basket> basket;

//    @OneToOne(mappedBy = "users", fetch = FetchType.LAZY)
//    private Basket basket;

}
