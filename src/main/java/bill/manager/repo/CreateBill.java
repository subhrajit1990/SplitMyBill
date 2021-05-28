/**
 * 
 */
package bill.manager.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bill.manager.domain.AddBillDomain;
import bill.manager.domain.BillsIdDomain;


/**
 * @author Troublem@ker
 */
@Repository
public interface CreateBill extends CrudRepository<AddBillDomain,String> {

	/**
	 * @param listIds
	 * @return
	 */
	//Iterable<AddBillDomain> findAllById(List<BillsIdDomain> listIds);

	/**
	 * @param parseLong
	 * @return
	 */
	Iterable<AddBillDomain> findById(long parseLong);


}
 