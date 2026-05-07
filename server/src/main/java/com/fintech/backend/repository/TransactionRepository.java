package com.fintech.backend.repository;

import com.fintech.backend.dto.TransactionProjection;
import com.fintech.backend.entity.Transaction;
import com.fintech.backend.enums.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByWalletId(Long walletId);

    List<Transaction> findByWalletIdOrderByCreatedAtDesc(Long walletId);

    List<Transaction> findByWalletIdAndStatus(Long walletId, TransactionStatus status);

    Optional<Transaction> findByReference(String reference);

    @Query("SELECT t FROM Transaction t WHERE t.wallet.id IN :walletIds ORDER BY t.createdAt DESC")
    List<Transaction> findByWalletIds(@Param("walletIds") List<Long> walletIds);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.wallet.id IN :walletIds AND t.status = 'COMPLETED' AND t.type IN ('DEPOSIT','WITHDRAWAL','TRANSFER')")
    BigDecimal sumCompletedVolume(@Param("walletIds") List<Long> walletIds);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.wallet.id IN :walletIds AND t.status = 'COMPLETED' AND t.type IN ('DEPOSIT','WITHDRAWAL','TRANSFER') AND t.createdAt >= :since")
    BigDecimal sumVolumeSince(@Param("walletIds") List<Long> walletIds, @Param("since") java.time.LocalDateTime since);

    long countByStatus(TransactionStatus status);

    Page<Transaction> findByWalletIdOrderByCreatedAtDesc(Long walletId, Pageable pageable);

    Page<Transaction> findByWalletIdAndStatusOrderByCreatedAtDesc(Long walletId, TransactionStatus status, Pageable pageable);

    List<TransactionProjection> findAllProjectedBy();

    List<TransactionProjection> findProjectedByWalletIdOrderByCreatedAtDesc(Long walletId);

    List<TransactionProjection> findProjectedByWalletIdAndStatusOrderByCreatedAtDesc(Long walletId, TransactionStatus status);

    @Query("SELECT t FROM Transaction t WHERE t.wallet.id IN :walletIds ORDER BY t.createdAt DESC")
    List<TransactionProjection> findProjectedByWalletIds(@Param("walletIds") List<Long> walletIds);

    @Query("SELECT t FROM Transaction t WHERE t.status = :status ORDER BY t.createdAt DESC")
    List<TransactionProjection> findProjectedByStatus(@Param("status") TransactionStatus status);

    Optional<TransactionProjection> findProjectedById(Long id);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.wallet.id = :walletId AND t.status = 'COMPLETED' AND t.type IN ('DEPOSIT','WITHDRAWAL','TRANSFER') AND t.createdAt >= :since")
    long countTransactionsSince(@Param("walletId") Long walletId, @Param("since") java.time.LocalDateTime since);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.wallet.id = :walletId AND t.status = 'COMPLETED' AND t.type IN ('DEPOSIT','WITHDRAWAL','TRANSFER') AND t.createdAt >= :since")
    BigDecimal sumVolumePerWalletSince(@Param("walletId") Long walletId, @Param("since") java.time.LocalDateTime since);
}
