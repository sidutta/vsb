package eu.chorevolution.vsb.test.trafficlight.bc;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import eu.chorevolution.vsb.gm.protocols.builders.ResponseBuilder;
import eu.chorevolution.vsb.gm.protocols.primitives.BcGmSubcomponent;
import eu.chorevolution.vsb.gmdl.utils.Data;

/**
 * This class was generated by the CHOReVOLUTION BindingComponent Generator
 * using com.sun.codemodel 2.6
 * 
 */
@WebService(serviceName = "BindingComponent", targetNamespace = "eu.chorevolution.vsb.test.trafficlight.bc")
public class BindingComponent {

	private final BcGmSubcomponent apiRef;

	public BindingComponent(BcGmSubcomponent apiRef) {
		this.apiRef = apiRef;
	}

	@WebMethod
	public TrafficLight getTrafficLight(Integer id) {
		List<Data<?>> datas = new ArrayList<Data<?>>();
		datas.add(new Data<Integer>("id", "Integer", true, id, "PATH"));
		String serializedlight = this.apiRef.mgetTwowaySync("/traffic-lights/{id}",
				datas);
		System.out.println("Answer from REST: " + serializedlight);
		return ResponseBuilder.unmarshalObject("application/json", serializedlight,
				TrafficLight.class);
	}

	@WebMethod
	public void postTrafficLight(TrafficLight light) {
		List<Data<?>> datas = new ArrayList<Data<?>>();
		datas.add(new Data<TrafficLight>("light", "TrafficLight", false, light,
				"BODY"));
		this.apiRef.mgetOneway("/traffic-lights", datas);
	}

}
