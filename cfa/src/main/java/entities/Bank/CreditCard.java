package entities.Bank;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "credit_cards")
public class CreditCard extends BillingDetail {

    private String cardType;
    private Integer expirationMonth;
    private Integer expirationYear;

    public CreditCard() {

    }

    @Column(name = "card_type")
    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    @Column(name = "expiration_month", nullable = false)
    public Integer getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(Integer expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    @Column(name = "expiration_year", nullable = false)
    public Integer getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(Integer expirationYear) {
        this.expirationYear = expirationYear;
    }
}
