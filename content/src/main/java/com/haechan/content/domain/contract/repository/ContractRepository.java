//package com.haechan.content.domain.contract.repository;
//
//import com.haechan.content.domain.contract.entity.Contract;
//import com.haechan.content.domain.distributor.entity.Distributor;
//import com.haechan.content.domain.ost.entity.Ost;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Optional;
//
//public interface ContractRepository extends JpaRepository<Contract, Long> {
//    Optional<Contract> findByOstAndDistributor(Ost ost, Distributor distributor);
//}
