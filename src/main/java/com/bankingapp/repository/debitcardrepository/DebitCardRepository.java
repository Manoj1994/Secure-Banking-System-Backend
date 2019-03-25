package com.bankingapp.repository.debitcardrepository;

import com.bankingapp.model.account.DebitCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebitCardRepository extends JpaRepository<DebitCard, Long> {

    //Account findById(int id);

}
