/**
 * 
 */
package bill.manager.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bill.manager.domain.ToDoListDomain;

/**
 * @author Troublem@ker
 */
@Repository
public interface ToDoList extends CrudRepository<ToDoListDomain,String> {

}
