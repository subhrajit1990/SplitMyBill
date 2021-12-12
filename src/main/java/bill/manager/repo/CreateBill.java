/**
 * 
 */
package bill.manager.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bill.manager.domain.AddBillDomain;


/**
 * @author Troublem@ker
 */
@Repository
public interface CreateBill extends CrudRepository<AddBillDomain,String> {


}
 