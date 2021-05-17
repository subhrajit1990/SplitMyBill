package bill.manager.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bill.manager.domain.CreateGroupDomain;

/**
 * @author Troublem@ker
 */

@Repository
public interface CreateGroup extends CrudRepository<CreateGroupDomain,String> {


}
 