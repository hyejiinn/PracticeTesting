package sample.cafekiosk.spring.domain.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>
{
	/**
	 * SELECT * FROM PRODUCT
	 * WHERE SELLING_STATUS IN ('SELLING', 'HOLD');
	 */
	List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);

}
