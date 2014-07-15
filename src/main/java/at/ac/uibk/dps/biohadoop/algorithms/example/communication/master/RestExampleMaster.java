package at.ac.uibk.dps.biohadoop.algorithms.example.communication.master;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import at.ac.uibk.dps.biohadoop.algorithms.example.algorithm.Example;
import at.ac.uibk.dps.biohadoop.communication.Message;
import at.ac.uibk.dps.biohadoop.communication.master.rest.RestResource;

import com.fasterxml.jackson.core.type.TypeReference;

@Path("/example")
@Produces(MediaType.APPLICATION_JSON)
public class RestExampleMaster extends RestResource<Integer, String> {

	@Override
	public String getQueueName() {
		return Example.EXAMPLE_QUEUE;
	}

	@Override
	public Object getRegistrationObject() {
		return null;
	}

	@Override
	public TypeReference<Message<String>> getInputType() {
		return new TypeReference<Message<String>>() {
		};
	}

}
