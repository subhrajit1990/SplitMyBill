/**
 * 
 */
package bill.manager.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bill.manager.domain.ToDoDomain;
import bill.manager.domain.ToDoListDomain;
import bill.manager.model.ToDoAddRequest;
import bill.manager.model.ToDoDeleteRequest;
import bill.manager.model.ToDoFetchRequest;
import bill.manager.model.ToDoFetchResponse;
import bill.manager.model.ToDoListText;
import bill.manager.model.ToDoResponse;
import bill.manager.repo.ToDo;
import bill.manager.utils.CommonUtils;

/**
 * @author Troublem@ker
 */

// https://www.baeldung.com/hibernate-one-to-many

@Service
public class ToDoService implements IToDoService {

	private static final Logger logger = Logger.getLogger(ToDoService.class);

	@Autowired
	private ToDo todo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see bill.manager.service.IToDoService#toDoAdd(bill.manager.model.
	 * ToDoAddRequest, java.lang.String, java.lang.String)
	 */
	@Override
	public ToDoResponse toDoAdd(ToDoAddRequest toDoAddRequest, String channel, String masterTxnRefNo) {

		ToDoDomain toDoDomain = new ToDoDomain();
		Set<ToDoListDomain> toDoListDomain = new HashSet<>();
		try {
			toDoDomain.setChannel(channel);
			toDoDomain.setCreatedDt(CommonUtils.currentTime());
			toDoDomain.setMasterTxnNo(masterTxnRefNo);

			for (ToDoListText toDoListEntries : toDoAddRequest.getToDoList()) {
				ToDoListDomain toDoListValues = new ToDoListDomain();
				toDoListValues.setCreatedDt(CommonUtils.currentTime());
				toDoListValues.setToDoText(toDoListEntries.getToDotext());
				toDoListValues.setToDoDomain(toDoDomain);
				toDoListDomain.add(toDoListValues);
			}

			toDoDomain.setToDoList(toDoListDomain);
			todo.save(toDoDomain);
		} catch (Exception e) {
			logger.error(" error occurred :: " + e.toString());
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bill.manager.service.IToDoService#toDoDelete(bill.manager.model.
	 * ToDoDeleteRequest, java.lang.String, java.lang.String)
	 */
	@Override
	public ToDoResponse toDoDelete(ToDoDeleteRequest toDoRequest, String channel, String masterTxnRefNo) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bill.manager.service.IToDoService#toDoFetch(bill.manager.model.
	 * ToDoFetchRequest, java.lang.String, java.lang.String)
	 */
	@Override
	public ToDoFetchResponse toDoFetch(ToDoFetchRequest toDoRequest, String channel, String masterTxnRefNo) {
		return null;
	}

}
