package osgi.enroute.iot.circuit.application;

import java.util.Collection;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.configurator.annotations.RequireConfigurator;

import osgi.enroute.eventadminserversentevents.capabilities.RequireEventAdminServerSentEventsWebResource;
import osgi.enroute.iot.admin.api.CircuitAdmin;
import osgi.enroute.iot.admin.dto.ICDTO;
import osgi.enroute.iot.admin.dto.WireDTO;
import osgi.enroute.jsonrpc.api.JSONRPC;
import osgi.enroute.jsonrpc.api.RequireJsonrpcWebResource;
import osgi.enroute.webserver.capabilities.RequireWebServerExtender;

/**
 * Main application class for the Circuit editor/viewer.
 */
@RequireWebServerExtender
@RequireConfigurator
@RequireEventAdminServerSentEventsWebResource
@RequireJsonrpcWebResource
@Component(name = "osgi.enroute.iot.circuit", property = JSONRPC.ENDPOINT + "=osgi.enroute.iot.circuit")
public class CircuitApplication implements JSONRPC {

	@Reference
	private CircuitAdmin ca;

	@Override
	public Object getDescriptor() throws Exception {
		return "";
	}

	public Collection<? extends WireDTO> getWires() {
		return ca.getWires();
	}

	public ICDTO[] getDevices() {
		return ca.getICs();
	}

	public boolean disconnect(int wireId) throws Exception {
		return ca.disconnect(wireId);
	}

	public WireDTO connect(String fromDevice, String fromPin, String toDevice, String toPin) throws Exception {
		return ca.connect(fromDevice, fromPin, toDevice, toPin);
	}

}
