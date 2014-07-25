package at.ac.uibk.dps.biohadoop.algorithms.deletable;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import at.ac.uibk.dps.biohadoop.algorithms.moead.algorithm.Moead;
import at.ac.uibk.dps.biohadoop.communication.Message;
import at.ac.uibk.dps.biohadoop.communication.master.rest.RestResource;

import com.fasterxml.jackson.core.type.TypeReference;

@Path("/moead")
@Produces(MediaType.APPLICATION_JSON)
public class MoeadRest extends RestResource<double[], double[]> {

	@Override
	public String getQueueName() {
		return Moead.MOEAD_QUEUE;
	}

	@Override
	public Object getRegistrationObject() {
		return null;
	}

	@Override
	public TypeReference<Message<double[]>> getInputType() {
		return new TypeReference<Message<double[]>>() {
		};
	}

}
