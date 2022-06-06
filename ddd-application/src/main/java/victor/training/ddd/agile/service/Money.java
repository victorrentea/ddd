//package victor.training.ddd.agile.service;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import java.util.Objects;
//import java.util.stream.Stream;
//
//public class Money {
//    public static final Money ZERO = new Money(0, Currency.EUR);
//    private final Integer amount;
//    private final Currency currency;
//
//    public Money(int amount, Currency currency) {
//        this.amount = amount;
//        this.currency = Objects.requireNonNull(currency);
//    }
//
//    public Currency getCurrency() {
//        return currency;
//    }
//
//    public Integer getAmount() {
//        return amount;
//    }
//
//    @Override
//    public String toString() {
//        return "Money{" +
//               "amount=" + amount +
//               ", currency=" + currency +
//               '}';
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Money money = (Money) o;
//        return Objects.equals(amount, money.amount) && currency == money.currency;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(amount, currency);
//    }
//
//    public Money add(Money other) {
//        if (currency != other.currency) {
//            throw new IllegalArgumentException("Cannot add.");
//        }
//        int newAmount = amount + other.amount;
//        return new Money(newAmount, currency);
//    }
//}
//class MoneyService { // Manager
////    public Money add(Money a, Money other) {
////        if (a.getCurrency() != other.getCurrency()) {
////            throw new IllegalArgumentException("Cannot add.");
////        }
////        int newAmount = a.getAmount() + other.getAmount();
////        return new Money(newAmount, a.getCurrency());
////    }
//}
//class FullName {
//    private final String firstName;
//    private final String lastName;
//
//    FullName(String firstName, String lastName) {
//        if (firstName == null && lastName == null) {
//            throw new IllegalArgumentException();
//        }
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }
//}
//@Entity
//class Customer { // an Entity, not a Value Object (ID)
//    @Id
//    @GeneratedValue
//    private Long id; // in DB
//    private FullName fullName;
////    private String firstName;
////    private String lastName;
//
//    private Money money;
//
//    public Money getMoney() {
//        return money;
//    }
//
//    public void setMoney(Money money) {
//        this.money = money;
//    }
//
//    // DO NOT implement hashcode equals on ENtities when using Hibernate
//}
//class SomeUsecase {
//
//    public void whyHibernateIsTricky() {
//        Customer customer = new Customer();
//        // customer.id is null over here
//        customerRepo.save(customer);
//        // customer has an ID not null here >> the hashcode has changed
//    }
//    public void method(Customer customer, Money moreMoney) {
//
//        Money reduce = Stream.of(
//                        new Money(1, Currency.EUR),
//                        new Money(2, Currency.EUR))
//                .reduce(Money.ZERO, (money, other) -> money.add(other));
//        Money newMoney = customer.getMoney().add(moreMoney);
//        customer.setMoney(newMoney);
//    }
//
//}
//enum Currency {EUR,USD, RON, MKD}
